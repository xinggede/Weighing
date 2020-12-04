package com.yingmei.scancode.core;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

public abstract class QRCodeView extends RelativeLayout implements Camera.PreviewCallback {
    private static final int NO_CAMERA_ID = -1;
    protected CameraPreview mCameraPreview;
    protected ScanBoxView mScanBoxView;
    protected Delegate mDelegate;
    protected boolean mSpotAble = false;
    protected ProcessDataTask mProcessDataTask;
    private Paint mPaint;
    protected BarcodeType mBarcodeType = BarcodeType.HIGH_FREQUENCY;
    private long startTime = 0;
    private PointF[] mLocationPoints;
    // 上次环境亮度记录的时间戳
    private long mLastAmbientBrightnessRecordTime = System.currentTimeMillis();
    // 上次环境亮度记录的索引
    private int mAmbientBrightnessDarkIndex = 0;
    // 环境亮度历史记录的数组，255 是代表亮度最大值
    private static final long[] AMBIENT_BRIGHTNESS_DARK_LIST = new long[]{255, 255, 255, 255};
    // 环境亮度扫描间隔
    private static final int AMBIENT_BRIGHTNESS_WAIT_SCAN_TIME = 150;
    // 亮度低的阀值
    private static final int AMBIENT_BRIGHTNESS_DARK = 60;

    private int width, height;

    public QRCodeView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public QRCodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
        setupReader();
    }

    private void initView(Context context, AttributeSet attrs) {
        mCameraPreview = new CameraPreview(context);
        mCameraPreview.setDelegate(new CameraPreview.Delegate() {
            @Override
            public void onStartPreview() {
                setOneShotPreviewCallback();
            }

            @Override
            public void onError() {
                if (mDelegate != null) {
                    mDelegate.onScanQRCodeOpenCameraError();
                }
            }
        });

        mScanBoxView = new ScanBoxView(context);
        mScanBoxView.init(this, attrs);
        mCameraPreview.setId(View.generateViewId());
        addView(mCameraPreview);
        LayoutParams layoutParams = new LayoutParams(context, attrs);
        layoutParams.addRule(RelativeLayout.ALIGN_TOP, mCameraPreview.getId());
        layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, mCameraPreview.getId());
        addView(mScanBoxView, layoutParams);

        mPaint = new Paint();
        mPaint.setColor(getScanBoxView().getCornerColor());
        mPaint.setStyle(Paint.Style.FILL);
    }

    private void setOneShotPreviewCallback() {
        if (mSpotAble && isPreviewing()) {
            try {
                getCamera().setOneShotPreviewCallback(this);
                Camera.Parameters parameters = getCamera().getParameters();
                Camera.Size size = parameters.getPreviewSize();
                width = size.width;
                height = size.height;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Camera getCamera() {
        return mCameraPreview.getCamera();
    }

    protected abstract void setupReader();

    /**
     * 设置扫描二维码的代理
     *
     * @param delegate 扫描二维码的代理
     */
    public void setDelegate(Delegate delegate) {
        mDelegate = delegate;
    }

    public CameraPreview getCameraPreview() {
        return mCameraPreview;
    }

    public ScanBoxView getScanBoxView() {
        return mScanBoxView;
    }

    public void scanFile(String filePath) {
        mCameraPreview.isScanFile = true;
        DecodeImageRunnable decodeImageRunnable = new DecodeImageRunnable(filePath, handler);
        ThreadPoolManager.getInstance().execute(decodeImageRunnable);

    }

    /**
     * 显示扫描框
     */
    public void showScanRect() {
        if (mScanBoxView != null) {
            mScanBoxView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 隐藏扫描框
     */
    public void hiddenScanRect() {
        if (mScanBoxView != null) {
            mScanBoxView.setVisibility(View.GONE);
        }
    }

    /**
     * 打开后置摄像头开始预览，但是并未开始识别
     */
    public void startCamera() {
        mCameraPreview.startCamera();
    }

    /**
     * 关闭摄像头预览，并且隐藏扫描框
     */
    public void stopCamera() {
        try {
            stopSpotAndHiddenRect();
            mCameraPreview.stopCameraPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 开始识别
     */
    public void startSpot() {
        mSpotAble = true;
        startTime = System.currentTimeMillis();
        setOneShotPreviewCallback();
    }

    /**
     * 停止识别
     */
    public void stopSpot() {
        mSpotAble = false;
        if (getCamera() != null) {
            try {
                getCamera().setOneShotPreviewCallback(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        handler.removeCallbacksAndMessages(null);
        if (mProcessDataTask != null) {
            mProcessDataTask.cancelTask();
            mProcessDataTask = null;
        }
        ThreadPoolManager.getInstance().shutDown();
    }

    /**
     * 停止识别，并且隐藏扫描框
     */
    public void stopSpotAndHiddenRect() {
        stopSpot();
        hiddenScanRect();
    }

    /**
     * 显示扫描框，并开始识别
     */
    public void startSpotAndShowRect() {
        startSpot();
        showScanRect();
    }

    public boolean isPreviewing(){
        return mCameraPreview.isPreviewing();
    }

    /**
     * 打开闪光灯
     */
    public void openFlashlight() {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                mCameraPreview.openFlashlight();
            }
        }, mCameraPreview.isPreviewing() ? 0 : 500);
    }

    public boolean switchFlashlight() {
        return mCameraPreview.switchFlashlight();
    }

    /**
     * 关闭闪光灯
     */
    public void closeFlashlight() {
        mCameraPreview.closeFlashlight();
    }

    /**
     * 销毁二维码扫描控件
     */
    public void onDestroy() {
        stopCamera();
        mDelegate = null;
    }

    /**
     * 切换成扫描条码样式
     */
    public void changeToScanBarcodeStyle() {
        if (!mScanBoxView.getIsBarcode()) {
            mScanBoxView.setIsBarcode(true);
        }
    }

    /**
     * 切换成扫描二维码样式
     */
    public void changeToScanQRCodeStyle() {
        if (mScanBoxView.getIsBarcode()) {
            mScanBoxView.setIsBarcode(false);
        }
    }

    /**
     * 当前是否为条码扫描样式
     */
    public boolean getIsScanBarcodeStyle() {
        return mScanBoxView.getIsBarcode();
    }

    @Override
    public void onPreviewFrame(final byte[] data, final Camera camera) {
        /*if (mCameraPreview != null && mCameraPreview.isPreviewing()) {
            try {
                handleAmbientBrightness(data, camera);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/

        if (!mSpotAble) {
            return;
        }
//        if ((mProcessDataTask != null && (mProcessDataTask.getStatus() == AsyncTask.Status.PENDING
//                || mProcessDataTask.getStatus() == AsyncTask.Status.RUNNING))) {
//            return;
//        }
//        mProcessDataTask = new ProcessDataTask(camera, data, this, BGAQRCodeUtil.isPortrait(getContext())).perform();

        ThreadPoolManager.getInstance().execute(new ProcessDecodeRunnable(width, height, data, this, BGAQRCodeUtil.isPortrait(getContext()), handler, count % 2 == 0));
        count++;
        if (System.currentTimeMillis() - startTime >= 5000) {
            handler.sendEmptyMessage(10);
        }
    }

    private void setAutoZoom(int zoom, int min) {
        mCameraPreview.setAutoZoom(zoom, min, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
//                handler.sendEmptyMessageDelayed(1, 1000);
            }
        });
    }

    int count = 0;

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                onRetry(null);
            } else if (msg.what == 1) {
                onRetry(null);
            } else if (msg.what == 2) {
                count = 0;
                startTime = System.currentTimeMillis();
                if (msg.obj != null) {
                    onRetry((ScanResult) msg.obj);
                } else {
                    onRetry(null);
                }
            } else if (msg.what == 3) {
                mCameraPreview.isScanFile = false;
                String result = (String) msg.obj;
                if (mDelegate != null) {
                    mDelegate.onScanQRCodeSuccess(result);
                }
            } else if (msg.what == 10) {
                int maxZoom = getCamera().getParameters().getMaxZoom();
                int zoomStep = maxZoom / 4;
                int zoom = getCamera().getParameters().getZoom();
                int newZoom = zoom + zoomStep;
                if (zoom >= zoomStep) {
                    newZoom = zoom - zoomStep;
                }
                setAutoZoom(zoom, Math.min(newZoom, maxZoom));
                startTime = System.currentTimeMillis();
            }
        }
    };

    synchronized void onRetry(ScanResult scanResult) {
        if (!mSpotAble) {
            return;
        }
        String result = scanResult == null ? null : scanResult.result;
        if (TextUtils.isEmpty(result)) {
            try {
                if (getCamera() != null) {
                    getCamera().setOneShotPreviewCallback(QRCodeView.this);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            mSpotAble = false;
            try {
                ThreadPoolManager.getInstance().shutDown();
                if (mDelegate != null) {
                    mDelegate.onScanQRCodeSuccess(result);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void handleAmbientBrightness(byte[] data, Camera camera) {
        if (mCameraPreview == null || !mCameraPreview.isPreviewing()) {
            return;
        }
        long currentTime = System.currentTimeMillis();
        if (currentTime - mLastAmbientBrightnessRecordTime < AMBIENT_BRIGHTNESS_WAIT_SCAN_TIME) {
            return;
        }
        mLastAmbientBrightnessRecordTime = currentTime;

        int width = camera.getParameters().getPreviewSize().width;
        int height = camera.getParameters().getPreviewSize().height;
        // 像素点的总亮度
        long pixelLightCount = 0L;
        // 像素点的总数
        long pixelCount = width * height;
        // 采集步长，因为没有必要每个像素点都采集，可以跨一段采集一个，减少计算负担，必须大于等于1。
        int step = 10;
        // data.length - allCount * 1.5f 的目的是判断图像格式是不是 YUV420 格式，只有是这种格式才相等
        //因为 int 整形与 float 浮点直接比较会出问题，所以这么比
        if (Math.abs(data.length - pixelCount * 1.5f) < 0.00001f) {
            for (int i = 0; i < pixelCount; i += step) {
                // 如果直接加是不行的，因为 data[i] 记录的是色值并不是数值，byte 的范围是 +127 到 —128，
                // 而亮度 FFFFFF 是 11111111 是 -127，所以这里需要先转为无符号 unsigned long 参考 Byte.toUnsignedLong()
                pixelLightCount += ((long) data[i]) & 0xffL;
            }
            // 平均亮度
            long cameraLight = pixelLightCount / (pixelCount / step);
            // 更新历史记录
            int lightSize = AMBIENT_BRIGHTNESS_DARK_LIST.length;
            AMBIENT_BRIGHTNESS_DARK_LIST[mAmbientBrightnessDarkIndex = mAmbientBrightnessDarkIndex % lightSize] = cameraLight;
            mAmbientBrightnessDarkIndex++;
            boolean isDarkEnv = true;
            // 判断在时间范围 AMBIENT_BRIGHTNESS_WAIT_SCAN_TIME * lightSize 内是不是亮度过暗
            for (long ambientBrightness : AMBIENT_BRIGHTNESS_DARK_LIST) {
                if (ambientBrightness > AMBIENT_BRIGHTNESS_DARK) {
                    isDarkEnv = false;
                    break;
                }
            }
            BGAQRCodeUtil.d("摄像头环境亮度为：" + cameraLight);
            if (mDelegate != null) {
                mDelegate.onCameraAmbientBrightnessChanged(isDarkEnv);
            }
        }
    }

    /**
     * 解析本地图片二维码。返回二维码图片里的内容 或 null
     *
     * @param picturePath 要解析的二维码图片本地路径
     */
    public void decodeQRCode(String picturePath) {
        mProcessDataTask = new ProcessDataTask(picturePath, this).perform();
    }

    /**
     * 解析 Bitmap 二维码。返回二维码图片里的内容 或 null
     *
     * @param bitmap 要解析的二维码图片
     */
    public void decodeQRCode(Bitmap bitmap) {
        mProcessDataTask = new ProcessDataTask(bitmap, this).perform();
    }

    protected abstract ScanResult processData(byte[] data, int width, int height, boolean isRetry);

    protected abstract ScanResult processBitmapData(Bitmap bitmap);

    public boolean transformToViewCoordinates(final PointF[] pointArr, final Rect scanBoxAreaRect, final boolean isNeedAutoZoom, final String result, int width) {
        if (pointArr == null || pointArr.length == 0) {
            return false;
        }
        try {
            // 不管横屏还是竖屏，size.width 大于 size.height
            Camera.Size size = getCamera().getParameters().getPreviewSize();
            boolean isMirrorPreview = mCameraPreview.mCameraId == Camera.CameraInfo.CAMERA_FACING_FRONT;
            int statusBarHeight = BGAQRCodeUtil.getStatusBarHeight(getContext());

            PointF[] transformedPoints = new PointF[pointArr.length];
            int index = 0;
            for (PointF qrPoint : pointArr) {
                transformedPoints[index] = transform(qrPoint.x, qrPoint.y, size.width, size.height, isMirrorPreview, statusBarHeight, scanBoxAreaRect);
                index++;
            }
            mLocationPoints = transformedPoints;
            postInvalidate();

            if (isNeedAutoZoom) {
                return mCameraPreview.handleAutoZoom(transformedPoints, width, new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        onPostParseData(new ScanResult(result));
                    }
                });
            }
            return false;
        } catch (Exception e) {
            mLocationPoints = null;
            e.printStackTrace();
            return false;
        }
    }

    private PointF transform(float originX, float originY, float cameraPreviewWidth, float cameraPreviewHeight, boolean isMirrorPreview, int statusBarHeight,
                             final Rect scanBoxAreaRect) {
        int viewWidth = getWidth();
        int viewHeight = getHeight();

        PointF result;
        float scaleX;
        float scaleY;

        if (BGAQRCodeUtil.isPortrait(getContext())) {
            scaleX = viewWidth / cameraPreviewHeight;
            scaleY = viewHeight / cameraPreviewWidth;
            result = new PointF((cameraPreviewHeight - originX) * scaleX, (cameraPreviewWidth - originY) * scaleY);
            result.y = viewHeight - result.y;
            result.x = viewWidth - result.x;

            if (scanBoxAreaRect == null) {
                result.y += statusBarHeight;
            }
        } else {
            scaleX = viewWidth / cameraPreviewWidth;
            scaleY = viewHeight / cameraPreviewHeight;
            result = new PointF(originX * scaleX, originY * scaleY);
            if (isMirrorPreview) {
                result.x = viewWidth - result.x;
            }
        }

        if (scanBoxAreaRect != null) {
            result.y += scanBoxAreaRect.top;
            result.x += scanBoxAreaRect.left;
        }

        return result;
    }


    void onPostParseData(ScanResult scanResult) {
        if (!mSpotAble) {
            return;
        }
        String result = scanResult == null ? null : scanResult.result;
        if (TextUtils.isEmpty(result)) {
            try {
                if (getCamera() != null) {
                    getCamera().setOneShotPreviewCallback(QRCodeView.this);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            mSpotAble = false;
            try {
                if (mDelegate != null) {
                    mDelegate.onScanQRCodeSuccess(result);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void onPostParseBitmapOrPicture(ScanResult scanResult) {
        if (mDelegate != null) {
            String result = scanResult == null ? null : scanResult.result;
            mDelegate.onScanQRCodeSuccess(result);
        }
    }

    void onScanBoxRectChanged(Rect rect) {
        mCameraPreview.onScanBoxRectChanged(rect);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);


        if (!isShowLocationPoint() || mLocationPoints == null) {
            return;
        }

        for (PointF pointF : mLocationPoints) {
            canvas.drawCircle(pointF.x, pointF.y, 10, mPaint);
        }
        mLocationPoints = null;
        postInvalidateDelayed(2000);
    }

    /**
     * 是否显示定位点
     */
    protected boolean isShowLocationPoint() {
        return mScanBoxView != null && mScanBoxView.isShowLocationPoint();
    }

    /**
     * 是否自动缩放
     */
    protected boolean isAutoZoom() {
        return mScanBoxView != null && mScanBoxView.isAutoZoom();
    }


    public interface Delegate {
        /**
         * 处理扫描结果
         *
         * @param result 摄像头扫码时只要回调了该方法 result 就一定有值，不会为 null。解析本地图片或 Bitmap 时 result 可能为 null
         */
        void onScanQRCodeSuccess(String result);

        /**
         * 摄像头环境亮度发生变化
         *
         * @param isDark 是否变暗
         */
        void onCameraAmbientBrightnessChanged(boolean isDark);

        /**
         * 处理打开相机出错
         */
        void onScanQRCodeOpenCameraError();
    }
}
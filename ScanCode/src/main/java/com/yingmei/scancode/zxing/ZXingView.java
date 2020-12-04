package com.yingmei.scancode.zxing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.GlobalHistogramBinarizer;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.yingmei.scancode.core.BGAQRCodeUtil;
import com.yingmei.scancode.core.BarcodeType;
import com.yingmei.scancode.core.QRCodeView;
import com.yingmei.scancode.core.ScanResult;

import java.util.Hashtable;
import java.util.Map;


public class ZXingView extends QRCodeView {
    private MultiFormatReader mMultiFormatReader;
    private Map<DecodeHintType, Object> mHintMap;

    public ZXingView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ZXingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void setupReader() {
        mMultiFormatReader = new MultiFormatReader();

        if (mBarcodeType == BarcodeType.ONE_DIMENSION) {
            mMultiFormatReader.setHints(QRCodeDecoder.ONE_DIMENSION_HINT_MAP);
        } else if (mBarcodeType == BarcodeType.TWO_DIMENSION) {
            mMultiFormatReader.setHints(QRCodeDecoder.TWO_DIMENSION_HINT_MAP);
        } else if (mBarcodeType == BarcodeType.ONLY_QR_CODE) {
            mMultiFormatReader.setHints(QRCodeDecoder.QR_CODE_HINT_MAP);
        } else if (mBarcodeType == BarcodeType.ONLY_CODE_128) {
            mMultiFormatReader.setHints(QRCodeDecoder.CODE_128_HINT_MAP);
        } else if (mBarcodeType == BarcodeType.ONLY_EAN_13) {
            mMultiFormatReader.setHints(QRCodeDecoder.EAN_13_HINT_MAP);
        } else if (mBarcodeType == BarcodeType.HIGH_FREQUENCY) {
            mMultiFormatReader.setHints(QRCodeDecoder.HIGH_FREQUENCY_HINT_MAP);
        } else if (mBarcodeType == BarcodeType.CUSTOM) {
            mMultiFormatReader.setHints(mHintMap);
        } else {
            mMultiFormatReader.setHints(QRCodeDecoder.ALL_HINT_MAP);
        }
    }

    /**
     * 设置识别的格式
     *
     * @param barcodeType 识别的格式
     * @param hintMap     barcodeType 为 BarcodeType.CUSTOM 时，必须指定该值
     */
    public void setType(BarcodeType barcodeType, Map<DecodeHintType, Object> hintMap) {
        mBarcodeType = barcodeType;
        mHintMap = hintMap;

        if (mBarcodeType == BarcodeType.CUSTOM && (mHintMap == null || mHintMap.isEmpty())) {
            throw new RuntimeException("barcodeType 为 BarcodeType.CUSTOM 时 hintMap 不能为空");
        }
        setupReader();
    }

    @Override
    protected ScanResult processBitmapData(Bitmap bitmap) {
        return new ScanResult(QRCodeDecoder.syncDecodeQRCode(bitmap));
    }

    /*@Override
    protected ScanResult processData(byte[] data, int width, int height, boolean isRetry) {
        Result rawResult = null;
        Rect scanBoxAreaRect = null;

        try {
            PlanarYUVLuminanceSource source;
            scanBoxAreaRect = mScanBoxView.getScanBoxAreaRect(height);
            if (scanBoxAreaRect != null) {
                source = new PlanarYUVLuminanceSource(data, width, height, scanBoxAreaRect.left, scanBoxAreaRect.top, scanBoxAreaRect.width(),
                        scanBoxAreaRect.height(), false);
            } else {
                source = new PlanarYUVLuminanceSource(data, width, height, 0, 0, width, height, false);
            }

            BGAQRCodeUtil.d("开始识别:" + System.currentTimeMillis());
            rawResult = mMultiFormatReader.decodeWithState(new BinaryBitmap(new HybridBinarizer(source)));
            BGAQRCodeUtil.d("识别结果:" + rawResult);
            if (rawResult == null) {
                rawResult = mMultiFormatReader.decodeWithState(new BinaryBitmap(new GlobalHistogramBinarizer(source)));
                if (rawResult != null) {
                    BGAQRCodeUtil.d("HybridBinarizer没识别到，GlobalHistogramBinarizer 能识别到");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            BGAQRCodeUtil.d("识别异常:" + System.currentTimeMillis());
        } finally {
            mMultiFormatReader.reset();
        }

        if (rawResult == null) {
            return null;
        }

        String result = rawResult.getText();
        if (TextUtils.isEmpty(result)) {
            return null;
        }

        BarcodeFormat barcodeFormat = rawResult.getBarcodeFormat();
        BGAQRCodeUtil.d("格式为：" + barcodeFormat.name());

        // 处理自动缩放和定位点
        boolean isNeedAutoZoom = isNeedAutoZoom(barcodeFormat);
        if (isShowLocationPoint() || isNeedAutoZoom) {
            ResultPoint[] resultPoints = rawResult.getResultPoints();
            final PointF[] pointArr = new PointF[resultPoints.length];
            int pointIndex = 0;
            for (ResultPoint resultPoint : resultPoints) {
                pointArr[pointIndex] = new PointF(resultPoint.getX(), resultPoint.getY());
                pointIndex++;
            }

            if (transformToViewCoordinates(pointArr, scanBoxAreaRect, isNeedAutoZoom, result, mScanBoxView.getRectWidth())) {
                return null;
            }
        }
        return new ScanResult(result);
    }*/

    private MultiFormatReader createMultiFormatReader() {
        MultiFormatReader mfr = new MultiFormatReader();
        mfr.setHints(QRCodeDecoder.ALL_HINT_MAP);
        return mfr;
    }

    /**
     * 二维码解析器
     *
     * @return
     */
    private QRCodeReader createReader() {
        QRCodeReader mQrCodeReader = new QRCodeReader();
        Hashtable mHints = new Hashtable<>();
        mHints.put(DecodeHintType.CHARACTER_SET, "utf-8");
        mHints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
        mHints.put(DecodeHintType.POSSIBLE_FORMATS, BarcodeFormat.QR_CODE);
        return mQrCodeReader;
    }

    @Override
    protected ScanResult processData(byte[] data, int width, int height, boolean isRetry) {
        Result rawResult = null;
        Rect scanBoxAreaRect = null;

        long stime = System.currentTimeMillis();
        BGAQRCodeUtil.d("开始识别:" + stime);


        MultiFormatReader mfr = createMultiFormatReader();

        PlanarYUVLuminanceSource source;
        try {
            scanBoxAreaRect = mScanBoxView.getScanBoxAreaRect(width, height);
            if (scanBoxAreaRect != null) {
                source = new PlanarYUVLuminanceSource(data, width, height, scanBoxAreaRect.left, scanBoxAreaRect.top, scanBoxAreaRect.width(),
                        scanBoxAreaRect.height(), false);
            } else {
                source = new PlanarYUVLuminanceSource(data, width, height, 0, 0, width, height, false);
            }
            BinaryBitmap binaryBitmap;
            if (!isRetry) {
                BGAQRCodeUtil.d("HybridBinarizer识别");
                binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));
            } else {
                BGAQRCodeUtil.d("GlobalHistogramBinarizer识别");
                binaryBitmap = new BinaryBitmap(new GlobalHistogramBinarizer(source));
            }
            rawResult = mfr.decodeWithState(binaryBitmap);
            BGAQRCodeUtil.d("识别结果:" + rawResult);
        } catch (Exception e) {
            e.printStackTrace();
            BGAQRCodeUtil.d("识别异常:" + (System.currentTimeMillis() - stime));
        } finally {
            mfr.reset();
        }
        if (rawResult == null) {
            return null;
        }
        String result = rawResult.getText();
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        return new ScanResult(result);
    }

    private boolean isNeedAutoZoom(BarcodeFormat barcodeFormat) {
        return isAutoZoom() && barcodeFormat == BarcodeFormat.QR_CODE;
    }
}
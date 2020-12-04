package com.yingmei.scancode.zxing;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.GlobalHistogramBinarizer;
import com.google.zxing.common.HybridBinarizer;
import com.yingmei.scancode.core.BGAQRCodeUtil;
import com.yingmei.scancode.core.BitmapLuminanceSource;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;


/**
 * 描述:解析二维码图片。一维条码、二维码各种类型简介 https://blog.csdn.net/xdg_blog/article/details/52932707
 */
public class QRCodeDecoder {
    public static final Map<DecodeHintType, Object> ALL_HINT_MAP = new EnumMap<>(DecodeHintType.class);

    static {
        List<BarcodeFormat> allFormatList = new ArrayList<>();
        allFormatList.add(BarcodeFormat.AZTEC);
        allFormatList.add(BarcodeFormat.CODABAR);
        allFormatList.add(BarcodeFormat.CODE_39);
        allFormatList.add(BarcodeFormat.CODE_93);
        allFormatList.add(BarcodeFormat.CODE_128);
        allFormatList.add(BarcodeFormat.ITF);

        allFormatList.add(BarcodeFormat.DATA_MATRIX);
        allFormatList.add(BarcodeFormat.EAN_8);
        allFormatList.add(BarcodeFormat.EAN_13);
        allFormatList.add(BarcodeFormat.RSS_14);
        allFormatList.add(BarcodeFormat.RSS_EXPANDED);
        allFormatList.add(BarcodeFormat.UPC_A);
        allFormatList.add(BarcodeFormat.UPC_E);

        allFormatList.add(BarcodeFormat.MAXICODE);
//        allFormatList.add(BarcodeFormat.PDF_417);
        allFormatList.add(BarcodeFormat.QR_CODE);
//        allFormatList.add(BarcodeFormat.UPC_EAN_EXTENSION);

        // 可能的编码格式
        ALL_HINT_MAP.put(DecodeHintType.POSSIBLE_FORMATS, allFormatList);
        // 花更多的时间用于寻找图上的编码，优化准确性，但不优化速度
//        ALL_HINT_MAP.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
        // 复杂模式，开启 PURE_BARCODE 模式（带图片 LOGO 的解码方案）
//        ALL_HINT_MAP.put(DecodeHintType.PURE_BARCODE, Boolean.TRUE);
        // 编码字符集
//        ALL_HINT_MAP.put(DecodeHintType.CHARACTER_SET, "utf-8");
    }

    static final Map<DecodeHintType, Object> ONE_DIMENSION_HINT_MAP = new EnumMap<>(DecodeHintType.class);

    static {
        List<BarcodeFormat> oneDimenFormatList = new ArrayList<>();
        oneDimenFormatList.add(BarcodeFormat.CODABAR);
        oneDimenFormatList.add(BarcodeFormat.CODE_39);
        oneDimenFormatList.add(BarcodeFormat.CODE_93);
        oneDimenFormatList.add(BarcodeFormat.CODE_128);
        oneDimenFormatList.add(BarcodeFormat.EAN_8);
        oneDimenFormatList.add(BarcodeFormat.EAN_13);
        oneDimenFormatList.add(BarcodeFormat.ITF);
        oneDimenFormatList.add(BarcodeFormat.PDF_417);
        oneDimenFormatList.add(BarcodeFormat.RSS_14);
        oneDimenFormatList.add(BarcodeFormat.RSS_EXPANDED);
        oneDimenFormatList.add(BarcodeFormat.UPC_A);
        oneDimenFormatList.add(BarcodeFormat.UPC_E);
        oneDimenFormatList.add(BarcodeFormat.UPC_EAN_EXTENSION);

        ONE_DIMENSION_HINT_MAP.put(DecodeHintType.POSSIBLE_FORMATS, oneDimenFormatList);
        ONE_DIMENSION_HINT_MAP.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
        ONE_DIMENSION_HINT_MAP.put(DecodeHintType.CHARACTER_SET, "utf-8");
    }

    static final Map<DecodeHintType, Object> TWO_DIMENSION_HINT_MAP = new EnumMap<>(DecodeHintType.class);

    static {
        List<BarcodeFormat> twoDimenFormatList = new ArrayList<>();
        twoDimenFormatList.add(BarcodeFormat.AZTEC);
        twoDimenFormatList.add(BarcodeFormat.DATA_MATRIX);
        twoDimenFormatList.add(BarcodeFormat.MAXICODE);
        twoDimenFormatList.add(BarcodeFormat.QR_CODE);

        TWO_DIMENSION_HINT_MAP.put(DecodeHintType.POSSIBLE_FORMATS, twoDimenFormatList);
        TWO_DIMENSION_HINT_MAP.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
        TWO_DIMENSION_HINT_MAP.put(DecodeHintType.CHARACTER_SET, "utf-8");
    }

    static final Map<DecodeHintType, Object> QR_CODE_HINT_MAP = new EnumMap<>(DecodeHintType.class);

    static {
        QR_CODE_HINT_MAP.put(DecodeHintType.POSSIBLE_FORMATS, Collections.singletonList(BarcodeFormat.QR_CODE));
        QR_CODE_HINT_MAP.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
        QR_CODE_HINT_MAP.put(DecodeHintType.CHARACTER_SET, "utf-8");
    }

    static final Map<DecodeHintType, Object> CODE_128_HINT_MAP = new EnumMap<>(DecodeHintType.class);

    static {
        CODE_128_HINT_MAP.put(DecodeHintType.POSSIBLE_FORMATS, Collections.singletonList(BarcodeFormat.CODE_128));
        CODE_128_HINT_MAP.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
        CODE_128_HINT_MAP.put(DecodeHintType.CHARACTER_SET, "utf-8");
    }

    static final Map<DecodeHintType, Object> EAN_13_HINT_MAP = new EnumMap<>(DecodeHintType.class);

    static {
        EAN_13_HINT_MAP.put(DecodeHintType.POSSIBLE_FORMATS, Collections.singletonList(BarcodeFormat.EAN_13));
        EAN_13_HINT_MAP.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
        EAN_13_HINT_MAP.put(DecodeHintType.CHARACTER_SET, "utf-8");
    }

    static final Map<DecodeHintType, Object> HIGH_FREQUENCY_HINT_MAP = new EnumMap<>(DecodeHintType.class);

    static {
        List<BarcodeFormat> highFrequencyFormatList = new ArrayList<>();
        highFrequencyFormatList.add(BarcodeFormat.QR_CODE);
        highFrequencyFormatList.add(BarcodeFormat.AZTEC);
        highFrequencyFormatList.add(BarcodeFormat.CODABAR);
//        highFrequencyFormatList.add(BarcodeFormat.UPC_A);
//        highFrequencyFormatList.add(BarcodeFormat.EAN_13);
        highFrequencyFormatList.add(BarcodeFormat.CODE_128);

        HIGH_FREQUENCY_HINT_MAP.put(DecodeHintType.POSSIBLE_FORMATS, highFrequencyFormatList);
        HIGH_FREQUENCY_HINT_MAP.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
        HIGH_FREQUENCY_HINT_MAP.put(DecodeHintType.CHARACTER_SET, "utf-8");
    }

    private QRCodeDecoder() {
    }

    /**
     * 同步解析本地图片二维码。该方法是耗时操作，请在子线程中调用。
     *
     * @param picturePath 要解析的二维码图片本地路径
     * @return 返回二维码图片里的内容 或 null
     */
    public static String syncDecodeQRCode(String picturePath) {
        if (!new File(picturePath).exists()) {
            return null;
        }
        return syncDecodeQRCode(BGAQRCodeUtil.getDecodeAbleBitmap(picturePath));
    }

    /**
     * 同步解析bitmap二维码。该方法是耗时操作，请在子线程中调用。
     *
     * @param bitmap 要解析的二维码图片
     * @return 返回二维码图片里的内容 或 null
     */

    public static String syncDecodeQRCode(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        BGAQRCodeUtil.d("开始识别图片");
        MultiFormatReader multiFormatReader = new MultiFormatReader();
        multiFormatReader.setHints(ALL_HINT_MAP);

        Result result = null;

        try {
            result = multiFormatReader.decodeWithState(new BinaryBitmap(new HybridBinarizer(new BitmapLuminanceSource(bitmap))));
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        byte[] data = getYUV420sp(w, h, bitmap);
        PlanarYUVLuminanceSource source = new PlanarYUVLuminanceSource(data, w, h, 0, 0, w, h, false);
        try {
            result = multiFormatReader.decodeWithState(new BinaryBitmap(new HybridBinarizer(source)));
        } catch (Exception e) {
            BGAQRCodeUtil.d("HybridBinarizer失败");
            try {
                result = multiFormatReader.decodeWithState(new BinaryBitmap(new GlobalHistogramBinarizer(source)));
            } catch (NotFoundException e1) {
                BGAQRCodeUtil.d("GlobalHistogramBinarizer失败");
                e1.printStackTrace();
            }
        } finally {
            multiFormatReader.reset();
        }*/
        BGAQRCodeUtil.d("识别结果: " + result);
        if (result != null) {
            return result.getText();
        }
        return null;
    }

    private static byte[] yuvs;

    public static byte[] getYUV420sp(int inputWidth, int inputHeight, Bitmap scaled) {
        int[] argb = new int[inputWidth * inputHeight];
        scaled.getPixels(argb, 0, inputWidth, 0, 0, inputWidth, inputHeight);
        /**
         * 需要转换成偶数的像素点，否则编码YUV420的时候有可能导致分配的空间大小不够而溢出。
         */
        int requiredWidth = inputWidth % 2 == 0 ? inputWidth : inputWidth + 1;
        int requiredHeight = inputHeight % 2 == 0 ? inputHeight : inputHeight + 1;
        int byteLength = requiredWidth * requiredHeight * 3 / 2;
        if (yuvs == null || yuvs.length < byteLength) {
            yuvs = new byte[byteLength];
        } else {
            Arrays.fill(yuvs, (byte) 0);
        }
        encodeYUV420SP(yuvs, argb, inputWidth, inputHeight);
        scaled.recycle();
        return yuvs;
    }

    /**
     * RGB转YUV420sp
     *
     * @param yuv420sp inputWidth * inputHeight * 3 / 2
     * @param argb     inputWidth * inputHeight
     * @param width    image width
     * @param height   image height
     */
    private static void encodeYUV420SP(byte[] yuv420sp, int[] argb, int width, int height) {
        // 帧图片的像素大小
        final int frameSize = width * height;
        // ---YUV数据---
        int Y, U, V;
        // Y的index从0开始
        int yIndex = 0;
        // UV的index从frameSize开始
        int uvIndex = frameSize;
        // ---颜色数据---
        int R, G, B;
        int rgbIndex = 0;
        // ---循环所有像素点，RGB转YUV---
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                R = (argb[rgbIndex] & 0xff0000) >> 16;
                G = (argb[rgbIndex] & 0xff00) >> 8;
                B = (argb[rgbIndex] & 0xff);
                rgbIndex++;
                Y = ((66 * R + 129 * G + 25 * B + 128) >> 8) + 16;
                U = ((-38 * R - 74 * G + 112 * B + 128) >> 8) + 128;
                V = ((112 * R - 94 * G - 18 * B + 128) >> 8) + 128;
                Y = Math.max(0, Math.min(Y, 255));
                U = Math.max(0, Math.min(U, 255));
                V = Math.max(0, Math.min(V, 255));
                yuv420sp[yIndex++] = (byte) Y;
                if ((j % 2 == 0) && (i % 2 == 0)) {
                    yuv420sp[uvIndex++] = (byte) V;
                    yuv420sp[uvIndex++] = (byte) U;
                }
            }
        }
    }
}
package com.xing.weight.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.Toast;

import com.xing.weight.BuildConfig;
import com.xing.weight.CPApp;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;


public class Tools {

    private static final String TAG = "xing";

    public static void logd(String str) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, str);
        }
    }

    public static void loge(String str) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, str);
        }
    }

    private static Toast toast;

    /**
     * 显示与隐藏toast
     *
     * @param context
     * @param msg
     */
    public static void toastShow(Context context, String msg) {
        if (toast == null) {
            toast = Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
        }
        toast.setText(msg);
        toast.show();
    }

    /**
     * 显示与隐藏toast
     *
     * @param context
     * @param id
     */
    public static void toastShow(Context context, int id) {
        if (toast == null) {
            toast = Toast.makeText(context.getApplicationContext(), id, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
        }
        toast.setText(id);
        toast.show();
    }

    /**
     * 验证手机号码
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobile(String mobiles) {
        Pattern p = compile("^[1]\\d{10}$");
        // Pattern p = Pattern
        // .compile("^(1[3|5|8|4|7]\\d{9})$");
        Pattern p1 = compile("^((\\+86)|(86))?(13)\\d{9}$");

        Matcher m = p.matcher(mobiles);
        if (m.matches())
            return true;
        else {
            m = p1.matcher(mobiles);
            return m.matches();
        }
    }

    /**
     * 是不是密码 (长度8-16位，必须包含数字和字母的组合)
     */
    public static boolean isPassword(String password) {
        return Pattern.matches("^(?![0-9]+$)(?![a-zA-Z]+$)(?![^A-Za-z0-9]+$)[0-9A-Za-z|[^A-Za-z0-9]]{6,16}$", password);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int dpToPx(Resources res, int dp) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, res.getDisplayMetrics()) + 0.5f);
    }

    public static Rect getTextRect(String text, Paint p) {
        Rect rect = new Rect(0, 0, (int) getTextWidth(text, p), (int) getTextHeight(p));
        return rect;
    }

    public static float getTextHeight(Paint p) {
        Paint.FontMetrics fontMetrics = p.getFontMetrics();
        return fontMetrics.descent + Math.abs(fontMetrics.ascent);
    }

    public static float getTextWidth(String text, Paint p) {
        return p.measureText(text);
    }

    public static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    public static DisplayMetrics getDisplayMetrics(Context context) {
        DisplayMetrics outMetrics = context.getResources().getDisplayMetrics();
        return outMetrics;
    }

    public static long getVerCode(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                return packInfo.getLongVersionCode();
            } else {
                return packInfo.versionCode;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static float calcTemp(double temp) {
        return BigDecimal.valueOf(temp).divide(BigDecimal.valueOf(10)).setScale(1, BigDecimal.ROUND_DOWN).floatValue();
    }

    public static String parseData(long date) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            return simpleDateFormat.format(new Date(date));
        } catch (Exception e) {
            return "";
        }
    }


    public static int parseInt(String number) {
        try {
            int value = Integer.parseInt(number);
            return value;
        } catch (Exception e) {

        }
        return 0;
    }


    public static long getOneDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 1 * 24 * 60 * 60);
        return 24 * 60 * 60 * 1000;
    }


    /**
     * @param date
     * @return
     */
    public static String getDateFromMillSeconds(long date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        return sdf.format(new Date(date));
    }

    public static String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    public static long stringToDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            return sdf.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return System.currentTimeMillis();
        }
    }


    public static String getSDCardPath() {
        if (Build.VERSION.SDK_INT >= 29) { //10.0
            File externalFilesDir = CPApp.getInstance().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
            if (externalFilesDir != null) {
                return externalFilesDir.getAbsolutePath();
            } else {
                return getSavePath();
            }
        } else {
            return getSavePath();
        }
    }

    private static String getSavePath() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().getPath();
        } else {
            return CPApp.getInstance().getFilesDir().getPath();
        }
    }

    public static List<byte[]> fen(byte[] data, int length) {
        ArrayList data2 = new ArrayList();
        int num = data.length / length;
        int num2 = data.length % length;
        if (data.length < length) {
            data2.add(data);
            return data2;
        } else {
            for (int buffer2 = 0; buffer2 < num; ++buffer2) {
                byte[] k = new byte[length];

                for (int j = 0; j < length; ++j) {
                    k[j] = data[j + buffer2 * length];
                }

                data2.add(k);
            }

            if (num2 != 0) {
                byte[] var8 = new byte[num2];

                for (int var9 = 0; var9 < num2; ++var9) {
                    var8[var9] = data[var9 + num * length];
                }

                data2.add(var8);
            }

            return data2;
        }
    }

    public static boolean stringIsMac(String val) {
        if(TextUtils.isEmpty(val)){
            return false;
        }
        String trueMacAddress = "([A-Fa-f0-9]{2}:){5}[A-Fa-f0-9]{2}";
        if (val.matches(trueMacAddress)) {
            return true;
        } else {
            return false;
        }
    }

    public static int byte2ToInt(byte[] b, int offset) {
        return (b[offset + 1] & 0xFF) << 8 | b[offset] & 0xFF;
    }

    public static boolean validity(byte[] b) {
        return (b[2] ^ b[3]) == b[4];
    }

    public static double stringToDouble2(String amount) {
        if (TextUtils.isEmpty(amount) || amount.equals(".")) {
            return 0.00;
        }
        Double d = Double.parseDouble(amount);
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
        return Double.parseDouble(decimalFormat.format(d));
    }


    public static double stringToDouble3(String amount) {
        if (TextUtils.isEmpty(amount) || amount.equals(".")) {
            return 0.000;
        }
        Double d = Double.parseDouble(amount);
        DecimalFormat decimalFormat = new DecimalFormat("0.000");
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
        return Double.parseDouble(decimalFormat.format(d));
    }

    public static String doubleToString2(double amount) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
        return decimalFormat.format(amount);
    }

    public static String doubleToString3(double amount) {
        DecimalFormat decimalFormat = new DecimalFormat("0.000");
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
        return decimalFormat.format(amount);
    }

    public static String doubleToInt(double amount) {
        DecimalFormat df = new DecimalFormat("0");
        return df.format(amount);
    }

    public static String calcAmount(String number, String price) {
        if (TextUtils.isEmpty(number) || TextUtils.isEmpty(price)) {
            return "0.00";
        }
        BigDecimal numberDecimal = new BigDecimal(number);
        BigDecimal priceDecimal = new BigDecimal(price);
        return numberDecimal.multiply(priceDecimal).setScale(2, RoundingMode.HALF_UP).toString();
    }

    public static String calcNumber(String amount, String price) {
        if (TextUtils.isEmpty(amount) || TextUtils.isEmpty(price)) {
            return "0.000";
        }
        BigDecimal amountDecimal = new BigDecimal(amount);
        BigDecimal priceDecimal = new BigDecimal(price);
        return amountDecimal.divide(priceDecimal, 3, RoundingMode.HALF_UP).toString();
    }

    public static String calcTotalAmount(List<String> amounts) {
        if (amounts == null || amounts.isEmpty()) {
            return "0.00";
        }
        BigDecimal amountDecimal = new BigDecimal(0);
        for (String amount : amounts) {
            amountDecimal = amountDecimal.add(new BigDecimal(amount));
        }
        return amountDecimal.setScale(2, RoundingMode.HALF_UP).toString();
    }

}

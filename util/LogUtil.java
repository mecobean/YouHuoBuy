package cn.bjsxt.youhuo.util;
import android.util.Log;

/**
 * log工具类
 */
public class LogUtil {
    public static final String DEBUG_TAG = "DEBUG_TAG";
    public static final String INFO_TAG = "INFO_TAG";
    private static boolean isLog = true;

    /**
     * log.v 详细
     */
    public static void logV(String TAG, String message) {
        if (isLog)
            Log.v(TAG, message);
    }

    /**
     * log.d 调式
     */
    public static void logD(String TAG, String message) {
        if (isLog)
            Log.d(TAG, message);
    }

    /**
     * log.i 通告
     */
    public static void logI(String TAG, String message) {
        if (isLog)
            Log.i(TAG, message);
    }

    /**
     * log.w 警告
     */
    public static void logW(String TAG, String message) {
        if (isLog)
            Log.w(TAG, message);
    }

    /**
     * log.e 错误
     */
    public static void logE(String TAG, String message) {
        if (isLog)
            Log.e(TAG, message);
    }

}

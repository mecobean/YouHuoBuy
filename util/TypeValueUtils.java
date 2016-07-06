package cn.bjsxt.youhuo.util;

import android.util.TypedValue;

import cn.bjsxt.youhuo.MyApplication;

/**
 * dimen 转换工具类
 */
public class TypeValueUtils {
    public static int dp2px (int dp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp, MyApplication.getInstance().getResources().getDisplayMetrics());
    }
}

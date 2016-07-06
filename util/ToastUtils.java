package cn.bjsxt.youhuo.util;

import android.widget.Toast;

import cn.bjsxt.youhuo.MyApplication;

/**
 * Toast工具类
 */
public class ToastUtils {

    private static Toast toast;

    /**
     * 单例模式  防止对此单击重复弹出toast
     * @param s 要弹出的内容
     */
    public static void toast(String s) {
        if (toast == null) {
        toast = Toast.makeText(MyApplication.getInstance(), s, Toast.LENGTH_SHORT);
        }else {
            toast.setText(s);
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        toast.show();
    }
}

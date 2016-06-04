package cn.bjsxt.youhuo.util;

import android.widget.Toast;

import cn.bjsxt.youhuo.MyApplication;

/**
 * Created by Mecono on 2016/6/3.
 */
public class ToastUtils {
    public static void toast(String s){
        Toast.makeText(MyApplication.getInstance(),s,Toast.LENGTH_SHORT).show();
    }
}

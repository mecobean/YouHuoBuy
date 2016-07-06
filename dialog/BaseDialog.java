package cn.bjsxt.youhuo.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

/**
 * dialog的基类
 * 1.拿到activity （dialog必须传activity 否则出错）
 * 2.拿到布局填充器
 * 3.拿到window
 */
public class BaseDialog extends Dialog {
    public Activity activity;
    public LayoutInflater inflater;
    public Window window;
    public View decorView;

    public BaseDialog(Context context) {
        super(context);
        activity = (Activity) context;
        inflater = LayoutInflater.from(context);
        window = getWindow();
        //去标题
        window.requestFeature(Window.FEATURE_NO_TITLE);
        //触摸dialog外边dismiss dialog
        setCanceledOnTouchOutside(true);
        //拿到decorView
        decorView = window.getDecorView();

    }

    /**
     * 设置dialog显示的位置
     *
     * @param gravity gravity 相对属性
     */
    public void setGravity(int gravity) {
        window.setGravity(gravity);
    }

    /**
     * 设置dialog的动画
     *
     * @param resId style中的动画
     */
    public void setAnimation(int resId) {
        window.setWindowAnimations(resId);
    }

    /**
     * 填充布局 设置dialog的宽高
     *
     * @param view   布局
     * @param width  dialog宽
     * @param height dialog高
     */
    public void setContentView(View view, int width, int height) {
        super.setContentView(view);
        window.setLayout(width, height);
    }
}

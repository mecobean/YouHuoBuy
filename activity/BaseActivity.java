package cn.bjsxt.youhuo.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import cn.bjsxt.youhuo.R;

/**
 *
 */
public  class BaseActivity extends FragmentActivity {
    /**
     * 子activity的根布局
     */
    private RelativeLayout childActivity;
    /**
     * 填充状态栏的布局
     */
    private View statusBarGroup;
    private LayoutInflater inflater;
    /**
     * 保存子activity的布局
     */
    public View childView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_base);

        initViews();
        //拿到布局填充器
        inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        //设置沉浸式状态栏
        initStatusBar();
    }

    /**
     * 初始化沉浸式状态栏
     */
    private void initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && getStatusBarHeight() > 0) {
            int statusBarHeight = getStatusBarHeight();
            //设置状态栏背景透明
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, statusBarHeight);
            statusBarGroup.setLayoutParams(params);
        }
    }

    /**
     * 拿到系统状态栏的高度
     *
     * @return 如果拿到返回这个高度，否则返回0
     */
    public int getStatusBarHeight() {
        int identifier = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (identifier > 0) {
            return getResources().getDimensionPixelSize(identifier);
        }
        return 0;
    }

    /**
     * 初始化控件
     */
    private void initViews() {
        childActivity = (RelativeLayout) findViewById(R.id.childActivity);
        statusBarGroup = findViewById(R.id.statusBarGroup_base);
    }

    /**
     * 填充字布局与系统状态栏的颜色
     * @param childID 子布局的id
     * @param color 要填充的颜色
     */
    public void inflaterView(int childID, int color) {
        statusBarGroup.setBackgroundColor(color);
        childView = inflater.inflate(childID, null);
        childActivity.addView(childView);
    }

    /**
     * 重载方法 如没有颜色，就设置默认颜色
     * @param childID 自布局的id
     */
    public void inflaterView(int childID) {
        inflaterView(childID,getResources().getColor(R.color.toolBar_bg));
    }
}

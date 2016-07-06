package cn.bjsxt.youhuo.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import cn.bjsxt.youhuo.MyApplication;

/**
 * BaseFragment --> 抽象类
 * 1.拿到context 与全局application
 * 2.重写哦你CreateView方法 填充布局
 * 3.在
 */
public class BaseFragment extends Fragment {
    private MyApplication application;
    public Context context;
    public Activity activity;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        activity = (Activity)context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = MyApplication.getInstance();
    }





    @Override
    public void onDestroy() {
        super.onDestroy();
//        saveData();
    }

    /**
     * 保存数据
     */
//    private void saveData() {
//
//    }
}

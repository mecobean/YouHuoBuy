package cn.bjsxt.youhuo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import cn.bjsxt.youhuo.MyApplication;

/**
 * BaseFragment --> 抽象类
 * 1.拿到context 与全局application
 * 2.重写哦你CreateView方法 填充布局
 * 3.在
 */
public abstract class GirlsBaseFragment extends Fragment {
    public Context context;
    public View rootView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView ==null){
            rootView = initView(inflater, container);
            initDatas();
        }else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null){
                parent.removeView(rootView);
            }
        }
        return rootView;
    }

    protected abstract void initDatas();

    public abstract View initView(LayoutInflater inflater, ViewGroup container) ;
}

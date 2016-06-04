package cn.bjsxt.youhuo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.bjsxt.youhuo.R;

/**
 * Created by Mecono on 2016/6/3.
 */
public class MainHomeFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainHomeRootView = inflater.inflate(R.layout.fragment_main_home, null);
        return mainHomeRootView;
    }
}

package cn.bjsxt.youhuo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import cn.bjsxt.youhuo.R;

/**
 * Created by Mecono on 2016/6/3.
 */
public class MainCategoryFragment extends BaseFragment {

    private View rootView;
    private ImageButton menu;
    private TabLayout tab;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main_categroy, null);
        initDatas();
        return rootView;
    }

    private void initDatas() {
        initViews();
        initTabLayout();
    }

    private void initTabLayout() {
        TabLayout.Tab tab1 = tab.newTab();
        tab1.setText("品类");
        TabLayout.Tab tab2 =tab.newTab();
        tab2.setText("品牌");
        TabLayout.Tab tab3 = tab.newTab();
        tab3.setText("关注");
        tab.addTab(tab1,true);
        tab.addTab(tab2);
        tab.addTab(tab3);
    }

    private void initViews() {
        menu = (ImageButton) rootView.findViewById(R.id.toolbar_main_category_menu);
        tab = (TabLayout) rootView.findViewById(R.id.toolbar_main_category_tab);
    }
}

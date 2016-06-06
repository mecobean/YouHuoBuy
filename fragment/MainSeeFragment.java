package cn.bjsxt.youhuo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import cn.bjsxt.youhuo.R;

/**
 * Created by Mecono on 2016/6/3.
 */
public class MainSeeFragment extends BaseFragment implements TabLayout.OnTabSelectedListener {

    private View rootView;
    private TabLayout tab;
    private ImageButton menu;
    private ImageButton like;
    private ImageButton photo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main_see, null);
        initDatas();
        return rootView;
    }

    /**
     * 初始化数据
     */
    private void initDatas() {
        initViews();
        initTabLayout();
        initListener();
    }

    /**
     * 初始化监听
     */
    private void initListener() {
        tab.setOnTabSelectedListener(this);
    }

    /**
     * 初始化TabLayout
     */
    private void initTabLayout() {
        TabLayout.Tab tab1 = tab.newTab();
        tab1.setText("逛");
        TabLayout.Tab tab2 = tab.newTab();
        tab2.setText("ShOw");
        tab.addTab(tab1, true);
        tab.addTab(tab2);
    }

    /**
     * 初始化View
     */
    private void initViews() {
        menu = (ImageButton) rootView.findViewById(R.id.toolbar_main_see_menu);
        tab = (TabLayout) rootView.findViewById(R.id.toolbar_main_see_tab);
        like = (ImageButton) rootView.findViewById(R.id.toolbar_main_see_like);
        photo = (ImageButton) rootView.findViewById(R.id.toolbar_main_see_photo);
    }

    /**
     * tabLayout 选中时调用
     * 控制toolBar右边的button显示隐藏
     * @param tab
     */
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if ("逛".equals(tab.getText())){
            //显示like按钮隐藏photo按钮
            like.setVisibility(View.VISIBLE);
            photo.setVisibility(View.GONE);
        }else if ("ShOw".equals(tab.getText())){
            //显示photo按钮隐藏like按钮
            like.setVisibility(View.GONE);
            photo.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}

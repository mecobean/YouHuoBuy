package cn.bjsxt.youhuo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.orhanobut.logger.Logger;

import cn.bjsxt.youhuo.R;
import cn.bjsxt.youhuo.view.CategoryGuanZhuView;
import cn.bjsxt.youhuo.view.CategoryPinLeiView;
import cn.bjsxt.youhuo.view.CategoryPinPaiView;

/**
 * Created by Mecono on 2016/6/3.
 */
public class MainCategoryFragment extends BaseFragment implements TabLayout.OnTabSelectedListener {
    /**
     * categoryFragment的根布局
     */
    private View rootView;
    private ImageButton slidingMenu;
    /**
     * 顶部导航栏的三个选项卡
     */
    private TabLayout tab;
    /**
     * 分类选项的子节点的根布局
     */
    private LinearLayout mLinear;
    /**
     * 品类选项卡
     */
    private CategoryPinLeiView pinLei;
    private CategoryPinPaiView pinPai;
    private CategoryGuanZhuView guanZhu;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main_categroy, null);
        initDatas();
        return rootView;
    }

    private void initDatas() {
        initViews();
        initCategoryPinLei();
        initTabLayout();
        initListener();
    }

    /**
     * 初始化CategoryChild
     * 默认显示品类页面
     */
    private void initCategoryPinLei() {
        mLinear.removeAllViews();
        if (pinLei == null) {
            pinLei = new CategoryPinLeiView(context);
        }
        mLinear.addView(pinLei.getCategoryChild());
    }

    private void initListener() {
        tab.setOnTabSelectedListener(this);
    }

    /**
     * 初始化toolbar的选项卡
     */
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
        slidingMenu = (ImageButton) rootView.findViewById(R.id.toolbar_main_category_menu);
        tab = (TabLayout) rootView.findViewById(R.id.toolbar_main_category_tab);
        mLinear = (LinearLayout) rootView.findViewById(R.id.categoryChild);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        switch (tab.getText().toString()){
            case "品类":
                initCategoryPinLei();
                break;
            case "品牌":
                initCategoryPinPai();
                break;
            case "关注":
                initCategoryGuanZhu();
                break;
        }
    }
    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    /**
     * 初始化关注选项
     */
    private void initCategoryGuanZhu() {
        mLinear.removeAllViews();
        Logger.e("关注");
        if (guanZhu == null) {
            guanZhu = new CategoryGuanZhuView(context);
        }
        mLinear.addView(guanZhu.getGuanZhuView());
    }
    /**
     * 初始化品牌选项
     */
    private void initCategoryPinPai() {
        mLinear.removeAllViews();
        if (pinPai == null) {
            pinPai = new CategoryPinPaiView(context);
        }
        mLinear.addView(pinPai.getBrandView());
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(pinPai != null){
            pinPai.stopAdvertThread();
        }
    }
}

package cn.bjsxt.youhuo.activity;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cn.bjsxt.youhuo.R;
import cn.bjsxt.youhuo.adapter.MyGirlsFragmentAdapter;
import cn.bjsxt.youhuo.fragment.GirlsCartFragment;
import cn.bjsxt.youhuo.fragment.GirlsCategoryFragment;
import cn.bjsxt.youhuo.fragment.GirlsHomeFragment;
import cn.bjsxt.youhuo.fragment.GirlsMineFragment;
import cn.bjsxt.youhuo.fragment.GirlsSeeFragment;

/**
 *
 */
public class GirlsActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 底部导航栏
     */
    private View bottomBar;
    /**
     * 用于显示fragment的viewPager
     */
    private ViewPager fragmentGroup;
    /**
     * 分类选型未选中
     */
    private View categoryNormal;
    /**
     * 分类选型选中
     */
    private View categorySelect;
    /**
     * 逛选型未选中
     */
    private View seeNormal;
    /**
     * 逛选型选中
     */
    private View seeSelect;
    /**
     * 购物车选型未选中
     */
    private View cartNormal;
    /**
     * 购物车选型选中
     */
    private View cartSelect;
    /**
     * 我的选型未选中
     */
    private View mineNormal;
    /**
     * 我的选型选中
     */
    private View mineSelect;
    
    private GirlsHomeFragment homeFragment;
    private GirlsCategoryFragment categoryFragment;
    private GirlsSeeFragment seeFragment;
    private GirlsCartFragment cartFragment;
    private GirlsMineFragment mineFragment;

    private List<Fragment> fragmentList;
    private View homeNormal;
    private View homeSelect;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initGirlsViews();
        initBottomBar();
        initViewPager();
    }

    /**
     * 初始化viewPager
     */
    private void initViewPager() {
        initFragment();
        MyGirlsFragmentAdapter girlsFragmentAdapter = new MyGirlsFragmentAdapter(getSupportFragmentManager(), fragmentList);
        fragmentGroup.setAdapter(girlsFragmentAdapter);
        fragmentGroup.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                switch (position) {
                    case 0:
                        homeSelect.setAlpha(1 - positionOffset);
                        homeNormal.setAlpha(positionOffset);

                        categoryNormal.setAlpha(1 - positionOffset);
                        categorySelect.setAlpha(positionOffset);
                        break;
                    case 1:
                        categoryNormal.setAlpha(positionOffset);
                        categorySelect.setAlpha(1 - positionOffset);

                        seeNormal.setAlpha(1 - positionOffset);
                        seeSelect.setAlpha(positionOffset);
                        break;
                    case 2:
                        seeNormal.setAlpha(positionOffset);
                        seeSelect.setAlpha(1 - positionOffset);

                        cartNormal.setAlpha(1 - positionOffset);
                        cartSelect.setAlpha(positionOffset);
                        break;
                    case 3:
                        cartNormal.setAlpha(positionOffset);
                        cartSelect.setAlpha(1 - positionOffset);

                        mineNormal.setAlpha(1 - positionOffset);
                        mineSelect.setAlpha(positionOffset);
                        break;
                }
            }

            @Override
            public void onPageSelected(int position) {
                setBottomBarPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 设置底部导航栏的选中状态
     *
     * @param position viewPager选中的position
     */
    private void setBottomBarPosition(int position) {
        switch (position) {
            case 0:
                homeNormal.setAlpha(0);
                homeSelect.setAlpha(1);

                categoryNormal.setAlpha(1);
                categorySelect.setAlpha(0);

                seeNormal.setAlpha(1);
                seeSelect.setAlpha(0);

                cartNormal.setAlpha(1);
                cartSelect.setAlpha(0);

                mineNormal.setAlpha(1);
                mineSelect.setAlpha(0);
                break;
            case 1:
                homeNormal.setAlpha(1);
                homeSelect.setAlpha(0);

                categoryNormal.setAlpha(0);
                categorySelect.setAlpha(1);

                seeNormal.setAlpha(1);
                seeSelect.setAlpha(0);

                cartNormal.setAlpha(1);
                cartSelect.setAlpha(0);

                mineNormal.setAlpha(1);
                mineSelect.setAlpha(0);
                break;
            case 2:
                homeNormal.setAlpha(1);
                homeSelect.setAlpha(0);

                categoryNormal.setAlpha(1);
                categorySelect.setAlpha(0);

                seeNormal.setAlpha(0);
                seeSelect.setAlpha(1);

                cartNormal.setAlpha(1);
                cartSelect.setAlpha(0);

                mineNormal.setAlpha(1);
                mineSelect.setAlpha(0);
                break;
            case 3:
                homeNormal.setAlpha(1);
                homeSelect.setAlpha(0);

                categoryNormal.setAlpha(1);
                categorySelect.setAlpha(0);

                seeNormal.setAlpha(1);
                seeSelect.setAlpha(0);

                cartNormal.setAlpha(0);
                cartSelect.setAlpha(1);

                mineNormal.setAlpha(1);
                mineSelect.setAlpha(0);
                break;
            case 4:
                homeNormal.setAlpha(1);
                homeSelect.setAlpha(0);

                categoryNormal.setAlpha(1);
                categorySelect.setAlpha(0);

                seeNormal.setAlpha(1);
                seeSelect.setAlpha(0);

                cartNormal.setAlpha(1);
                cartSelect.setAlpha(0);

                mineNormal.setAlpha(0);
                mineSelect.setAlpha(1);
                break;

        }
    }


    /**
     * 初始化fragment
     */
    private void initFragment() {
        fragmentList = new ArrayList<>();
        if (homeFragment == null) {
            homeFragment = new GirlsHomeFragment();
            fragmentList.add(homeFragment);
        }
        if (categoryFragment == null) {
            categoryFragment = new GirlsCategoryFragment();
            fragmentList.add(categoryFragment);
        }
        if (seeFragment == null) {
            seeFragment = new GirlsSeeFragment();
            fragmentList.add(seeFragment);
        }
        if (cartFragment == null) {
            cartFragment = new GirlsCartFragment();
            fragmentList.add(cartFragment);
        }
        if (mineFragment == null) {
            mineFragment = new GirlsMineFragment();
            fragmentList.add(mineFragment);
        }

    }

    /**
     * 初始化底部导航栏
     */
    private void initBottomBar() {
        //主页选项卡 默认选中
        homeNormal = bottomBar.findViewById(R.id.girl_bottom_home_norma);
        homeSelect = bottomBar.findViewById(R.id.girl_bottom_home_select);
        homeNormal.setOnClickListener(this);
        homeSelect.setOnClickListener(this);
        homeNormal.setAlpha(0);

        //分类选型卡
        categoryNormal = bottomBar.findViewById(R.id.girl_bottom_category_norma);
        categorySelect = bottomBar.findViewById(R.id.girl_bottom_category_select);
        categoryNormal.setOnClickListener(this);
        categorySelect.setOnClickListener(this);
        categorySelect.setAlpha(0);

        //逛选项卡
        seeNormal = bottomBar.findViewById(R.id.girl_bottom_see_norma);
        seeSelect = bottomBar.findViewById(R.id.girl_bottom_see_select);
        seeNormal.setOnClickListener(this);
        seeSelect.setOnClickListener(this);
        seeSelect.setAlpha(0);

        //购物车选项卡
        cartNormal = bottomBar.findViewById(R.id.girl_bottom_cart_norma);
        cartSelect = bottomBar.findViewById(R.id.girl_bottom_cart_select);
        cartNormal.setOnClickListener(this);
        cartSelect.setOnClickListener(this);
        cartSelect.setAlpha(0);

        //我的选项卡
        mineNormal = bottomBar.findViewById(R.id.girl_bottom_mine_norma);
        mineSelect = bottomBar.findViewById(R.id.girl_bottom_mine_select);
        mineNormal.setOnClickListener(this);
        mineSelect.setOnClickListener(this);
        mineSelect.setAlpha(0);
    }

    /**
     * 初始化控件
     */
    private void initGirlsViews() {
        inflaterView(R.layout.activity_girls);
        bottomBar = childView.findViewById(R.id.girlsBottomBar);
        fragmentGroup = (ViewPager) childView.findViewById(R.id.girls_fragmentGroup);
    }


    /**
     * 返回上次页面添加activity转场动画
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_updown_quit_in, R.anim.activity_updown_enter_out);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //点击首页
            case R.id.girl_bottom_home_norma:
            case R.id.girl_bottom_home_select:
                fragmentGroup.setCurrentItem(0,false);
                break;

            case R.id.girl_bottom_category_norma:
            case R.id.girl_bottom_category_select:
                fragmentGroup.setCurrentItem(1,false);
                break;

            case R.id.girl_bottom_see_norma:
            case R.id.girl_bottom_see_select:
                fragmentGroup.setCurrentItem(2,false);
                break;

            case R.id.girl_bottom_cart_norma:
            case R.id.girl_bottom_cart_select:
                fragmentGroup.setCurrentItem(3,false);
                break;
            case R.id.girl_bottom_mine_norma:
            case R.id.girl_bottom_mine_select:
                fragmentGroup.setCurrentItem(4,false);
                break;
        }
    }
}

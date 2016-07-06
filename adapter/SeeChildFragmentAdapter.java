package cn.bjsxt.youhuo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Mecono on 2016/6/23.
 */
public class SeeChildFragmentAdapter extends BaseFragmentPagerAdapter {
    /**
     * 与viewPager item 向对应的tab标签
     */
    private  List<String> titles;

    public SeeChildFragmentAdapter(FragmentManager fm, List<Fragment> list, List<String> titles) {
        super(fm, list);
        this.titles = titles;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}

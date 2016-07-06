package cn.bjsxt.youhuo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import cn.bjsxt.youhuo.R;

/**
 *
 */
public class GirlsSeeFragment extends GirlsBaseFragment{


    @Override
    protected void initDatas() {

    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {
        View inflate = inflater.inflate(R.layout.fragment_girls_cart, null);
        TextView tv = (TextView) inflate.findViewById(R.id.girl_fragment);
        tv.setText("See");
        return inflate;
    }
}

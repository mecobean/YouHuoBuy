package cn.bjsxt.youhuo.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.bjsxt.youhuo.R;


/**
 * Created by Mecono on 2016/6/3.
 */
public class GirlsCategoryFragment extends GirlsBaseFragment  {


    @Override
    protected void initDatas() {

    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {
        View inflate = inflater.inflate(R.layout.fragment_girls_cart, null);
        TextView tv = (TextView) inflate.findViewById(R.id.girl_fragment);
        tv.setText("category");
        return inflate;
    }
}

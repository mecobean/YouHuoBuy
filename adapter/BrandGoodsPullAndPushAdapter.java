package cn.bjsxt.youhuo.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * 商品列表的listView adapter
 */
public class BrandGoodsPullAndPushAdapter extends BaseAdapter{
    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return (View) parent.getTag();
    }
}

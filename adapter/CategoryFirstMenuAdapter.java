package cn.bjsxt.youhuo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.bjsxt.youhuo.R;
import cn.bjsxt.youhuo.bean.CategoryFirstMenuBean;

/**
 * Created by Mecono on 2016/6/7.
 */
public class CategoryFirstMenuAdapter extends BaseViewAdapter<CategoryFirstMenuBean> {
    public CategoryFirstMenuAdapter(Context context, List<CategoryFirstMenuBean> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.category_first_menu_item, null);
            holder = new ViewHolder();
            holder.tv = (TextView) convertView.findViewById(R.id.category_first_item_tv);
            holder.iv = (ImageView) convertView.findViewById(R.id.category_first_item_iv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        CategoryFirstMenuBean item = getItem(position);
        holder.tv.setText(item.getName());
        if (item.getSelect()){
            holder.iv.setVisibility(View.VISIBLE);
        }else {
            holder.iv.setVisibility(View.GONE);
        }
        return convertView;
    }

    class ViewHolder {
        TextView tv;
        ImageView iv;
    }
}

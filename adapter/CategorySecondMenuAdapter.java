package cn.bjsxt.youhuo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.bjsxt.youhuo.R;
import cn.bjsxt.youhuo.bean.CategorySecondMenuBean;

/**
 * Created by Mecono on 2016/6/7.
 */
public class CategorySecondMenuAdapter extends BaseViewAdapter<CategorySecondMenuBean> {
    public CategorySecondMenuAdapter(Context context, List<CategorySecondMenuBean> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.category_second_menu_item, null);
            holder = new ViewHolder();
            holder.tv = (TextView) convertView.findViewById(R.id.category_second_menu_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        CategorySecondMenuBean item = getItem(position);
        holder.tv.setText(item.getName());
        return convertView;
    }

    class ViewHolder {
        TextView tv;
    }

}

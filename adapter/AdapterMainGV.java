package cn.bjsxt.youhuo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.bjsxt.youhuo.R;
import cn.bjsxt.youhuo.bean.MainHomeGVBean;

/**
 *
 */
public class AdapterMainGV extends BaseViewAdapter<MainHomeGVBean> {

    public AdapterMainGV(Context context, List<MainHomeGVBean> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MainGvHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_main_home_gv, null);
            holder = new MainGvHolder();
            holder.iv = (ImageView) convertView.findViewById(R.id.item_main_home_gv_iv);
            holder.tv = (TextView) convertView.findViewById(R.id.item_main_home_gv_tv);
            convertView.setTag(holder);
        } else {
            holder = (MainGvHolder) convertView.getTag();
        }
        MainHomeGVBean item = getItem(position);

        holder.iv.setImageResource(item.getImgID());
        holder.tv.setText(item.getStr());
        return convertView;
    }

    class MainGvHolder {
        ImageView iv;
        TextView tv;
    }
}

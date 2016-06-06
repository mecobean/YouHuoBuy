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
public class AdapterMainGV extends BaseViewAdapter<MainHomeGVBean>{
    private List<MainHomeGVBean> list;
    private Context context;

    public AdapterMainGV(Context context, List<MainHomeGVBean> list) {
        super(list);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = View.inflate(context, R.layout.item_main_home_gv,null);
        ImageView iv = (ImageView) convertView.findViewById(R.id.item_main_home_gv_iv);
        TextView tv = (TextView) convertView.findViewById(R.id.item_main_home_gv_tv);
        MainHomeGVBean mainHomeGVBean = list.get(position);
        iv.setImageResource(mainHomeGVBean.getImgID());
        tv.setText(mainHomeGVBean.getStr());
        return convertView;
    }
}

package cn.bjsxt.youhuo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import cn.bjsxt.youhuo.R;
import cn.bjsxt.youhuo.util.HttpModel;

/**
 * Created by Mecono on 2016/6/16.
 */
public class GoodsDetailListViewAdapter extends BaseViewAdapter<String>{


    public GoodsDetailListViewAdapter(Context context, List<String> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GoodsDetailHolder holder = null;
        if (convertView == null){
            convertView = View.inflate(context, R.layout.item_goods_detail_lv,null);
            holder = new GoodsDetailHolder();
            holder.img = (ImageView) convertView.findViewById(R.id.item_goods_detail_iv);
            convertView.setTag(holder);
        }else {
            holder = (GoodsDetailHolder) convertView.getTag();
        }
        Picasso.with(context).load(HttpModel.IMGURL+list.get(position)).placeholder(R.mipmap.share_loading).into(holder.img);
        return convertView;
    }
    class GoodsDetailHolder{
        ImageView img;
    }
}

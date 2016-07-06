package cn.bjsxt.youhuo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import cn.bjsxt.youhuo.R;
import cn.bjsxt.youhuo.bean.HomeLvItemBean;
import cn.bjsxt.youhuo.util.HttpModel;

/**
 * home 主页的listView 的adapter
 */
public class HomeLvAdapter extends BaseViewAdapter<HomeLvItemBean> {
    String[] titles = new String[]{"热门品牌", "men", "menpants", "accessories", "other"};

    public HomeLvAdapter(Context context, List<HomeLvItemBean> list) {
        super(context, list);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       HomeLvHolder holder = null;
        if (convertView == null){
            convertView = View.inflate(context, R.layout.itme_home_lv,null);
            holder = new HomeLvHolder();
            holder.title = (TextView) convertView.findViewById(R.id.item_home_titleTv);
            holder.firstIv = (ImageView) convertView.findViewById(R.id.item_home_firstIv);
            holder.secondIv = (ImageView) convertView.findViewById(R.id.item_home_secondIv);
            holder.thirdIv = (ImageView) convertView.findViewById(R.id.item_home_thirdIv);
            holder.fourIv = (ImageView) convertView.findViewById(R.id.item_home_fourIv);
            holder.fiveIv = (ImageView) convertView.findViewById(R.id.item_home_fiveIv);
            holder.sixIv = (ImageView) convertView.findViewById(R.id.item_home_sixIv);
            convertView.setTag(holder);
        }else {
            holder = (HomeLvHolder) convertView.getTag();
        }

        HomeLvItemBean item = getItem(position);
        holder.title.setText(titles[position]);

        Picasso.with(context).load(HttpModel.IMGURL+item.getFirstIvPath()).placeholder(R.mipmap.share_loading).into(holder.firstIv);
        Picasso.with(context).load(HttpModel.IMGURL+item.getSecondIvPath()).placeholder(R.mipmap.share_loading).into(holder.secondIv);
        Picasso.with(context).load(HttpModel.IMGURL+item.getThirdIvPath()).placeholder(R.mipmap.share_loading).into(holder.thirdIv);
        Picasso.with(context).load(HttpModel.IMGURL+item.getFourIvPath()).placeholder(R.mipmap.share_loading).into(holder.fourIv);
        Picasso.with(context).load(HttpModel.IMGURL+item.getFiveIvPath()).placeholder(R.mipmap.share_loading).into(holder.fiveIv);
        Picasso.with(context).load(HttpModel.IMGURL+item.getSixIvPath()).placeholder(R.mipmap.share_loading).into(holder.sixIv);
        return convertView;
    }

    static class HomeLvHolder {
        TextView title;
        ImageView firstIv, secondIv, thirdIv, fourIv, fiveIv, sixIv;
    }
}

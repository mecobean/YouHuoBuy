package cn.bjsxt.youhuo.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bjsxt.youhuo.R;
import cn.bjsxt.youhuo.activity.WebActivity;
import cn.bjsxt.youhuo.bean.SeeChildBean;
import cn.bjsxt.youhuo.util.HttpModel;
import cn.bjsxt.youhuo.util.ToastUtils;

/**
 * seeChild listView 的adapter
 */
public class SeeChildLvAdapter extends BaseViewAdapter<SeeChildBean.NewsBean> {

    public SeeChildLvAdapter(Context context, List<SeeChildBean.NewsBean> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_see_child, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final SeeChildBean.NewsBean item = getItem(position);
        Picasso.with(context).load(HttpModel.IMGURL + item.getImgpath()).placeholder(R.mipmap.share_loading).into(holder.itemSeeChildImg);
        holder.itemSeeChildTime.setText(item.getTime());
        holder.itemSeeChildTitleTv.setText(item.getTitle());
        holder.itemSeeChildValueTv.setText(item.getValue());
        holder.itemSeeChildUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WebActivity.class);
                intent.putExtra("URL", item.getUrl());
                context.startActivity(intent);
            }
        });
        holder.itemSeeChildImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(item);
            }
        });
        holder.itemSeeChildTitleTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(item);
            }
        });

        return convertView;
    }

    /**
     * show bottomSheetDialog
     *
     * @param item 数据源
     */
    private void showDialog(SeeChildBean.NewsBean item) {
        BottomSheetDialog dialog = new BottomSheetDialog(context);
        ViewGroup view = (ViewGroup) View.inflate(context, R.layout.item_see_child, null);
        TextView title = (TextView) view.findViewById(R.id.item_see_child_titleTv);
        title.setText(item.getTitle());
        TextView content = (TextView) view.findViewById(R.id.item_see_child_valueTv);
        content.setText(item.getValue());
        content.setVisibility(View.VISIBLE);
        TextView time = (TextView) view.findViewById(R.id.item_see_child_time);
        time.setText(item.getTime());
        ImageView img = (ImageView) view.findViewById(R.id.item_see_child_img);
        Picasso.with(context).load(HttpModel.IMGURL + item.getImgpath()).placeholder(R.mipmap.share_loading).into(img);
        View topLine = view.findViewById(R.id.item_see_child_topLine);
        topLine.setVisibility(View.GONE);
        dialog.setContentView(view);
        dialog.show();
    }

    static class ViewHolder {
        /**
         * 用户栏
         */
        @Bind(R.id.item_see_child_user)
        RelativeLayout itemSeeChildUser;
        /**
         * 图片
         */
        @Bind(R.id.item_see_child_img)
        ImageView itemSeeChildImg;
        @Bind(R.id.item_see_child_titleTv)
        TextView itemSeeChildTitleTv;
        @Bind(R.id.item_see_child_valueTv)
        TextView itemSeeChildValueTv;
        @Bind(R.id.item_see_child_time)
        TextView itemSeeChildTime;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

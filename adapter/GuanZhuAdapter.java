package cn.bjsxt.youhuo.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bjsxt.youhuo.R;
import cn.bjsxt.youhuo.activity.BrandGoodsActivity;
import cn.bjsxt.youhuo.activity.GoodsDetailActivity;
import cn.bjsxt.youhuo.bean.GuanZhuInfoBean;
import cn.bjsxt.youhuo.util.HttpModel;

/**
 * 关注的listView的adapter
 */
public class GuanZhuAdapter extends BaseViewAdapter<GuanZhuInfoBean.FollowBean> implements View.OnClickListener {


    public GuanZhuAdapter(Context context, List<GuanZhuInfoBean.FollowBean> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.itme_guanzhu, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        GuanZhuInfoBean.FollowBean item = getItem(position);
        holder.itemBrandInfo.setOnClickListener(this);
        holder.itemGuanZhuBrandName.setText(item.getBrandname());
        Picasso.with(context).load(HttpModel.IMGURL + item.getBrandimg()).placeholder(R.mipmap.share_loading).into(holder.itemGuanZhuBrandImg);

        holder.itemGuanZhuPriceTv1.setText("￥" + item.getGoods().get(0).getPrice());
        holder.itemGuanZhuPriceTv2.setText("￥" + item.getGoods().get(1).getPrice());
        holder.itemGuanZhuPriceTv3.setText("￥" + item.getGoods().get(2).getPrice());

        holder.itemGuanZhuDiscountTv1.setText(getSpannableString(item.getGoods().get(0).getPrice(), item.getGoods().get(0).getDistance()));
        holder.itemGuanZhuDiscountTv2.setText(getSpannableString(item.getGoods().get(1).getPrice(), item.getGoods().get(1).getDistance()));
        holder.itemGuanZhuDiscountTv3.setText(getSpannableString(item.getGoods().get(2).getPrice(), item.getGoods().get(2).getDistance()));

        Picasso.with(context).load(HttpModel.IMGURL + item.getGoods().get(0).getGoodsimg()).placeholder(R.mipmap.share_loading).into(holder.itemGuanZhuIv1);
        Picasso.with(context).load(HttpModel.IMGURL + item.getGoods().get(1).getGoodsimg()).placeholder(R.mipmap.share_loading).into(holder.itemGuanZhuIv2);
        Picasso.with(context).load(HttpModel.IMGURL + item.getGoods().get(2).getGoodsimg()).placeholder(R.mipmap.share_loading).into(holder.itemGuanZhuIv3);

        holder.itemGuanZhuIvGroup1.setOnClickListener(this);
        holder.itemGuanZhuIvGroup2.setOnClickListener(this);
        holder.itemGuanZhuIvGroup3.setOnClickListener(this);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.item_brandInfo://跳转商品列表
                intent.setClass(context, BrandGoodsActivity.class);
                intent.putExtra("brandId", 10);
                context.startActivity(intent);
                break;
            case R.id.item_guanZhu_IvGroup1://跳转商品详情
                startGoodsDetailActivity(intent, "关注item 第一个图片");
                break;
            case R.id.item_guanZhu_IvGroup2:
                startGoodsDetailActivity(intent, "关注item 第二个图片");
                break;
            case R.id.item_guanZhu_IvGroup3:
                startGoodsDetailActivity(intent, "关注item 第三个图片");
                break;

        }
    }

    /**
     * 启动商品详情activity
     * @param intent
     * @param msg
     */
    private void startGoodsDetailActivity(Intent intent, String msg) {
        intent.setClass(context, GoodsDetailActivity.class);
        intent.putExtra("goodsId", msg);
        context.startActivity(intent);
    }

    static class ViewHolder {
        @Bind(R.id.item_guanZhu_brandImg)
        ImageView itemGuanZhuBrandImg;
        @Bind(R.id.item_guanZhu_brandName)
        TextView itemGuanZhuBrandName;
        @Bind(R.id.item_brandInfo)
        RelativeLayout itemBrandInfo;
        @Bind(R.id.item_guanZhu_Iv1)
        ImageView itemGuanZhuIv1;
        @Bind(R.id.item_guanZhu_priceTv1)
        TextView itemGuanZhuPriceTv1;
        @Bind(R.id.item_guanZhu_discountTv1)
        TextView itemGuanZhuDiscountTv1;
        @Bind(R.id.item_guanZhu_Iv2)
        ImageView itemGuanZhuIv2;
        @Bind(R.id.item_guanZhu_priceTv2)
        TextView itemGuanZhuPriceTv2;
        @Bind(R.id.item_guanZhu_discountTv2)
        TextView itemGuanZhuDiscountTv2;
        @Bind(R.id.item_guanZhu_Iv3)
        ImageView itemGuanZhuIv3;
        @Bind(R.id.item_guanZhu_priceTv3)
        TextView itemGuanZhuPriceTv3;
        @Bind(R.id.item_guanZhu_discountTv3)
        TextView itemGuanZhuDiscountTv3;
        @Bind(R.id.item_guanZhu_IvGroup1)
        LinearLayout itemGuanZhuIvGroup1;
        @Bind(R.id.item_guanZhu_IvGroup2)
        LinearLayout itemGuanZhuIvGroup2;
        @Bind(R.id.item_guanZhu_IvGroup3)
        LinearLayout itemGuanZhuIvGroup3;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    /**
     * 设置中划线
     */
    public SpannableString getSpannableString(String price, String discount) {
        String value = "￥" + (Double.parseDouble(price) + Double.parseDouble(discount));
        //设置中划线
        SpannableString spannableString = new SpannableString(value);
        spannableString.setSpan(new StrikethroughSpan(), 0, value.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        return spannableString;
    }
}

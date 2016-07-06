package cn.bjsxt.youhuo.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import cn.bjsxt.youhuo.R;
import cn.bjsxt.youhuo.activity.GoodsDetailActivity;
import cn.bjsxt.youhuo.bean.BrandGoodsInfo;
import cn.bjsxt.youhuo.util.HttpModel;
import cn.bjsxt.youhuo.util.ToastUtils;

/**
 * Created by Mecono on 2016/6/12.
 */
public class BrandGoodsInfoAdapter extends BaseViewAdapter<BrandGoodsInfo.GoodsBean> implements AdapterView.OnItemClickListener {
    public BrandGoodsInfoAdapter(Context context, List<BrandGoodsInfo.GoodsBean> list, GridView gridView) {
        super(context, list);
        gridView.setOnItemClickListener(this);
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BrandGoodsInfo.GoodsBean item = getItem(position);
        String goodsId = item.get_id();
        Intent intent = new Intent(context, GoodsDetailActivity.class);
        intent.putExtra("goodsId",goodsId);
        context.startActivity(intent);
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent) {
        BrandGoodsInfoHolder holder = null;
        if (convertView==null){
            convertView = View.inflate(context, R.layout.item_brand_goods_info_grid,null);
            holder = new BrandGoodsInfoHolder();
            //初始化
            holder.goodsImg = (ImageView) convertView.findViewById(R.id.BrandGoodsItem_Img);
            holder.goodsTitle = (TextView) convertView.findViewById(R.id.BrandGoodsItem_TitleTv);
            holder.discount = (TextView) convertView.findViewById(R.id.BrandGoodsItem_DistanceTv);
            holder.price = (TextView) convertView.findViewById(R.id.BrandGoodsItem_PriceTv);
            convertView.setTag(holder);
        }else{
            holder = (BrandGoodsInfoHolder) convertView.getTag();
        }
        //赋值
        BrandGoodsInfo.GoodsBean bean = getItem(position);

        holder.goodsTitle.setText(bean.getTitle());
        holder.price.setText("￥"+bean.getPrice());
        //设置原价，中划线
        String value ="￥"+(Double.parseDouble(bean.getDiscount())+Double.parseDouble(bean.getPrice()));
        //设置中划线
        holder.discount.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);//设置绘制文字的paint的flag
        holder.discount.setText(value);
        //处理图片
        Picasso.with(context).load(HttpModel.IMGURL+bean.getImgpath()).placeholder(R.mipmap.share_loading).into(holder.goodsImg);
        return convertView;
    }


    class BrandGoodsInfoHolder{
        ImageView goodsImg;
        TextView goodsTitle;
        TextView price,discount;
    }
}

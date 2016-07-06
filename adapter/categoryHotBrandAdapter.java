package cn.bjsxt.youhuo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import cn.bjsxt.youhuo.R;
import cn.bjsxt.youhuo.bean.CategoryHotBrandBean;
import cn.bjsxt.youhuo.util.HttpModel;

/**
 * 热门品牌的适配器
 * RecyclerView
 *
 */
public class CategoryHotBrandAdapter extends RecyclerView.Adapter<CategoryHotBrandAdapter.HotBrandHolder>{
    private final List<CategoryHotBrandBean> list;
    private Context context;

    public CategoryHotBrandAdapter(Context context, List<CategoryHotBrandBean> list) {
        this.context = context;
        this.list = list;
    }

    /**
     * 将建并返回对应的viewHolder
     */
    @Override
    public HotBrandHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = View.inflate(context, R.layout.item_category_hot_brand, null);
        HotBrandHolder holder = new HotBrandHolder(item);
        return holder;
    }

    /**
     *赋值
     */
    @Override
    public void onBindViewHolder(HotBrandHolder holder, int position) {
        CategoryHotBrandBean brandBean = list.get(position);
        holder.tv.setText(brandBean.getName());
        Picasso.with(context).load(HttpModel.IMGURL + brandBean.getImgpath()).placeholder(R.mipmap.share_loading).into(holder.iv);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * 申明 并绑定view
     */
    class HotBrandHolder extends RecyclerView.ViewHolder{
        TextView tv;
        ImageView iv;
        public HotBrandHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.category_hotBrand_item_tv);
            iv = (ImageView) itemView.findViewById(R.id.category_hotBrand_item_iv);
        }
    }
}

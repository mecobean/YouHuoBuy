package cn.bjsxt.youhuo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.bjsxt.youhuo.R;
import cn.bjsxt.youhuo.adapter.BaseViewAdapter;
import cn.bjsxt.youhuo.bean.CategoryAllBrandBean;

/**
 * 所有品牌的适配器
 */
public class CategoryAllBrandAdapter extends BaseViewAdapter<CategoryAllBrandBean> {
    /**
     * 用于保存letter的值 并且不重复   a~b~c.......z
     */
    private StringBuffer sb = new StringBuffer();
    /**
     * 用于保存显示letter的name的值
     */
    private List<String> nameList = new ArrayList<>();

    /**
     * 对位提供方法 获取所有letter的集合
     * @return 返回所有sb转换后的list集合
     */
    public List<String> getLetterList() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < sb.length(); i++) {
            list.add(sb.charAt(i)+"");
        }
        return list;
    }

    public CategoryAllBrandAdapter(Context context, List<CategoryAllBrandBean> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_category_all_brand, null);
            holder.nameTv = (TextView) convertView.findViewById(R.id.item_category_allBrand_nameTv);
            holder.latterTv = (TextView) convertView.findViewById(R.id.item_category_allBrand_headTv);
            holder.hotIv = (ImageView) convertView.findViewById(R.id.item_category_allBrand_hotIv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        CategoryAllBrandBean item = getItem(position);
        holder.nameTv.setText(item.getName());
        holder.latterTv.setText(item.getLetter());
        if (item.getHotflag().equals("0")) {
            holder.hotIv.setVisibility(View.VISIBLE);
        } else {
            holder.hotIv.setVisibility(View.GONE);
        }
        //进行letter的去重复
        //sb.indexOf() 查询数据第一次出现的位置  存在返回这个位置 ，不存在返回-1
        if (sb.indexOf(item.getLetter()) == -1) {
            //第一次判断肯定不存在  添加这个第一次出现的数据
            sb.append(item.getLetter());
            nameList.add(item.getName());
        }
        /**
         * 判断nameList中是否包含了item.getName
         */
        if (nameList.contains(item.getName())) {
            holder.latterTv.setVisibility(View.VISIBLE);

        } else {
            holder.latterTv.setVisibility(View.GONE);
        }
        return convertView;
    }

    class ViewHolder {
        TextView latterTv, nameTv;
        ImageView hotIv;
    }
}

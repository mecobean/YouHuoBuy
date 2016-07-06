package cn.bjsxt.youhuo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bjsxt.youhuo.R;
import cn.bjsxt.youhuo.bean.OrderExpandableLvBean;
import cn.bjsxt.youhuo.util.LogUtil;
import cn.bjsxt.youhuo.util.ToastUtils;

/**
 * 确认订单expandableAdapter
 */
public class OrderExpandableLvAdapter extends MyBaseExpandableAdapter {
    /**
     * key : group的id
     * value : child的id
     */
    public Map<Integer, Integer> childMap = new HashMap<>();
    /**
     * key : group的id
     * value : group的指示器状态 -- true 向上 / false 向下
     */
    public Map<Integer, Boolean> parentMap = new HashMap<>();

    public OrderExpandableLvAdapter(Context context, List<OrderExpandableLvBean> list) {
        super(context, list);
        intiMap();
    }

    /**
     * 初始化map 数据
     */
    private void intiMap() {
        for (int i = 0; i < list.size(); i++) {
            childMap.put(i, 0);
            parentMap.put(i, false);
        }
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, final ViewGroup parent) {
        //初始化父item
        convertView = View.inflate(context, R.layout.item_order_expand_group, null);
        GroupViewHolder holder = new GroupViewHolder(convertView);
        ExpandableListView lv = (ExpandableListView) parent;
        //设置parent 标题
        holder.itemOrderExpandGroupTitle.setText(list.get(groupPosition).getGroup());
        //设置孩子标题
        holder.itemOrderExpandGroupChildTitle.setText(list.get(groupPosition).getChild().get(childMap.get(groupPosition)));
        //根据map 动态设置小箭头
        if (parentMap.get(groupPosition)) {
            holder.itemOrderExpandGroupImg.setImageResource(R.mipmap.up_order);
        } else {
            holder.itemOrderExpandGroupImg.setImageResource(R.mipmap.down_order);
        }
        //孩子监听
        lv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                //保存点击的孩子的id
                childMap.put(groupPosition, childPosition);
                notifyDataSetChanged();
                return false;
            }
        });
        //group关闭监听
        lv.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                parentMap.put(groupPosition, false);
                notifyDataSetChanged();
            }
        });
        //group打开监听
        lv.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                parentMap.put(groupPosition, true);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, final ViewGroup parent) {
        convertView = View.inflate(context, R.layout.item_order_expand_child, null);
         final ChildViewHolder holder = new ChildViewHolder(convertView);
        holder.itemOrderExpandChildTitle.setText(list.get(groupPosition).getChild().get(childPosition));
        //设置checkBox 的选中状态
        holder.itemOrderExpandChildCb.setChecked(false);
        if (childPosition == childMap.get(groupPosition)) {
            holder.itemOrderExpandChildCb.setChecked(true);
        }
        holder.itemOrderExpandChildCb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //保存点击的孩子的id
                childMap.put(groupPosition, childPosition);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }


    static class GroupViewHolder {
        @Bind(R.id.item_order_expand_group_Title)
        TextView itemOrderExpandGroupTitle;
        @Bind(R.id.item_order_expand_group_childTitle)
        TextView itemOrderExpandGroupChildTitle;
        @Bind(R.id.item_order_expand_group_img)
        ImageView itemOrderExpandGroupImg;

        GroupViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class ChildViewHolder {
        @Bind(R.id.item_order_expand_child_Title)
        TextView itemOrderExpandChildTitle;
        @Bind(R.id.item_order_expand_child_cb)
        CheckBox itemOrderExpandChildCb;

        ChildViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

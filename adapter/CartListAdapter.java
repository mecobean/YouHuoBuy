package cn.bjsxt.youhuo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bjsxt.youhuo.Enum.CartModeType;
import cn.bjsxt.youhuo.R;
import cn.bjsxt.youhuo.bean.CartListInfoBean;
import cn.bjsxt.youhuo.dialog.ChooseGoodsDialog;
import cn.bjsxt.youhuo.dialog.LoadingDialog;
import cn.bjsxt.youhuo.event.SelectGoodsEvent;
import cn.bjsxt.youhuo.util.HttpModel;
import cn.bjsxt.youhuo.util.ToastUtils;

/**
 * 购物车listView的adapter
 * 加载两个不同的布局
 */
public class CartListAdapter extends BaseViewAdapter<CartListInfoBean.CartBean> {
    /**
     * 布局的类型 默认为正常模式
     */
    private int modeType = CartModeType.NORMAL.getValue();
    private LoadingDialog loadingDialog;
    /**
     * 是否显示线
     */
    private boolean isShowLine = false;
    /**
     * 是否显示checkBox
     */
    private boolean isShowCb = true;

    public CartListAdapter(Context context, List<CartListInfoBean.CartBean> list) {
        super(context, list);
        loadingDialog = new LoadingDialog(context);
    }

    private void loadingDismiss() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingDialog.dismiss();
            }
        }, 700);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CartBaseViewHolder holder = null;
        if (convertView == null) {
            if (getItemViewType(position) == CartModeType.NORMAL.getValue()) {//加载item_1 布局
                convertView = View.inflate(context, R.layout.item_cart_1, null);
                holder = new ViewHolderNormal(convertView);
                convertView.setTag(holder);
            } else if (getItemViewType(position) == CartModeType.EDITING.getValue()) {//加载item_2 布局
                convertView = View.inflate(context, R.layout.item_cart_2, null);
                holder = new ViewHolderEditing(convertView);
                convertView.setTag(holder);
            }
        } else {
            holder = (CartBaseViewHolder) convertView.getTag();
        }
        showItem(position, holder);
        return convertView;
    }

    /**
     * 渲染数据
     */
    private void showItem(int position, CartBaseViewHolder holder) {
        if (getItemViewType(position) == CartModeType.NORMAL.getValue()) {//item_1
            showNormal(position, (ViewHolderNormal) holder);
        } else if (getItemViewType(position) == CartModeType.EDITING.getValue()) {//item_2
            showEditing(position, (ViewHolderEditing) holder);
        }
    }

    /**
     * 显示编辑模式的布局
     */
    private void showEditing(int position, ViewHolderEditing holder) {
        CartListInfoBean.CartBean item = getItem(position);
        holder.itemCart2GoodsCb.setChecked(item.isCheck);
        holder.itemCart2ShopCb.setChecked(item.isCheck);
        holder.itemCart2GoodsCb.setTag(position);
        holder.itemCart2ShopCb.setTag(position);

        Picasso.with(context).load(HttpModel.IMGURL + item.getImgpath()).placeholder(R.mipmap.share_loading).into(holder.itemCart2GoodsImg);
        holder.itemCart2GoodsCS.setText("颜色：" + item.getColor() + " 尺寸：" + item.getSize());
        holder.itemCart2GoodsNum.setText(item.getNum());
        holder.itemCart2goodsPrice.setText("￥" + item.getPrice());
    }

    /**
     * 显示正常模式的布局
     */
    private void showNormal(int position, ViewHolderNormal holder) {
        CartListInfoBean.CartBean item = getItem(position);
        if (isShowLine) {
            holder.itemCart1Line.setVisibility(View.VISIBLE);
        }
        if (isShowCb) {
            holder.itemCart1GoodsCb.setChecked(item.isCheck);
            holder.itemCart1ShopCb.setChecked(item.isCheck);
            holder.itemCart1GoodsCb.setTag(position);
            holder.itemCart1ShopCb.setTag(position);
        }else {
            holder.itemCart1GoodsCb.setVisibility(View.GONE);
            holder.itemCart1ShopCb.setVisibility(View.GONE);
        }
        //图片
        Picasso.with(context).load(HttpModel.IMGURL + item.getImgpath()).placeholder(R.mipmap.share_loading).into(holder.itemCart1GoodsImg);
        //商品标题
        holder.itemCart1GoodsTitle.setText(item.getTitle());
        //商品样式
        holder.itemCart1GoodsCS.setText("颜色：" + item.getColor() + "  尺寸：" + item.getSize());
        //价钱
        holder.itemCart1GoodsPrice.setText("￥" + item.getPrice());
        //数量
        holder.itemCart1GoodsNum.setText("x" + item.getNum());
        //editing
        holder.itemCart1Goodsediting.setTag(position);
    }

    @Override
    public int getItemViewType(int position) {
        return modeType;
    }

    /**
     * listView 不同布局的个数
     */
    @Override
    public int getViewTypeCount() {
        return 2;
    }

    /**
     * 拿到listView的布局模式
     */
    public int getMode() {
        return modeType;
    }

    /**
     * 设置listView的布局模式
     */
    public void setMode(CartModeType modeType) {
        this.modeType = modeType.getValue();
    }

    /**
     * viewHolder基类
     */
    static class CartBaseViewHolder {
    }

    class ViewHolderEditing extends CartBaseViewHolder {
        @Bind(R.id.item_cart_2_shopCb)
        CheckBox itemCart2ShopCb;
        @Bind(R.id.item_cart_2_goodsCb)
        CheckBox itemCart2GoodsCb;
        @Bind(R.id.item_cart_2_goodsImg)
        ImageView itemCart2GoodsImg;
        @Bind(R.id.item_cart_2_goodsCS)
        TextView itemCart2GoodsCS;
        @Bind(R.id.item_cart_2_goodsNum)
        TextView itemCart2GoodsNum;
        @Bind(R.id.item_cart_2_subtract)
        TextView itemCart2Subtract;
        @Bind(R.id.item_cart_2__add)
        TextView itemCart2Add;
        @Bind(R.id.item_cart_2_goodsPrice)
        TextView itemCart2goodsPrice;

        ViewHolderEditing(View view) {
            ButterKnife.bind(this, view);
            itemCart2ShopCb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectEditingGoods(itemCart2ShopCb, itemCart2GoodsCb);
                }
            });
            itemCart2GoodsCb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectEditingGoods(itemCart2GoodsCb, itemCart2ShopCb);
                }
            });
            //商品数量减
            itemCart2Subtract.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i = Integer.parseInt(itemCart2GoodsNum.getText().toString());
                    if (i > 1) {
                        loadingDialog.show();
                        loadingDismiss();
                        i--;
                        itemCart2GoodsNum.setText("" + i);
                        int positon = (int) itemCart2GoodsCb.getTag();
                        list.get(positon).setNum("" + i);
                    } else {
                        ToastUtils.toast("最后一件商品");
                    }
                }
            });
            //商品数量加
            itemCart2Add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i = Integer.parseInt(itemCart2GoodsNum.getText().toString());
                    // 联网添加
                    i++;
                    // 模拟联网
                    loadingDialog.show();
                    loadingDismiss();
                    itemCart2GoodsNum.setText("" + i);
                    int positon = (int) itemCart2GoodsCb.getTag();
                    list.get(positon).setNum("" + i);
                }
            });
            //选择商品样式
            itemCart2GoodsCS.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int positon = (int) itemCart2GoodsCb.getTag();
                    final CartListInfoBean.CartBean cartBean = list.get(positon);
                    ChooseGoodsDialog chooseGoodsDialog = new ChooseGoodsDialog(context, cartBean);
                    chooseGoodsDialog.setOnAddCartSureListener(new ChooseGoodsDialog.OnAddCartSureListener() {
                        @Override
                        public void addCartSuccess(int goodsNumber, Bitmap bitmap) {
                            cartBean.setNum((Integer.parseInt(cartBean.getNum()) + goodsNumber) + "");
                            notifyDataSetChanged();
                        }
                    });
                    chooseGoodsDialog.show();
                }
            });
        }
    }

    class ViewHolderNormal extends CartBaseViewHolder {
        @Bind(R.id.item_cart_1_shopCb)
        CheckBox itemCart1ShopCb;
        @Bind(R.id.item_cart_1_goodsCb)
        CheckBox itemCart1GoodsCb;
        @Bind(R.id.item_cart_1_goodsImg)
        ImageView itemCart1GoodsImg;
        @Bind(R.id.item_cart_1_goodsTitle)
        TextView itemCart1GoodsTitle;
        @Bind(R.id.item_cart_1_goodsCS)
        TextView itemCart1GoodsCS;
        @Bind(R.id.item_cart_1_goodsPrice)
        TextView itemCart1GoodsPrice;
        @Bind(R.id.item_cart_1_goodsNum)
        TextView itemCart1GoodsNum;
        @Bind(R.id.item_cart_1_goodsEditing)
        TextView itemCart1Goodsediting;
        @Bind(R.id.item_cart_1_line)
        View itemCart1Line;

        ViewHolderNormal(View view) {
            ButterKnife.bind(this,view);
            //商品checkBox点击
            itemCart1GoodsCb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectNormalGoods(itemCart1GoodsCb, itemCart1ShopCb);
                }
            });
            //商铺checkBox点击
            itemCart1ShopCb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectNormalGoods(itemCart1ShopCb, itemCart1GoodsCb);
                }
            });
            itemCart1Goodsediting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (int) itemCart1Goodsediting.getTag();
//                    setMode(CartModeType.EDITING);
//                    View item = getView(position,null,null);
                    ToastUtils.toast(position + "");
                }
            });
        }
    }

    /**
     * 正常布局
     * 选中/取消 商品
     * 要传两个checkBox
     * 第一个是当前点击的checkBox 第二个是跟随改变的checkBox
     *
     * @param cb 点击的checkBox
     */
    private void selectNormalGoods(CheckBox... cb) {
        int position = (int) cb[0].getTag();
        list.get(position).isCheck = cb[0].isChecked();
        cb[1].setChecked(cb[0].isChecked());
        //全选标记
        boolean allSelect = true;
        int prices = 0;
        int num = 0;
        int k = 0;
        for (int i = 0; i < list.size(); i++) {
            CartListInfoBean.CartBean cartBean = list.get(i);
            if (cartBean.isCheck) { //选中 计算价钱
                prices += Integer.parseInt(cartBean.getPrice()) * Integer.parseInt(cartBean.getNum());
                num += Integer.parseInt(cartBean.getNum());
                k++;
            }
        }
        if (k < list.size()) {//判断是否全部选中
            allSelect = false;
        }
        //发送处理后的价钱 与选中状态 和选中的商品总数
        EventBus.getDefault().post(new SelectGoodsEvent(allSelect, num, prices));
    }

    /**
     * 编辑模式布局
     * 选中/取消 商品
     * 要传两个checkBox
     * 第一个是当前点击的checkBox 第二个是跟随改变的checkBox
     *
     * @param cb 点击的checkBox
     */
    private void selectEditingGoods(CheckBox... cb) {
        int position = (int) cb[0].getTag();
        list.get(position).isCheck = cb[0].isChecked();
        cb[1].setChecked(cb[0].isChecked());
        //全选标记
        boolean allSelect = true;
        int prices = 0;
        int num = 0;
        int k = 0;
        for (int i = 0; i < list.size(); i++) {
            CartListInfoBean.CartBean cartBean = list.get(i);
            if (cartBean.isCheck) { //选中 计算价钱
                k++;
            }
        }
        if (k < list.size()) {//判断是否全部选中
            allSelect = false;
        }
        //发送处理后的价钱 与选中状态 和选中的商品总数
        EventBus.getDefault().post(new SelectGoodsEvent(allSelect, num, prices));
    }

    /**
     * 设置item 顶部手绘的线是否显示
     */
    public void setLine(boolean isShowLine) {
        this.isShowLine = isShowLine;
    }

    /**
     * 设置item checkBox 是否显示
     */
    public void setCheckBox(boolean isShowCb) {
        this.isShowCb = isShowCb;
    }
}

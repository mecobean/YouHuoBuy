package cn.bjsxt.youhuo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bjsxt.youhuo.Enum.CartModeType;
import cn.bjsxt.youhuo.MyApplication;
import cn.bjsxt.youhuo.R;
import cn.bjsxt.youhuo.adapter.CartListAdapter;
import cn.bjsxt.youhuo.bean.BaseBean;
import cn.bjsxt.youhuo.bean.CartListInfoBean;
import cn.bjsxt.youhuo.bean.OrderLvBean;
import cn.bjsxt.youhuo.event.CartListEvent;
import cn.bjsxt.youhuo.event.SelectGoodsEvent;
import cn.bjsxt.youhuo.handler.MainCartHandler;
import cn.bjsxt.youhuo.util.HttpHandler;
import cn.bjsxt.youhuo.util.HttpModel;
import cn.bjsxt.youhuo.util.HttpThread;
import cn.bjsxt.youhuo.util.RequestData;
import cn.bjsxt.youhuo.util.ToastUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;


public class CartActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 返回
     */
    @Bind(R.id.toolbar_cart_backIb)
    ImageButton toolbarCartBackIb;
    /**
     * 编辑
     */
    @Bind(R.id.toolbar_cart_editBtn)
    Button toolbarCartEditBtn;
    /**
     * 全选CheckBox
     */
    @Bind(R.id.cart_allSelect_CB)
    CheckBox cartAllSelectCB;
    /**
     * 全选/全不选  textView
     */
    @Bind(R.id.cart_allSelect_Tv)
    TextView cartAllSelectTv;
    /**
     * 结算
     */
    @Bind(R.id.cart_Settlement)
    TextView cartSettlement;
    /**
     * 总价
     */
    @Bind(R.id.cart_allPrice_Tv)
    TextView cartAllPriceTv;
    /**
     * 移动到收藏夹
     */
    @Bind(R.id.cart_collect)
    TextView cartCollect;
    /**
     * listView
     */
    @Bind(R.id.cart_lv)
    ListView cartLv;
    private MainCartHandler cartHandler;
    private CartListAdapter cartListAdapter;

    private List<CartListInfoBean.CartBean> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initUtils();
        initDatas();
        initListener();
    }

    /**
     * 初始化监听
     */
    private void initListener() {
        toolbarCartEditBtn.setOnClickListener(this);
        toolbarCartBackIb.setOnClickListener(this);
        cartSettlement.setOnClickListener(this);
        cartAllSelectCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {//全选
                    int prices = 0;
                    int num = 0;
                    cartAllSelectTv.setText("全不选");
                    for (CartListInfoBean.CartBean bean : list) {
                        bean.isCheck = true;
                        prices += Integer.parseInt(bean.getPrice()) * Integer.parseInt(bean.getNum());
                        num += Integer.parseInt(bean.getNum());
                    }
                    settlementGoods(prices, num);
                } else {//全不选
                    cartAllSelectTv.setText("全选");
                    for (CartListInfoBean.CartBean bean : list) {
                        bean.isCheck = false;
                    }
                    settlementGoods(0, 0);
                }
                cartListAdapter.notifyDataSetChanged();
            }
        });
        cartLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //跳转到 商品详情页
                Intent intent = new Intent(CartActivity.this,GoodsDetailActivity.class);
                intent.putExtra("goodsId",position+"");
                startActivity(intent);
            }
        });
    }

    private void initUtils() {
        cartHandler = new MainCartHandler(this);
    }

    /**
     * 从服务器请求购物车数据
     */
    private void initDatas() {
        new HttpThread(this, HttpModel.CART_LIST_URL, "parames={\"userId\":\"6\"}", cartHandler, HttpHandler.CART_LIST_SUCCESS).start();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showGoods(CartListEvent cartListEvent) {
        //拿到数据集合
        List<CartListInfoBean.CartBean> cart = cartListEvent.cartListInfoBean.getCart();
        for (int i = 0; i < list.size(); i++) {
            //切换的时候 保存选中的状态
            cart.get(i).isCheck = list.get(i).isCheck;
        }
        list.clear();
        list.addAll(cart);
        if (cartListAdapter == null) {
            cartListAdapter = new CartListAdapter(this, list);
            cartLv.setAdapter(cartListAdapter);
        } else {
            int mode = cartListAdapter.getMode();
            if (mode == CartModeType.NORMAL.getValue()) {
                //正常 模式
                toolbarCartEditBtn.setText("编辑");
                cartSettlement.setText("结算(0)");
                cartCollect.setVisibility(View.GONE);
            } else if (mode == CartModeType.EDITING.getValue()) {
                //编辑模式
                toolbarCartEditBtn.setText("完成");
                cartSettlement.setText("删除");
                cartCollect.setVisibility(View.VISIBLE);
            }
            cartListAdapter.notifyDataSetChanged();
//            cartLv.setAdapter(cartListAdapter);
        }
    }

    /**
     * 处理商品选中的事件
     *
     * @param goodsEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void selectGoodsResult(SelectGoodsEvent goodsEvent) {
        //是否全选
        cartAllSelectCB.setChecked(goodsEvent.allSelect);
        if (goodsEvent.allSelect) {
            cartAllSelectTv.setText("全不选");
        } else {
            cartAllSelectTv.setText("全选");
        }
        settlementGoods(goodsEvent.prices, goodsEvent.num);

    }

    /**
     * 设置商品的总价 和总数e
     *
     * @param prices
     * @param num
     */
    private void settlementGoods(int prices, int num) {
        if (cartListAdapter.getMode() == CartModeType.NORMAL.getValue()) {
            //总数
            cartSettlement.setText("结算(" + num + ")");
            //总价
            cartAllPriceTv.setText("￥" + prices + ".0");
        } else if (cartListAdapter.getMode() == CartModeType.EDITING.getValue()) {
            //总数
            cartSettlement.setText("删除");
        }
    }

    /**
     * 初始化控件
     */
    private void initView() {
        inflaterView(R.layout.activity_cart);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        list = new ArrayList<>();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }

    /**
     * 点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_cart_editBtn://编辑 / 正常
                changeModeType();
                break;
            case R.id.toolbar_cart_backIb://返回
                onBackPressed();
                break;
            case R.id.cart_Settlement://结算/删除
                settlementAndDelectGoods();
                break;

        }
    }

    /**
     * 结算/删除商品
     */
    private void settlementAndDelectGoods() {
        if (cartListAdapter.getMode() == CartModeType.NORMAL.getValue()) {
            //结算
            settlement();
        } else if (cartListAdapter.getMode() == CartModeType.EDITING.getValue()) {
            //删除
            List<CartListInfoBean.CartBean> temp = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).isCheck)
                    temp.add(list.get(i));
            }
            list.removeAll(temp);
            if (list.size() == 0 && cartAllSelectCB.isChecked()){
                //购物车清空
                cartAllSelectCB.performClick();
            }
            cartListAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 拿到选中的商品 跳转到结算界面
     */
    private void settlement() {
        Intent intent = new Intent(this,OrderActivity.class);
        if (cartAllSelectCB.isChecked()){
            //已经全选
            intent.putExtra("order",new OrderLvBean(list));
        }else {
            List<CartListInfoBean.CartBean> temp = new ArrayList<>();
            //没有全选  遍历数据 拿出选中的
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).isCheck){
                    temp.add(list.get(i));
                }
            }
            if (temp.size() ==0){
                ToastUtils.toast("请先选择商品");
                return;
            }
            intent.putExtra("order",new OrderLvBean(temp));
        }
      startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_updown_quit_in, R.anim.activity_updown_enter_out);
    }

    /**
     * 改变当前的listView的布局
     */
    private void changeModeType() {
        int mode = cartListAdapter.getMode();
        if (mode == CartModeType.NORMAL.getValue()) {
            //从正常 切换到编辑模式
            cartListAdapter.setMode(CartModeType.EDITING);
        } else if (mode == CartModeType.EDITING.getValue()) {
            //从编辑模式切换到正常模式
            cartListAdapter.setMode(CartModeType.NORMAL);
        }
        //重新从服务器请求购物车数据
        initDatas();
    }
}

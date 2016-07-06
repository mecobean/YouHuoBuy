package cn.bjsxt.youhuo.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bjsxt.youhuo.R;
import cn.bjsxt.youhuo.adapter.CartListAdapter;
import cn.bjsxt.youhuo.adapter.OrderExpandableLvAdapter;
import cn.bjsxt.youhuo.alipay.PayDialog;
import cn.bjsxt.youhuo.bean.CartListInfoBean;
import cn.bjsxt.youhuo.bean.OrderExpandableLvBean;
import cn.bjsxt.youhuo.bean.OrderLvBean;
import cn.bjsxt.youhuo.util.LocationUtils;
import cn.bjsxt.youhuo.util.ToastUtils;
import cn.bjsxt.youhuo.view.MyExpanableListView;

/**
 * 确认订单啊activity
 */
public class OrderActivity extends BaseActivity implements View.OnClickListener, LocationUtils.OnLocationListener {

    @Bind(R.id.toolbar_order_backIb)
    ImageButton toolbarOrderBackIb;
    @Bind(R.id.order_lv)
    ListView orderLv;
    @Bind(R.id.order_bottomBar_submitTv)
    TextView orderBottomBarSubmitTv;
    @Bind(R.id.order_bottomBar_prices)
    TextView orderBottomBarPrices;
    private View headUser;
    private View headLine;
    private MyExpanableListView expanableListView;
    private List<OrderExpandableLvBean> orderExpandableLvList;
    private List<CartListInfoBean.CartBean> list;
    private OrderExpandableLvAdapter expandableLvAdapter;
    private LocationUtils locationUtils;
    private TextView userLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //拿到数据
        OrderLvBean order = (OrderLvBean) getIntent().getSerializableExtra("order");
        list = (List<CartListInfoBean.CartBean>) order.object;
        initViews();
        initOrderPrices();
        initHeadUser();
        initHeadLine();
        initHeadExpandableLv();
        initLocation();
        initLv();
        initListener();
    }

    /**
     * 初始化当前位置
     */
    private void initLocation() {
        locationUtils = new LocationUtils();
        locationUtils.startLocation();
        locationUtils.setOnLocationListener(this);
    }

    /**
     * 初始化订单的价钱
     */
    private void initOrderPrices() {
        double prices = 0;
        for (int i = 0; i < list.size(); i++) {
          prices += Double.parseDouble(list.get(i).getPrice()) *  Double.parseDouble(list.get(i).getNum());
        }
        //设置不同字体属性
//        BigDecimal bigDecimal = new BigDecimal(prices);
//        bigDecimal.setScale(2);
        String format = "合计：￥" + prices +"0";
        SpannableString spannableString = new SpannableString(format);
        spannableString.setSpan(new TextAppearanceSpan(this,R.style.order_prices_0),0,3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new TextAppearanceSpan(this,R.style.order_prices_1),3,format.length()-3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new TextAppearanceSpan(this,R.style.order_prices_2),format.length()-3,format.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        orderBottomBarPrices.setText(spannableString,TextView.BufferType.SPANNABLE);
    }

    /**
     * 初始化监听
     */
    private void initListener() {
        toolbarOrderBackIb.setOnClickListener(this);
        orderBottomBarSubmitTv.setOnClickListener(this);
        orderLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    //如果点击第一个头部 重新进行定位
                    ToastUtils.toast("正在重新定位，请稍后 ...");
                    locationUtils.startLocation();
                }else {
                    //跳转到 商品详情页
                    Intent intent = new Intent(OrderActivity.this, GoodsDetailActivity.class);
                    intent.putExtra("goodsId", position + "");
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * 初始化listView
     */
    private void initLv() {
        //添加头部
        orderLv.addHeaderView(headUser);
        orderLv.addHeaderView(headLine);
        orderLv.addHeaderView(expanableListView);

        CartListAdapter cartListAdapter = new CartListAdapter(this, list);
        //隐藏左边的checkBox
        cartListAdapter.setCheckBox(false);
        //显示手绘的线
        cartListAdapter.setLine(true);

        orderLv.setAdapter(cartListAdapter);
    }

    /**
     * 初始化头部 选项
     */
    private void initHeadExpandableLv() {
        expanableListView = new MyExpanableListView(this);

        //初始化expandableLv 数据
        orderExpandableLvList = new ArrayList<>();
        orderExpandableLvList.add(getOrderExpandableLvBean("支付方式", "在线支付", "货到付款"));
        orderExpandableLvList.add(getOrderExpandableLvBean("配送方式", "普通快递", "顺丰"));
        orderExpandableLvList.add(getOrderExpandableLvBean("送货时间", "只周日送货", "全天送货", "节假日送货"));

        expandableLvAdapter = new OrderExpandableLvAdapter(this, orderExpandableLvList);
        expanableListView.setAdapter(expandableLvAdapter);
        //设置group指示器透明
        expanableListView.setGroupIndicator(new BitmapDrawable());
        expanableListView.setDividerHeight(0);
    }

    /**
     * 拿到一个OrderExpandableLvBean
     */
    private OrderExpandableLvBean getOrderExpandableLvBean(String... value) {
        OrderExpandableLvBean bean = new OrderExpandableLvBean();
        bean.setGroup(value[0]);
        List<String> temp = new ArrayList<>();
        for (int i = 1; i < value.length; i++) {
            temp.add(value[i]);
        }
        bean.setChild(temp);
        return bean;
    }

    /**
     * listView 头部 渐变分割线
     */
    private void initHeadLine() {
        headLine = View.inflate(this, R.layout.order_head_line, null);
    }

    /**
     * 初始化listView的头部
     * 用户信息
     */
    private void initHeadUser() {
        headUser = View.inflate(this, R.layout.order_head_user, null);
        userLocation = (TextView) headUser.findViewById(R.id.order_user_location);
    }

    /**
     * 初始化控件
     */
    private void initViews() {
        inflaterView(R.layout.activity_order);
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //返回
            case R.id.toolbar_order_backIb:
                onBackPressed();
                break;
            case R.id.order_bottomBar_submitTv:
                //提交订单
               new PayDialog(this).show();
                break;
        }
    }

    /**
     * 定位回调
     * @param s
     */
    @Override
    public void locationCallBack(String s) {
        if (s != null){
            userLocation.setText(s);
        }
        locationUtils.stopLocation();
    }
}

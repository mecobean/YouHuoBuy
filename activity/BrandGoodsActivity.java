package cn.bjsxt.youhuo.activity;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.bjsxt.youhuo.R;
import cn.bjsxt.youhuo.adapter.BasePagerAdapter;
import cn.bjsxt.youhuo.adapter.BrandGoodsInfoAdapter;
import cn.bjsxt.youhuo.adapter.BrandGoodsPullAndPushAdapter;
import cn.bjsxt.youhuo.bean.BrandGoodsInfo;
import cn.bjsxt.youhuo.util.HttpHandler;
import cn.bjsxt.youhuo.util.HttpModel;
import cn.bjsxt.youhuo.util.HttpThread;
import cn.bjsxt.youhuo.util.JsonUtils;
import cn.bjsxt.youhuo.util.LogUtil;
import cn.bjsxt.youhuo.util.ToastUtils;
import cn.bjsxt.youhuo.view.MyGridView;
import cn.bjsxt.youhuo.view.PullAndPushListView;

public class BrandGoodsActivity extends BaseActivity implements View.OnClickListener, PullAndPushListView.OnRefreshListener {

    private TabLayout tabLayout;
    /**
     * 传过来的品牌id 通过此id从服务器请求该品牌的列表项
     */
    private String brandId;
    /**
     * viewPager
     */
    private ViewPager vp;
    private JsonUtils jsonUtils;
    private HttpHandler handler;
    private TextView name;
    /**
     * 价格gridView的数据集合
     */
    private List<BrandGoodsInfo.GoodsBean> priceList = new ArrayList<>();
    /**
     * 折扣GridView的数据集合
     */
    private List<BrandGoodsInfo.GoodsBean> discontList = new ArrayList<>();
    /**
     * 最新的数据集合
     */
    private List<BrandGoodsInfo.GoodsBean> goodsNew = new ArrayList<>();
    private List<PullAndPushListView> vpList;
    private BasePagerAdapter<PullAndPushListView> pagerAdapter;
    private ImageButton back;
    private BrandGoodsInfoAdapter goosInfoAdapter;
    private BrandGoodsInfoAdapter priceAdapter;
    private BrandGoodsInfoAdapter discountAdapter;
    private int i = 0;
    private List<BrandGoodsInfoAdapter> goodsInfoAdapters = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() == null) {
            return;
        }
        brandId = getIntent().getStringExtra("brandId");
        initUtils();
        initBrandGoodsView();
        initTabLayout();
        initVPDatas();
        initGridDates();
        initListener();
    }

    /**
     * 初始化监听
     */
    private void initListener() {
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vp.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        TabLayout.TabLayoutOnPageChangeListener listener = new TabLayout.TabLayoutOnPageChangeListener(tabLayout);
        vp.addOnPageChangeListener(listener);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_brand_goods_back:
                onBackPressed();
                break;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * 上下拉刷新的回调
     */
    @Override
    public void pullDownRefresh() {

    }

    @Override
    public void pullUpRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                i++;
                int selectedTabPosition = tabLayout.getSelectedTabPosition();
                PullAndPushListView pushListView = vpList.get(selectedTabPosition);
                if (i < 3) {
                    initGridDates();
                    pushListView.pullUpRefreshSuccess();
                    ToastUtils.toast("第" + selectedTabPosition + "项上拉加载");
                    for (int i = 0; i < 3; i++) {
                        if (selectedTabPosition == i) {
                            goodsInfoAdapters.get(selectedTabPosition).notifyDataSetChanged();
                        }
                    }
                } else {
                    pushListView.setEndView();
                }
            }
        }, 2000);
    }

    class BrandGoodsHandler extends HttpHandler {

        public BrandGoodsHandler(Context context) {
            super(context);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HttpHandler.BRAND_INFO_SUCCESS://商品列表正确返回
                    LogUtil.logE(LogUtil.DEBUG_TAG, "商品列表正确返回的数据--------》" + msg.obj.toString());
                    handlerResult(msg.obj.toString());
                    break;
            }
        }
    }

    /**
     * 处理返回的数据
     *
     * @param result
     */
    private void handlerResult(String result) {
        //拿到数据
        BrandGoodsInfo brandGoodsInfo = jsonUtils.getBrandGoodsInfo(result);
        //设置商品品牌名称
        name.setText(brandGoodsInfo.getBrandname());
        //拿到内部封装的数据集合
        goodsNew.addAll(brandGoodsInfo.getGoods());

        //添加第二个gridView的数据源 并排序
        priceList.addAll(brandGoodsInfo.getGoods());
        shortByPriceList();
        //添加第三个gridView的数据源 并排序
        discontList.addAll(brandGoodsInfo.getGoods());
        shortByDiscount();

        //创建  3个gridView的适配器
        if (goosInfoAdapter == null) {
            goosInfoAdapter = new BrandGoodsInfoAdapter(this, goodsNew, (GridView) vpList.get(0).getTag());
            ((GridView) vpList.get(0).getTag()).setAdapter(goosInfoAdapter);
            goodsInfoAdapters.add(0,goosInfoAdapter);
        }

        if (priceAdapter == null) {
            priceAdapter = new BrandGoodsInfoAdapter(this, priceList, (GridView) vpList.get(1).getTag());
            ((GridView) vpList.get(1).getTag()).setAdapter(priceAdapter);
            goodsInfoAdapters.add(1,priceAdapter);
        }

        if (discountAdapter == null) {
            discountAdapter = new BrandGoodsInfoAdapter(this, discontList, (GridView) vpList.get(2).getTag());
            ((GridView) vpList.get(2).getTag()).setAdapter(discountAdapter);
            goodsInfoAdapters.add(2,discountAdapter);
        }

        //渲染数据
        if (pagerAdapter == null) {
            pagerAdapter = new BasePagerAdapter<>(this, vpList);
            vp.setAdapter(pagerAdapter);
        }

    }

    /**
     * 按照价格排序
     */
    private void shortByPriceList() {
        Collections.sort(priceList, new Comparator<BrandGoodsInfo.GoodsBean>() {
            @Override
            public int compare(BrandGoodsInfo.GoodsBean lhs, BrandGoodsInfo.GoodsBean rhs) {
                return lhs.getPrice().compareTo(rhs.getPrice());
            }
        });
    }

    /**
     * 按照折扣排序
     */
    private void shortByDiscount() {
        Collections.sort(priceList, new Comparator<BrandGoodsInfo.GoodsBean>() {
            @Override
            public int compare(BrandGoodsInfo.GoodsBean lhs, BrandGoodsInfo.GoodsBean rhs) {
                return lhs.getDiscount().compareTo(rhs.getDiscount());
            }
        });
    }

    /**
     * 初始化gridVIew的数据
     */
    private void initGridDates() {
        new HttpThread(this, HttpModel.BRAND_INFO_URL, "parames={\"page\":\"0\",\"id\":\"" + brandId + "\"}", handler, HttpHandler.BRAND_INFO_SUCCESS).start();
    }

    /**
     * 初始化viewPager的数据
     * viewPager每页是listView
     */
    private void initVPDatas() {
        vpList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            PullAndPushListView pushListView = new PullAndPushListView(this);
            GridView gridView = new MyGridView(this);
            gridView.setNumColumns(2);//设置列数
            gridView.setHorizontalSpacing(10);//设置水平间距
            gridView.setVerticalSpacing(10);//设置垂直间距
            gridView.setPadding(20, 10, 20, 10);//设置内边距
            gridView.setVerticalScrollBarEnabled(true);
            pushListView.setTag(gridView);
            pushListView.setHead(false);
            pushListView.setAdapter(new BrandGoodsPullAndPushAdapter());
            pushListView.setOnRefreshListener(this);
            vpList.add(pushListView);
        }
    }

    /**
     * 初始化一些工具类
     */
    private void initUtils() {
        jsonUtils = new JsonUtils();
        handler = new BrandGoodsHandler(this);
    }

    /**
     * 初始化tabLayout的选项卡
     */
    private void initTabLayout() {
        TabLayout.Tab tab1 = tabLayout.newTab();
        tab1.setText("最新");
        TabLayout.Tab tab2 = tabLayout.newTab();
        tab2.setText("价格");
        TabLayout.Tab tab3 = tabLayout.newTab();
        tab3.setText("折扣");
        tabLayout.addTab(tab1,0, true);
        tabLayout.addTab(tab2,1);
        tabLayout.addTab(tab3,2);
    }

    /**
     * 初始化View
     */
    private void initBrandGoodsView() {
        inflaterView(R.layout.activity_brand_goods, getResources().getColor(R.color.toolBar_bg));
        tabLayout = (TabLayout) childView.findViewById(R.id.brand_goods_tabLayout);
        vp = (ViewPager) childView.findViewById(R.id.brand_goods_vp);
        vp.setOffscreenPageLimit(4);
        name = (TextView) childView.findViewById(R.id.toolbar_brand_goods_name);
        back = (ImageButton) childView.findViewById(R.id.toolbar_brand_goods_back);
    }
}

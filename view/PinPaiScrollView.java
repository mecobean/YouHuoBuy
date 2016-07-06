package cn.bjsxt.youhuo.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;

import cn.bjsxt.youhuo.R;
import cn.bjsxt.youhuo.activity.BrandGoodsActivity;
import cn.bjsxt.youhuo.adapter.CategoryHotBrandAdapter;
import cn.bjsxt.youhuo.bean.AdvertInfo;
import cn.bjsxt.youhuo.bean.CategoryAllBrandBean;
import cn.bjsxt.youhuo.bean.CategoryHotBrandBean;
import cn.bjsxt.youhuo.adapter.CategoryAllBrandAdapter;
import cn.bjsxt.youhuo.util.JsonUtils;

/**
 * 品牌分类下的scrollView
 * 1.广告轮播 recyclerView 在scrollView中处理
 * 2.展示所有品牌的listView放在父类中处理（categoryPinPai） 需要和右边的字母索引导航交互
 */
public class PinPaiScrollView extends ScrollView {

    private Context context;
    /**
     * PinPaiScrollView 的唯一子节点
     */
    private LinearLayout rootChild;
    /**
     * 广告轮播
     */
    private LinearLayout advert;
    /**
     * 热门品牌 （recycleView）
     */
    private LinearLayout hotLinear;
    /**
     * 所有品牌
     */
    private ListView allBrandLv;
    private JsonUtils jsonUtils;
    private AdvertView advertView;
    /**
     * 搜索
     */
    private View search;
    /**
     * 搜索editText
     */
    private EditText searchEdt;
    /**
     * 取消的分割线
     */
    private View searchLine;
    /**
     * 取消按钮
     */
    private TextView searchTv;
    /**
     * 搜索按钮
     */
    private ImageButton searchIb;
    private TextView hotTitle;
    /**
     * 保存所有品牌的适配器
     */
    private CategoryAllBrandAdapter allBrandAdapter;

    public PinPaiScrollView(Context context) {
        this(context, null);
    }

    public PinPaiScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initViews();
        initUtils();
        initSearch();
    }

    /**
     * 初始化搜索
     */
    public void initSearch(){
        searchEdt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setVisibleGone();
            }
        });
        searchTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setVisible();
            }
        });
    }

    private void setVisible() {
        CategoryPinPaiView.setTopOptionsVisible();
        advert.setVisibility(VISIBLE);
        hotTitle.setVisibility(VISIBLE);
        hotLinear.setVisibility(VISIBLE);
        allBrandLv.setVisibility(VISIBLE);
        searchLine.setVisibility(GONE);
        searchTv.setVisibility(GONE);
    }

    private void setVisibleGone() {
        CategoryPinPaiView.setTopOptionsGone();
        advert.setVisibility(GONE);
        hotTitle.setVisibility(GONE);
        hotLinear.setVisibility(GONE);
        allBrandLv.setVisibility(GONE);
        searchLine.setVisibility(VISIBLE);
        searchTv.setVisibility(VISIBLE);
    }

    /**
     * 初始化一些工具类
     */
    private void initUtils() {
        jsonUtils = new JsonUtils();
    }

    /**
     * 初始化控件
     */
    private void initViews() {
        //添加PinPaiScrollView子节点（scrollView只能有一个子节点）
        rootChild = new LinearLayout(context);
        //设置rootChild的属性  scrollView继承自frameLayout
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        rootChild.setLayoutParams(params);
        rootChild.setOrientation(LinearLayout.VERTICAL);
        this.addView(rootChild);

        //填充布局
        View view = View.inflate(context, R.layout.view_category_pinpai_boy, null);
        advert = (LinearLayout) view.findViewById(R.id.category_pinPai_boy_advert);
        hotLinear = (LinearLayout) view.findViewById(R.id.category_pinPai_boy_hot);
        hotTitle = (TextView) view.findViewById(R.id.category_pinPai_boy_hotTitle);
        allBrandLv = (ListView) view.findViewById(R.id.category_pinPai_boy_lv);
        allBrandLv.setDividerHeight(0);
        //初始化搜索控件
        search = view.findViewById(R.id.category_pinPai_boy_search);
        searchEdt = (EditText) search.findViewById(R.id.category_search_edt);
        searchEdt.clearFocus();
        searchEdt.setEnabled(false);
        searchLine = search.findViewById(R.id.category_search_line);
        searchTv = (TextView) search.findViewById(R.id.category_search_cancel);
        searchIb = (ImageButton) search.findViewById(R.id.category_search_ib);
        searchIb.setEnabled(false);
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        rootChild.addView(view);
    }

    /**
     * 处理广告返回的结果
     *
     * @param result 从文件读取/从网络获取的数据
     */
    public void handleAdvert(String result) {
        List<AdvertInfo> advertParseJson = jsonUtils.getAdvertParseJson(result);
        Log.e("advertParseJson", advertParseJson.toString());
        //呈现广告
        advertView = new AdvertView(context, advertParseJson);
        advert.addView(advertView.getViewContent());
        //开启线程 实现轮播
        advertView.startAdverThread();
    }

    public void stopAdverThread() {
        if (advertView != null) {
            advertView.stopAdverThread();
        }
    }

    /**
     * 处理 热门品牌返回的结果
     *
     * @param result 从内存读取/从网络获取的数据
     */
    public void handleHotBrand(String result) {
        List<CategoryHotBrandBean> hotBrandList = jsonUtils.getHotBrandToJson(result);
        Log.e("hotBrandList", hotBrandList.toString());
        //创建recyclerView对象
        RecyclerView recyclerView = new RecyclerView(context);
        //凡是new 出来的view都需要设置layoutParams
        recyclerView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        //recyclerView 必须设置layoutManger
        LinearLayoutManager manager = new LinearLayoutManager(context);
        //设置水平（）默认
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(manager);
        //设置适配器
        CategoryHotBrandAdapter hotBrandAdapter = new CategoryHotBrandAdapter(context, hotBrandList);
        recyclerView.setAdapter(hotBrandAdapter);
        //将recyclerView添加至linearLayout中
        hotLinear.addView(recyclerView);
    }

    /**
     * 计算listView的高度 设置适配器 渲染数据
     *
     * @param allBrandAdapter 适配器
     */
    public void setAllBrandAdapter(final CategoryAllBrandAdapter allBrandAdapter) {
        this.allBrandAdapter = allBrandAdapter;
        allBrandLv.setAdapter(allBrandAdapter);
        measureListViewHeight(allBrandAdapter);
        allBrandLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CategoryAllBrandBean item = allBrandAdapter.getItem(position);
                String brandId = item.get_id();
                Intent intent = new Intent(context, BrandGoodsActivity.class);
                intent.putExtra("brandId",brandId);
                context.startActivity(intent);
            }
        });
    }

    /**
     * 计算listView的高度
     * listView的高度 = 所有listView的item的高度 + 每个线的高度 + paddingTop + paddingBottom
     *
     * @param allBrandAdapter
     */
    private void measureListViewHeight(CategoryAllBrandAdapter allBrandAdapter) {
        //获取所有item
        int count = allBrandAdapter.getCount();
        //初始化listView的高低
        int lvHeight = 0;
        //计算每个item的高度
        for (int i = 0; i < count; i++) {
            View itemView = allBrandAdapter.getView(i, null, null);
            //清空之前的测量
            itemView.measure(0, 0);
            lvHeight += itemView.getMeasuredHeight();
        }
        //添加所有线的高度和padding
        lvHeight += allBrandLv.getDividerHeight() * (count - 1) + allBrandLv.getPaddingTop() + allBrandLv.getPaddingBottom();
        //把高度设置给listView
        allBrandLv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, lvHeight));
        Log.e("高度", lvHeight + "");
        //重绘
        invalidate();
    }

    /**
     * 根据所有品牌要显示的位置 动态计算scrollView要移动的距离
     * 移动的距离 = 搜索的框的高度 + 广告的高度 + 热门字体标签的高度 + 热门品牌的（recyclerView）高度 + position之前所有item的高度 + position之前所有线以及paddingTop的高度
     * @param allBrandPosition 所有品牌要显示的item
     */
    public void setScrollPosition(int allBrandPosition) {
        int scrollY = 0;
        scrollY += search.getMeasuredHeight();//加搜索框高度
        scrollY += advert.getMeasuredHeight();//加广告的高度
        scrollY += hotTitle.getMeasuredHeight();//加热门标题的高度
        scrollY += hotLinear.getMeasuredHeight();//加热门品牌的（recyclerView）高度
        for (int i = 0; i < allBrandPosition; i++) {
            View view = allBrandAdapter.getView(i, null, null);
            //设置父级不限定 否则测量结果为0
            view.measure(0,0);
            //加position之前所有item的高度
            scrollY += view.getMeasuredHeight();
        }
        //加position之前所有线以及paddingTop的高度
        scrollY += allBrandLv.getDividerHeight() * (allBrandPosition -1) + allBrandLv.getPaddingTop();
        this.scrollTo(0,scrollY);
    }
}

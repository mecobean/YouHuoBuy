package cn.bjsxt.youhuo.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import cn.bjsxt.youhuo.R;
import cn.bjsxt.youhuo.adapter.BasePagerAdapter;
import cn.bjsxt.youhuo.adapter.GoodsDetailListViewAdapter;
import cn.bjsxt.youhuo.bean.GoodsDetailBean;
import cn.bjsxt.youhuo.util.HttpModel;
import cn.bjsxt.youhuo.util.LogUtil;

/**
 * 自定义控件
 */
public class GoodsDetailScrollView extends ScrollView implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private final Context context;
    private ViewPager mViewPager;//viewPage
    private TextView goodsName;
    private TextView goodsPrice;
    private TextView goodsDiscount;
    private View unfoldGroup;//折扣
    private ImageView unfold;//折扣展开
    private ImageView fold;//折扣未展开
    private ListView detailLv;//listVIew
    /**
     * viewPager的图片路径集合
     */
    private List<String> imgList;
    /**
     * listView的图片路径集合
     */
    private List<String> imgvalueList;
    /**
     * 所有数据的实体类
     */
    private GoodsDetailBean goodsDetailBean;
    /**
     * viewPager的数据集合
     */
    private List<View> vpList = new ArrayList<>();
    /**
     * viewPager 上的点的父布局
     */
    private LinearLayout dotLinear;
    private GoodsDetailListViewAdapter listViewAdapter;
    private int top;

    public GoodsDetailScrollView(Context context) {
        this(context, null);
    }

    public GoodsDetailScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    private void initViews() {
        //添加子节点
        LinearLayout rootChild = new LinearLayout(context);
        //设置rootChild的属性  scrollView继承自frameLayout
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        rootChild.setLayoutParams(params);
        rootChild.setOrientation(LinearLayout.VERTICAL);
        this.addView(rootChild);

        //填充布局
        View view = View.inflate(context, R.layout.view_goods_detail_scroll, null);
        mViewPager = (ViewPager) view.findViewById(R.id.goods_detail_scrollView_vp);
        dotLinear = (LinearLayout) view.findViewById(R.id.goods_detail_dotLinear);

        goodsName = (TextView) view.findViewById(R.id.goods_detail_scrollView_title);
        goodsPrice = (TextView) view.findViewById(R.id.goods_detail_price);
        goodsDiscount = (TextView) view.findViewById(R.id.goods_detail_discount);

        unfoldGroup = view.findViewById(R.id.goods_detail_discount_unfold_group);
        unfold = (ImageView) view.findViewById(R.id.goods_detail_discount_unfold);
        fold = (ImageView) view.findViewById(R.id.goods_detail_discount_fold);

        detailLv = (ListView) view.findViewById(R.id.goods_detail_lv);
        rootChild.addView(view);
        initListener();
    }

    /**
     * 动态改变点的颜色
     */
    private void initDotsRes(int position) {
        for (int i = 0; i < imgList.size(); i++) {
            View childImg = dotLinear.getChildAt(i);
            if (position == i){//选中
                childImg.setBackgroundResource(R.mipmap.tabmain_dot_icon_unselect);
            }else {//未选中
                childImg.setBackgroundResource(R.mipmap.tabmain_dot_icon);
            }
        }
    }

    /**
     * 初始化viewpager上的点
     */
    private void initVPDots() {
        for (int i = 0; i < imgList.size(); i++) {
            ImageView iv = new ImageView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 10;
            params.rightMargin = 10;
            iv.setLayoutParams(params);
            dotLinear.addView(iv);
        }
    }

    /**
     * 初始化viewpager的数据
     */
    private void initVPData() {
        for (int i = 0; i < imgList.size(); i++) {
            ImageView iv = new ImageView(context);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            Picasso.with(context).load(HttpModel.IMGURL + imgList.get(i)).placeholder(R.mipmap.share_loading).into(iv);
            vpList.add(iv);
        }
        ImageView imageView = new ImageView(context);
        imageView.setImageResource(R.mipmap.look);
        vpList.add(imageView);
    }

    private void initListener() {
        fold.setOnClickListener(this);
        unfold.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.goods_detail_discount_unfold:
                unfoldGroup.setVisibility(GONE);
                fold.setVisibility(VISIBLE);
                break;
            case R.id.goods_detail_discount_fold:
                fold.setVisibility(GONE);
                unfoldGroup.setVisibility(VISIBLE);
                break;

        }
    }

    /**
     * 设置数据源
     *
     * @param goodsDetailBean
     */
    public void setDatas(GoodsDetailBean goodsDetailBean) {
        initViews();
        LogUtil.logE("imgList","imgList"+goodsDetailBean.getImgList().toString());
        LogUtil.logE("imgvalueList","imgvalueList"+goodsDetailBean.getImgvalueList().toString());
        this.goodsDetailBean = goodsDetailBean;
        goodsName.setText(goodsDetailBean.getTitle());
        goodsPrice.setText("￥"+goodsDetailBean.getPrice());
        String value = "￥"+(Double.parseDouble(goodsDetailBean.getDiscount())+Double.parseDouble(goodsDetailBean.getPrice()));
        //设置中划线
        SpannableString spannableString = new SpannableString(value);
        spannableString.setSpan(new StrikethroughSpan(),0,value.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        goodsDiscount.setText(spannableString);
        imgList = goodsDetailBean.getImgList();
        imgvalueList = goodsDetailBean.getImgvalueList();
        //渲染listview的数据
        if (listViewAdapter == null){
        listViewAdapter = new GoodsDetailListViewAdapter(context,imgvalueList);
        }
        detailLv.setAdapter(listViewAdapter);
        measureListViewHeight();
        //移动到最顶端
        detailLv.post(new Runnable() {
            @Override
            public void run() {
                scrollTo(0,0);
            }
        });
        //渲染viewPager的数据
        if (imgList != null && imgList.size()>0){
            initVPData();
            initVPDots();
        }
        BasePagerAdapter<View> pagerAdapter = new BasePagerAdapter<View>(context,vpList);
        mViewPager.setAdapter(pagerAdapter);
        initDotsRes(0);
        mViewPager.addOnPageChangeListener(this);
    }

    /**
     * 测量listView的高度
     * 所有item的高度+线的高度+paddingTop +paddingBottom
     */
    private void measureListViewHeight() {
        int count = listViewAdapter.getCount();
        int lvHeight = 0;
        for (int i = 0; i < count; i++) {
            View childItem = listViewAdapter.getView(i,null,null);
            childItem.measure(0,0);//设置父级不限定
            lvHeight += childItem.getMeasuredHeight();
        }
        lvHeight += detailLv.getDividerHeight()*(count -1)+detailLv.getPaddingTop()+detailLv.getPaddingBottom();
        //给listView设置上去
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,lvHeight);
        detailLv.setLayoutParams(params);
        LogUtil.logE(LogUtil.DEBUG_TAG,"listView的高度"+lvHeight);
//        requestLayout();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position>imgList.size()-1){
            position = imgList.size()-1;
            smoothScrollTo(0,top);
            mViewPager.setCurrentItem(position);
        }
        initDotsRes(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (detailLv != null){
            //拿到listView上边的view的高度
            top = detailLv.getTop();
            LogUtil.logE(LogUtil.DEBUG_TAG,"top的高度"+top);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 松手的时候判断滑动的距离 当listView未显示时候，向上拖动查看更多可见的时候判断向上滑动的距离是否大于top-屏幕的高度
     * @param ev
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                int Y = top - getScrollY();
                if (Y>getHeight()/2 &&getScrollY() > top-getHeight()){//向上滑动可见且向上滑动的距离大于屏幕高度的三分之一    显示listView
                    smoothScrollTo(0,top-getHeight());
                    return true;
                }else if (Y<getHeight()/2 && getScrollY()<top){
                    smoothScrollTo(0,top);
                    return true;
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 解决抛动的时候 拦截onTouch的松手事件
     * @param velocityY 速度 [向上抛动值大于零 向下抛动值小于零]
     */
    @Override
    public void fling(int velocityY) {
        super.fling(velocityY);
        LogUtil.logE("抛动",velocityY+"");
        if (velocityY>500 && getScrollY()<=top-getHeight()){
            smoothScrollTo(0,top-getHeight());
        }else if (velocityY<-500 && getScrollY()>=top){
            smoothScrollTo(0,top);
        }
    }
}

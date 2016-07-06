package cn.bjsxt.youhuo.view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;

import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import cn.bjsxt.youhuo.R;
import cn.bjsxt.youhuo.util.ToastUtils;

/**
 * 自定义listView 可上下拉刷新
 */
public class PullAndPushListView extends ListView {

    private View headView;
    private View footerView;
    private ImageView headIv;
    private int measuredHeight;
    private ImageView footerIv;
    private int y;
    /**
     * 刷新数据接口
     */
    private OnRefreshListener refreshListener;
    /**
     * 下拉刷新的标记 防止反复刷新
     */
    public boolean isPullDownRefresh = false;
    /**
     * 上啦加载的标记 反之反复操作
     */
    public boolean isPullUpRefresh = false;
    /**
     * 判断事件拦截
     * 判断是否向下滑动的开始X位置
     */
    private int startX;
    /**
     * 判断事件拦截
     * 判断是否向下滑动的开始Y位置
     */
    private int startY;
    /**
     * 是否有头部的标记
     */
    private boolean hasHead = true;
    /**
     * 是否有尾部的标记
     */
    private boolean hasFooter = true;
    /**
     * 上拉加载所有数据 完成后显示的标识
     */
    private View endTv;

    public PullAndPushListView(Context context) {
        this(context, null);
    }

    public PullAndPushListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullAndPushListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化头部和尾部的上下拉的刷新控件
     */
    private void init() {
        //初始化头部和尾部
        headView = View.inflate(getContext(), R.layout.pull_push_item, null);
        footerView = View.inflate(getContext(), R.layout.pull_push_item, null);

        endTv = footerView.findViewById(R.id.pullAndPush_Tv);

        headIv = (ImageView) headView.findViewById(R.id.pullAndPush_iv);
        footerIv = (ImageView) footerView.findViewById(R.id.pullAndPush_iv);
        //测量高度
        headView.measure(0, 0);
        measuredHeight = headView.getMeasuredHeight();
        //设置头部 和尾部隐藏
        headView.setPadding(0, -measuredHeight, 0, 0);
        footerView.setPadding(0, 0, 0, -measuredHeight);
        //listView添加头部和尾部
        addHeaderView(headView, null, false);
        addFooterView(footerView, null, false);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //拿到按下的当前按下的Y轴位置
                y = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                //下拉刷新的条件 listView的第一个item可见 并且向下移动
                int minY = (int) ((ev.getRawY() - y) / 2);
                if (isPullDownRefresh || isPullUpRefresh) {
                    break;
                }
                if (minY > 0 && getFirstVisiblePosition() == 0 && hasHead) {
                    //动态改变headView的padding
                    int i = -measuredHeight + minY;
                    headView.setPadding(0, Math.min(i, measuredHeight), 0, 0);
                    //   return true;
                    if (headView.getPaddingTop() >= 0) {
                        setSelection(0);
                        break;
                    }
                } else if (minY < 0 && getLastVisiblePosition() == getCount() - 1 && hasFooter) {
                    //最后一个item可见 并且向上滑动
                    int i = -measuredHeight - minY;
                    footerView.setPadding(0, 0, 0, Math.min(i, measuredHeight));
                    if (footerView.getPaddingBottom() >= 0) {
                    //  footerView.setPadding(0, 0, 0, 0);
                        setSelection(getCount());
                        break;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                //headView 还没有完全显示出来 并且listView的paddingTop大于headView的paddingTop的初始值
                if (hasHead) {
                    if (headView.getPaddingTop() < 0 && headView.getPaddingTop() > -measuredHeight) {
                        //松手后不显示headView
                        headView.setPadding(0, -measuredHeight, 0, 0);
                    } else if (headView.getPaddingTop() >= 0) {//头部已经完全显示出来
                        //开启动画 加载数据
                        pullDownRefresh();
                    }
                }
                if (hasFooter) {
                    if (footerView.getPaddingBottom() < 0 && footerView.getPaddingBottom() > -measuredHeight) {
                        //松手后不显示footerView
                        footerView.setPadding(0, 0, 0, -measuredHeight);
                    } else if (footerView.getPaddingBottom() >= 0) {
                        //尾部完全显示出来
                        //开启动画加载数据
                        footerView.setPadding(0, 0, 0, 0);
                        if (refreshListener != null && !isPullUpRefresh) {
                            footerIv.setImageResource(R.drawable.pull_push_animation);
                            AnimationDrawable footerAnimation = (AnimationDrawable) footerIv.getDrawable();
                            footerAnimation.start();
                            isPullUpRefresh = true;
                            refreshListener.pullUpRefresh();
                        }
                    }
                }
                break;
        }

        return super.onTouchEvent(ev);
    }

    /**
     * 开启动画 加载数据
     */
    public void pullDownRefresh() {
        if (hasHead) {
            headView.setPadding(0, 0, 0, 0);
            if (refreshListener != null && !isPullDownRefresh) {
                headIv.setImageResource(R.drawable.pull_push_animation);
                AnimationDrawable headAnimation = (AnimationDrawable) headIv.getDrawable();
                headAnimation.start();
                isPullDownRefresh = true;
                refreshListener.pullDownRefresh();
            }
        }
    }

    //接口回调 上下拉刷新加载数据
    public interface OnRefreshListener {
        /**
         * 下拉刷新加载数据
         */
        void pullDownRefresh();

        /**
         * 上拉加载加载数据
         */
        void pullUpRefresh();
    }

    public void setOnRefreshListener(OnRefreshListener refreshListener) {
        this.refreshListener = refreshListener;
    }

    /**
     * 下拉刷新数据加载成功
     */
    public void pullDownRefreshSuccess() {
        headIv.setImageResource(R.mipmap.icon_loaing_frame_1);
        headView.setPadding(0, -measuredHeight, 0, 0);
        isPullDownRefresh = false;
    }

    /**
     * 上拉加载数据成功
     */
    public void pullUpRefreshSuccess() {
        footerIv.setImageResource(R.mipmap.icon_loaing_frame_1);
        footerView.setPadding(0, 0, 0, -measuredHeight);
        isPullUpRefresh = false;
    }

    /**
     * 每次按下的移动的时候 如果拦截了移动事件，那么这个移动事件将交给本类的
     * onTouch的move事件处理，那么在拦截的时候需要把移动的距离赋值给onTouch的move处理
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = (int) ev.getRawX();
                startY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int minX = (int) (ev.getRawX() - startX);
                int minY = (int) (ev.getRawY() - startY);
                if (Math.abs(minY) > Math.abs(minX)) {
                    y = (int) ev.getRawY();
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    /**
     * 设置是否显示下拉刷新的头部
     *
     * @param hasHead
     */
    public void setHead(boolean hasHead) {
        this.hasHead = hasHead;
    }

    /**
     * 设置是否显示
     *
     * @param hasFooter
     */
    public void setFooter(boolean hasFooter) {
        this.hasFooter = hasFooter;
    }

    /**
     * 上啦加载没有数据 显示标识
     */
    public void setEndView(){
        hasFooter = false;
        footerIv.setVisibility(GONE);
        endTv.setVisibility(VISIBLE);
        isPullUpRefresh = false;
        footerView.setPadding(0,0,0,0);
    }
}

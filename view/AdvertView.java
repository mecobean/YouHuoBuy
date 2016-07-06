package cn.bjsxt.youhuo.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import cn.bjsxt.youhuo.R;
import cn.bjsxt.youhuo.bean.AdvertInfo;
import cn.bjsxt.youhuo.util.HttpModel;
import cn.bjsxt.youhuo.util.LogUtil;

/**
 * 自定义组合控件实现广告轮播效果
 */
public class AdvertView implements ViewPager.OnPageChangeListener {
    private Context context;
    private List<AdvertInfo> list;
    private RelativeLayout mRelative;
    private ViewPager vp;
    private LinearLayout mLinear;
    private List<ImageView> imageList = new ArrayList<>();
    private boolean isAlive = true;
    private boolean isRunning = true;
    private int currentPosition = 0;
    private MyAdverHandler handler = new MyAdverHandler();
    private MyAdvertThread thread;
    private MyVpAdapter adapter;

    public AdvertView(Context context, List<AdvertInfo> list) {
        this.context = context;
        this.list = list;
    }

    class MyAdverHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == -1 && isAlive) {
                vp.setCurrentItem(++currentPosition);
            }
        }
    }

    /**
     * 对外界提供拿到这个组合控件的方法
     *
     * @return 返回这个组合控件
     */
    public RelativeLayout getViewContent() {
        initViews();
        return mRelative;
    }

    /**
     * 对外提供开始图片轮播的方法
     */
    public void startAdverThread() {
        if (thread == null) {
            thread = new MyAdvertThread();
            thread.start();
        }
    }

    /**
     * 对外提供停止图片轮播的方法
     */
    public void stopAdverThread() {
        isAlive = false;
        isRunning = false;
        thread = null;
    }

    /**
     * viewPager + linearLayout（添加小圆点）
     */
    private void initViews() {
        //最外层的布局
        mRelative = new RelativeLayout(context);
        //viewpager
        vp = new ViewPager(context);
        vp.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        vp.addOnPageChangeListener(this);
        mRelative.addView(vp);
        //小圆点
        mLinear = new LinearLayout(context);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
//        params.bottomMargin = 20;
        mLinear.setLayoutParams(params);
        mLinear.setOrientation(LinearLayout.HORIZONTAL);
        mRelative.addView(mLinear);

        //动态添加小圆点
        createDotPoint();
        //获取ViewPager显示的图片
        createViewPagerList();
        adapter = new MyVpAdapter();
        vp.setAdapter(adapter);
        //设置默认选中
        vp.setCurrentItem(list.size() * 400);
        currentPosition = vp.getCurrentItem();
    }

    /**
     * 创建在viewPager中显示的image
     */
    private void createViewPagerList() {
        for (int i = 0; i < list.size(); i++) {
            ImageView img = new ImageView(context);
            img.setScaleType(ImageView.ScaleType.FIT_XY);
            //网络加载图片 再缓存，，，用picasso类库
            Picasso.with(context).load(HttpModel.IMGURL + list.get(i).getImgpath()).placeholder(R.mipmap.share_loading).into(img);
            LogUtil.logE(LogUtil.INFO_TAG, list.get(i).getImgpath());
            imageList.add(img);
        }
    }

    /**
     * 根据list的大小动态添加小圆点
     */
    private void createDotPoint() {
        for (int i = 0; i < list.size(); i++) {
            ImageView dot = new ImageView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 10;
            params.rightMargin = 10;
            params.bottomMargin = 20;
            dot.setLayoutParams(params);
            //将小圆点添加至linearLayout中
            mLinear.addView(dot);
        }
        checkDotPoint(0);
    }

    /**
     * 更新图片轮播
     * @param list 数据源
     */
    public void setNotify(List<AdvertInfo> list) {
        if (list.size() > 3) {
            this.list.clear();
            this.list.addAll(list);
            adapter.notifyDataSetChanged();
        }

    }

    /**
     * 设置小圆点颜色
     *
     * @param position 当前小圆点的位置
     */
    private void checkDotPoint(int position) {
        for (int i = 0; i < list.size(); i++) {
            ImageView imgChild = (ImageView) mLinear.getChildAt(i);
            if (i == position) {//选中
                imgChild.setImageResource(R.mipmap.tabmain_dot_icon_unselect);
            } else {//没选中
                imgChild.setImageResource(R.mipmap.tabmain_dot_icon);
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    /**
     * 滑动的时候关联小圆点
     *
     * @param position
     */
    @Override
    public void onPageSelected(int position) {
        currentPosition = position;
        checkDotPoint(position % list.size());
    }

    /**
     * 判断设置是否自动滑动
     *
     * @param state
     */
    @Override
    public void onPageScrollStateChanged(int state) {
        switch (state) {
            case ViewPager.SCROLL_STATE_DRAGGING:
                LogUtil.logE(LogUtil.INFO_TAG, "SCROLL_STATE_DRAGGING");
                isRunning = false;
                break;
            case ViewPager.SCROLL_STATE_SETTLING:
                LogUtil.logE(LogUtil.INFO_TAG, "SCROLL_STATE_SETTLING");
                break;
            case ViewPager.SCROLL_STATE_IDLE:
                LogUtil.logE(LogUtil.INFO_TAG, "SCROLL_STATE_IDLE");
                isRunning = true;
                break;

        }
    }

    class MyVpAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = imageList.get(position % list.size());
            ViewGroup parent = (ViewGroup) imageView.getParent();
            if (parent != null){
                parent.removeView(imageView);
            }
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            super.destroyItem(container, position, object);
//            container.removeView((View) object);
            container.removeView(imageList.get(position % list.size()));
        }
    }

    class MyAdvertThread extends Thread {
        @Override
        public void run() {
            super.run();
            //无限轮播是死循环
            while (isAlive) {//线程是否活着
                if (isRunning) {//是否用户手动滑动
                    handler.sendEmptyMessage(-1);
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}

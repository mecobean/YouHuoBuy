package cn.bjsxt.youhuo.view;

import android.content.Context;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.bjsxt.youhuo.R;
import cn.bjsxt.youhuo.bean.CategoryAllBrandBean;
import cn.bjsxt.youhuo.adapter.CategoryAllBrandAdapter;
import cn.bjsxt.youhuo.util.FileUtils;
import cn.bjsxt.youhuo.util.HttpHandler;
import cn.bjsxt.youhuo.util.HttpModel;
import cn.bjsxt.youhuo.util.HttpThread;
import cn.bjsxt.youhuo.util.JsonUtils;
import cn.bjsxt.youhuo.util.ToastUtils;

/**
 * categoryFragment 分类下品牌分类选项卡
 * 1.复用HomeFragment中的自定义组合控件广告轮播
 * 2.使用recyclerView实现热门品牌的水平方向展示
 * 3.使用listView展示所有的品牌按照字母的顺序排列展示
 * TODO 把所有的控件放入scrollView中，实现上下滑动
 * *测量listView的高度 解决listView与scrollView的事件冲突
 * * 1.广告轮播 recyclerView 在scrollView中处理
 * 2.展示所有品牌的listView放在父类中处理（categoryPinPai） 需要和右边的字母索引导航交互
 */
public class CategoryPinPaiView {
    private Context context;
    /**
     * 品牌分类的根布局
     */
    private LinearLayout rootView;
    /**
     * 自定义scrollView
     */
    private PinPaiScrollView mScrollView;
    /**
     * 自定义View 右侧的字母索引控件
     */
    private PinPaiIndexView mIndexView;
    /**
     * 顶部的选型卡
     */
    private static View topOptions;
    private FileUtils fileUtils;
    private MyHandler handler;
    private JsonUtils jsonUtils;
    /**
     * 所有品牌  的适配器
     */
    private CategoryAllBrandAdapter allBrandAdapter;
    /**
     * 所有品牌的数据集合
     */
    private List<CategoryAllBrandBean> allBrandToJson;

    public CategoryPinPaiView(Context context) {
        this.context = context;
        init();
    }

    class MyHandler extends HttpHandler {

        public MyHandler(Context context) {
            super(context);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HttpHandler.CATEGORY_BRAND_ADBERT_SUCCESS://广告的数据正常返回
                    mScrollView.handleAdvert(msg.obj.toString());
                    break;
                case HttpHandler.CATEGORY_HOT_BRAND_SUCCESS://热门品牌的数据正常返回
                    Log.e("CATEGORY_HOT_BRAND_SUCCESS", msg.obj.toString());
                    mScrollView.handleHotBrand(msg.obj.toString());
                    break;
                case HttpHandler.CATEGORY_ALL_BRAND_SUCCESS://所有品牌的数据正常返回
                    Log.e("全部广告",msg.obj.toString());
                    handlerResultAllBrand(msg.obj.toString());
                    break;
            }
        }
    }

    /**
     * 处理所有品牌数据正确返回的数据
     * @param result
     */
    private void handlerResultAllBrand(String result) {
        allBrandToJson = jsonUtils.getAllBrandToJson(result);
        //将解析的数据进行按字母的顺序排序
        Collections.sort(allBrandToJson, new Comparator<CategoryAllBrandBean>() {
            @Override
            public int compare(CategoryAllBrandBean lhs, CategoryAllBrandBean rhs) {
                return lhs.getLetter().compareTo(rhs.getLetter());
            }
        });
        allBrandAdapter = new CategoryAllBrandAdapter(context, allBrandToJson);
        mScrollView.setAllBrandAdapter(allBrandAdapter);

//        获取letterList数据
        List<String> letterList = allBrandAdapter.getLetterList();
        //把数据提供给自定义view绘制
        mIndexView.setLetterList(letterList);
        //实现回调
        mIndexView.setOnListViewPositionListener(new PinPaiIndexView.onListViewPositionListener() {
            @Override
            public void scrollPosition(String text) {
                ToastUtils.toast(text);
                //通过letter按下的text 拿到allBrandList相对应的item的position
                int allBrandPosition = -1;
                for (int i = 0; i < allBrandToJson.size(); i++) {
                    if (text.equals(allBrandToJson.get(i).getLetter())){
                        allBrandPosition = i;
                        break;
                    }
                }
            //根据position让scrollView移动相应的距离
                mScrollView.setScrollPosition(allBrandPosition);
            }
        });
    }


    /**
     * 初始化
     */
    private void init() {
        initUtils();
        initViews();
        initAdvert();
        initHotBrand();
        initAllBrand();
        initListener();
    }

    /**
     * 初始化所有品牌数据
     */
    private void initAllBrand() {
        //判断文件是否存在
        if (fileUtils.isSaveFile("data" + HttpHandler.CATEGORY_ALL_BRAND_SUCCESS + ".txt")){
            String result = fileUtils.readCacheJson("data" + HttpHandler.CATEGORY_ALL_BRAND_SUCCESS + ".txt");
            //处理返回结果
            handlerResultAllBrand(result);
        }else {//不存在 从服务器获取
            new HttpThread(context,HttpModel.ALL_BRAND_URL,"parames={\"page\":\"10\"}",handler,HttpHandler.CATEGORY_ALL_BRAND_SUCCESS).start();
        }
    }

    /**
     * 初始化热门品牌
     */
    private void initHotBrand() {
        //如果文件存在
        if (fileUtils.isSaveFile("data" + HttpHandler.CATEGORY_HOT_BRAND_SUCCESS + ".txt")) {
            String result = fileUtils.readCacheJson("data" + HttpHandler.CATEGORY_HOT_BRAND_SUCCESS + ".txt");
            //处理
            mScrollView.handleHotBrand(result);
        } else {//从网络加载
            new HttpThread(context, HttpModel.HOT_BRAND_URL, "", handler, HttpHandler.CATEGORY_HOT_BRAND_SUCCESS).start();
        }
    }

    /**
     * 初始化广告轮播
     */
    private void initAdvert() {
        //判断本地有没有数据
        if (fileUtils.isSaveFile("data" + HttpHandler.CATEGORY_BRAND_ADBERT_SUCCESS + ".txt")) {
            String result = fileUtils.readCacheJson("data" + HttpHandler.CATEGORY_BRAND_ADBERT_SUCCESS + ".txt");
            //处理
            mScrollView.handleAdvert(result);
        } else {
            new HttpThread(context, HttpModel.ADVERTURL, "", handler, HttpHandler.CATEGORY_BRAND_ADBERT_SUCCESS).start();
        }
    }

    /**
     * 初始化所有的监听
     */
    private void initListener() {

    }

    /**
     * 初始化所有的控件
     */
    private void initViews() {
        rootView = (LinearLayout) View.inflate(context, R.layout.view_category_pinpai, null);
        mScrollView = (PinPaiScrollView) rootView.findViewById(R.id.category_pinPai_scrollView);
        mIndexView = (PinPaiIndexView) rootView.findViewById(R.id.category_pinPai_indexView);
        topOptions = rootView.findViewById(R.id.category_pinPai_options);
    }

    /**
     * 初始化用到的工具类
     */
    private void initUtils() {
        fileUtils = new FileUtils(context);
        handler = new MyHandler(context);
        jsonUtils = new JsonUtils();
    }

    /**
     * 对外提供方法 停止广告轮播
     */
    public void stopAdvertThread() {
        if (mScrollView != null) {
            mScrollView.stopAdverThread();
        }

    }

    /**
     * 对外提供方法拿到该组合控件
     *
     * @return 返回该组合控件
     */
    public View getBrandView() {
        if (rootView == null) {
            init();
        }
        return rootView;
    }


    public static void setTopOptionsVisible(){
        topOptions.setVisibility(View.VISIBLE);
    }
    public static void setTopOptionsGone(){
        topOptions.setVisibility(View.GONE);
    }
}

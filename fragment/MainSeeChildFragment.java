package cn.bjsxt.youhuo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cn.bjsxt.youhuo.R;
import cn.bjsxt.youhuo.adapter.SeeChildLvAdapter;
import cn.bjsxt.youhuo.bean.AdvertInfo;
import cn.bjsxt.youhuo.bean.SeeChildBean;
import cn.bjsxt.youhuo.util.FileUtils;
import cn.bjsxt.youhuo.util.HttpHandler;
import cn.bjsxt.youhuo.util.HttpModel;
import cn.bjsxt.youhuo.util.HttpThread;
import cn.bjsxt.youhuo.util.HttpUtil;
import cn.bjsxt.youhuo.util.JsonUtils;
import cn.bjsxt.youhuo.util.LogUtil;
import cn.bjsxt.youhuo.util.TypeValueUtils;
import cn.bjsxt.youhuo.view.AdvertView;
import cn.bjsxt.youhuo.view.PullAndPushListView;

/**
 * seeFragment 的子Fragment
 */
public class MainSeeChildFragment extends BaseFragment implements PullAndPushListView.OnRefreshListener {

    private HttpHandler handler;
    private FileUtils fileUtils;
    /**
     * 第一次进入 广告
     */
    private boolean firsIn_advert = true;
    /**
     * 第一次进入 new
     */
    private boolean firsIn_new = true;
    /**
     * 是否联网请求的数据
     */
    private boolean isNetWork = false;
    private JsonUtils jsonUtils;
    private AdvertView advertView;
    private PullAndPushListView pushListView;
    private TextView header;
    /**
     * listView的数据源
     */
    private List<SeeChildBean.NewsBean> list = new ArrayList<>();
    /**
     * listView的临时数据源
     */
    private List<SeeChildBean.NewsBean> temp = new ArrayList<>();
    /**
     * listView adapter
     */
    private SeeChildLvAdapter childLvAdapter;
    /**
     * 是否上拉加载的数据
     */
    private boolean isPullUp = false;
    private HttpUtil httpUtil;
    /**
     * 下拉 刷新次数
     */
    private int k = 0;
    /**
     * 上拉 加载次数
     */
    private int i = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (pushListView == null) {
            pushListView = new PullAndPushListView(context);
        }
        //头部提示
        header = (TextView) View.inflate(context, R.layout.head_view, null);
        init();
        return pushListView;

    }

    /**
     * 初始化
     */
    private void init() {
        initUtils();
        initDatas();
        initListener();
    }

    /**
     * 初始化一些监听
     */
    private void initListener() {
        pushListView.setOnRefreshListener(this);
        pushListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position ==2 && pushListView.getHeaderViewsCount() >= 3){//点击的是头部提示
                    pushListView.removeHeaderView(header);
                    pushListView.setHead(true);
                    pushListView.pullDownRefreshSuccess();
                    pushListView.isPullDownRefresh = false;
                    //点击更新数据
                    if (temp.size() >0){
                        list.addAll(0,temp);
                        childLvAdapter.notifyDataSetChanged();
                        temp.clear();
                    }
                }
                if (position == pushListView.getCount() -1 && pushListView.getFooterViewsCount() >= 2){
                    //点击的是尾部提示
                    pushListView.removeFooterView(header);
                    pushListView.setFooter(true);
                    pushListView.pullUpRefreshSuccess();
                }
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initDatas() {

        //广告缓存数据
        boolean advert = fileUtils.isSaveFile("data" + HttpHandler.SEE_CHILD_ADVERT_SUCCESS + ".txt");
        if (advert) {
            String s = fileUtils.readCacheJson("data" + HttpHandler.SEE_CHILD_ADVERT_SUCCESS + ".txt");
            if (!TextUtils.isEmpty(s) && firsIn_advert) {//第一次进入 并且本地读取的缓存不为空
                isNetWork = false;
                handlerAdvertResult(s);
            }
        } else {//第一次安装 进入
            firsIn_advert = false;
            isNetWork = true;
            requetAdvertDatas();
        }
        //new 缓存数据
        boolean seeChild = fileUtils.isSaveFile("data" + HttpHandler.SEE_CHILD_SUCCESS + ".txt");
        if (seeChild) {
            String s = fileUtils.readCacheJson("data" + HttpHandler.SEE_CHILD_SUCCESS + ".txt");
            if (!TextUtils.isEmpty(s) && firsIn_new) {//第一次进入 并且本地读取的缓存不为空
                isNetWork = false;
                handlerNewResult(s);
            }
        } else {//第一次安装 进入
            firsIn_new = false;
            isNetWork = true;
            requetNewDatas();
        }


    }

    /**
     * 处理广告返回数据
     *
     * @param result
     */
    private void handlerAdvertResult(String result) {
        List<AdvertInfo> advertParseJson = jsonUtils.getAdvertParseJson(result);
        if (!isNetWork && firsIn_advert) {//第一次进入 从本地读取
            if (advertView == null) {
                advertView = new AdvertView(context, advertParseJson);
                advertView.startAdverThread();
                RelativeLayout viewContent = advertView.getViewContent();
                AbsListView.LayoutParams params = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, TypeValueUtils.dp2px(200));
                viewContent.setLayoutParams(params);
                pushListView.removeHeaderView(viewContent);
                pushListView.addHeaderView(viewContent);
            } else {
                advertView.setNotify(advertParseJson);
            }
            if (advertParseJson.size() > 0) {
                isNetWork = true;
                requetAdvertDatas();
            }
        } else if (isNetWork && firsIn_advert && advertParseJson.size() > 0) {//第一次进入 的二次数据欲加载
            advertView.setNotify(advertParseJson);
            firsIn_advert = false;
        } else {
            if (advertView == null) {
                advertView = new AdvertView(context, advertParseJson);
                advertView.startAdverThread();
                RelativeLayout viewContent = advertView.getViewContent();
                AbsListView.LayoutParams params = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, TypeValueUtils.dp2px(200));
                viewContent.setLayoutParams(params);
                pushListView.removeHeaderView(viewContent);
                pushListView.addHeaderView(viewContent);
            } else {
                advertView.setNotify(advertParseJson);
            }
        }
    }

    /**
     * 处理New 数据返回
     *
     * @param result
     */
    private void handlerNewResult(String result) {
        SeeChildBean seeChildBean = new Gson().fromJson(result, SeeChildBean.class);
        if (HttpModel.SUCCESSFULLY_NO.equals(seeChildBean.getSucess())) {
            header.setText("连接错误");
            pushListView.addHeaderView(header);
        } else {
            if (!isNetWork && firsIn_new) {
                list.clear();
                list.addAll(seeChildBean.getNews());
                childLvAdapter = new SeeChildLvAdapter(context, list);
                pushListView.setAdapter(childLvAdapter);
                //本地加载完 自动从网络网络获取 “一次” 最新数据
                isNetWork = false;
                requetNewDatas();
            } else if (isNetWork && firsIn_new && seeChildBean.getNews().size() >0) { //第一次进入 的二次数据欲加载
                //提示点击更新
                pushListView.removeHeaderView(header);
                header.setText("有" + seeChildBean.getNews().size() + "条数据更新，点击更新");
                pushListView.addHeaderView(header);

                //保存数据
                temp.addAll(seeChildBean.getNews());
                //初始化下拉刷新的头部  点击更新不下拉
                pushListView.pullDownRefreshSuccess();
                pushListView.setHead(false);
                pushListView.isPullDownRefresh = true;

                firsIn_new = false;
            } else {//每次下拉刷新的数据 / 第一次安装 进入加载数据
                if (isPullUp) {
                    list.addAll(seeChildBean.getNews());
                } else {
                    list.addAll(0, seeChildBean.getNews());
                }
                if (childLvAdapter == null) {
                    childLvAdapter = new SeeChildLvAdapter(context, list);
                    pushListView.setAdapter(childLvAdapter);
                } else {
                    childLvAdapter.notifyDataSetChanged();
                }
                pushListView.pullDownRefreshSuccess();
                pushListView.setHead(true);
            }
        }
    }

    /**
     * 请求 new 数据
     */
    private void requetNewDatas() {
        new HttpThread(context, HttpModel.SEE_CHILD_URL, "parames={\"page\":\"1\",\"classes\":\"0\"}", handler, HttpHandler.SEE_CHILD_SUCCESS).start();
    }

    /**
     * 请求广告数据
     */
    private void requetAdvertDatas() {
        new HttpThread(context, HttpModel.ADVERTURL, "", handler, HttpHandler.SEE_CHILD_ADVERT_SUCCESS).start();
    }


    /**
     * 初始化一些工具类
     */
    private void initUtils() {
        handler = new SeeChildHandler(context);
        fileUtils = new FileUtils(context);
        jsonUtils = new JsonUtils();
        httpUtil = new HttpUtil(context);
    }

    /**
     * listView 下拉刷新的回调
     */
    @Override
    public void pullDownRefresh() {
        if (httpUtil.isNetOk()) {
            if (k > 1) {
                addHeadView("暂无最新数据");
                pushListView.pullDownRefreshSuccess();
            } else {
                if (pushListView.getHeaderViewsCount() >= 3) {//当刷新完毕后 点击头部更新后才可以下拉更新
                    pushListView.pullDownRefreshSuccess();
                    pushListView.setHead(false);
//                    pushListView.isPullDownRefresh = true;
                } else {
                    isPullUp = false;//是否上拉加载
                    requetNewDatas();//从网络加载数据
                }
            }
            k++;
        }else {
            addHeadView("网络状态异常");
            pushListView.pullDownRefreshSuccess();
        }
    }

    private void addHeadView(String msg) {
        pushListView.removeHeaderView(header);
        header.setText(msg);
        pushListView.addHeaderView(header);
    }

    /**
     * listView 上拉加载的回调
     */
    @Override
    public void pullUpRefresh() {
        if (httpUtil.isNetOk()) {//有网络
            if (i > 1) {
                pushListView.setEndView();
            } else {
                isPullUp = true;
                requetNewDatas();
                pushListView.pullUpRefreshSuccess();
            }
            i++;
        }else {//无网络
            pushListView.removeFooterView(header);
            header.setText("网络状态异常");
            pushListView.addFooterView(header);
            pushListView.pullUpRefreshSuccess();
        }
    }

    /**
     * handler 通信
     */
    class SeeChildHandler extends HttpHandler {

        public SeeChildHandler(Context context) {
            super(context);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HttpHandler.SEE_CHILD_SUCCESS://new 数据返回
                    handlerNewResult(msg.obj.toString());
                    break;
                case HttpHandler.SEE_CHILD_ADVERT_SUCCESS://广告数据返回
                    handlerAdvertResult(msg.obj.toString());
                    break;
            }
        }
    }
}

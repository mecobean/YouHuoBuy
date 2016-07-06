package cn.bjsxt.youhuo.view;

import android.content.Context;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import cn.bjsxt.youhuo.R;
import cn.bjsxt.youhuo.adapter.GuanZhuAdapter;
import cn.bjsxt.youhuo.bean.GuanZhuInfoBean;
import cn.bjsxt.youhuo.util.FileUtils;
import cn.bjsxt.youhuo.util.HttpHandler;
import cn.bjsxt.youhuo.util.HttpModel;
import cn.bjsxt.youhuo.util.HttpThread;
import cn.bjsxt.youhuo.util.HttpUtil;
import cn.bjsxt.youhuo.util.LogUtil;
import cn.bjsxt.youhuo.util.ToastUtils;

/**
 * 关注view 一个listView
 */
public class CategoryGuanZhuView implements PullAndPushListView.OnRefreshListener {
    private Context context;
    public PullAndPushListView listView;
    private FileUtils fileUtils;
    private HttpHandler handler;
    /**
     * 显示提示的listView 的头部
     */
    private TextView headView;
    /**
     * listView的数据源
     */
    private List<GuanZhuInfoBean.FollowBean> list;
    /**
     * listView的adapter
     */
    private GuanZhuAdapter guanZhuAdapter;
    /**
     * 是否上啦加载
     */
    private boolean isPullUp = false;
    /**
     *
     */
    int i = 0;
    int k = 0;
    private boolean isIntnectRequest = false;
    private boolean firstIn = true;
    /**
     * 临时存储第一次下拉的数据
     */
    private List<GuanZhuInfoBean.FollowBean> temp;
    private HttpUtil httpUtil;

    public CategoryGuanZhuView(Context context) {
        this.context = context;
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        listView = new PullAndPushListView(context);
        list = new ArrayList<>();
        initUtils();
        initViews();
        initDatas();

    }

    /**
     * 初始化控件
     */
    private void initViews() {
        headView = (TextView) View.inflate(context, R.layout.head_view, null);
        listView.setOnRefreshListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1 && listView.getHeaderViewsCount() >=2) {
                    listView.removeHeaderView(headView);
                    listView.setHead(true);
                    listView.pullDownRefreshSuccess();
                    listView.isPullDownRefresh= false;
                    if (temp != null) {
                        list.addAll(0, temp);
                        guanZhuAdapter.notifyDataSetChanged();
                        temp.clear();
                    }
                }
                LogUtil.logE(LogUtil.DEBUG_TAG,"getCount--"+listView.getCount()+"-----getchildcount---"+listView.getChildCount());
                if (position == listView.getCount()-1 && listView.getFooterViewsCount() >= 2){
                    listView.removeFooterView(headView);
                    listView.setFooter(true);
                    listView.pullDownRefreshSuccess();
                }
            }
        });

    }

    /**
     * 初始化listVIew的数据
     */
    private void initDatas() {
        boolean saveFile = fileUtils.isSaveFile("data" + HttpHandler.GUANZHU_SUCCESS + ".txt");
        if (saveFile) {//本地存在
            String s = fileUtils.readCacheJson("data" + HttpHandler.GUANZHU_SUCCESS + ".txt");
            if (!TextUtils.isEmpty(s) && firstIn) {
                isIntnectRequest = false;
                handlerResult(s);
            }
        } else {
            firstIn = false;
            isIntnectRequest = true;
            requestDatas();
        }
    }

    /**
     * 从网络获取数据
     */
    private void requestDatas() {
        new HttpThread(context, HttpModel.GUANZHU_URL, "", handler, HttpHandler.GUANZHU_SUCCESS).start();
    }

    /**
     * 处理本地读取/网络加载返回的json
     *
     * @param result
     */
    private void handlerResult(String result) {
        GuanZhuInfoBean guanZhuInfoBean = new Gson().fromJson(result, GuanZhuInfoBean.class);
        if (guanZhuInfoBean.getSucessfully().equals(HttpModel.SUCCESSFULLY_NO)) {
            headView.setText("网络连接错误");
            listView.addHeaderView(headView, null, true);
//            guanZhuAdapter.notifyDataSetChanged();
        } else {
            if (!isIntnectRequest && firstIn) {//第一次进入 并且从本地读取
                Logger.e("本地读取" + result);
                list.clear();
                list.addAll(guanZhuInfoBean.getFollow());
                if (guanZhuAdapter == null) {
                    guanZhuAdapter = new GuanZhuAdapter(context, list);
                    listView.setAdapter(guanZhuAdapter);
                } else {
                    guanZhuAdapter.notifyDataSetChanged();
                }
                if (list.size() > 0) {
                    isIntnectRequest = true;
                    requestDatas();
                }
            } else if (isIntnectRequest && firstIn && guanZhuInfoBean.getFollow().size()>0){
                //第一次进入 联网请求数据更新提示
                headView.setText("有6条更新，点击更新");
                listView.addHeaderView(headView, null, true);
                //下拉刷新的数据
                temp = guanZhuInfoBean.getFollow();
                listView.pullDownRefreshSuccess();
                listView.setHead(false);
                listView.isPullDownRefresh = true;
                firstIn = false;
            } else {
                if (isPullUp) {//上拉加载的数据
                    list.addAll(guanZhuInfoBean.getFollow());
                } else {//下拉刷新的数据
                    list.addAll(0, guanZhuInfoBean.getFollow());
                }
                if (guanZhuAdapter == null) {
                    guanZhuAdapter = new GuanZhuAdapter(context, list);
                    listView.setAdapter(guanZhuAdapter);
                } else {
                    guanZhuAdapter.notifyDataSetChanged();
                }
                listView.pullDownRefreshSuccess();
                listView.setHead(true);
            }

        }
    }

    /**
     * 初始化一些工具类
     */
    private void initUtils() {
        fileUtils = new FileUtils(context);
        handler = new GuanZhuHandler(context);
        httpUtil = new HttpUtil(context);
    }

    /**
     * 下拉刷新
     */
    @Override
    public void pullDownRefresh() {
        if (httpUtil.isNetOk()) {
            if (k > 1) {
                addHeadView("暂无最新数据");
                listView.pullDownRefreshSuccess();
            } else {
                if (listView.getHeaderViewsCount() >= 2) {//当刷新完毕后 点击头部更新后才可以下拉更新
                    listView.pullDownRefreshSuccess();
                    listView.setHead(false);
                } else {
                    isPullUp = false;//是否上拉加载
                    requestDatas();//从网络加载数据
                }
            }
            k++;
        }else {
            addHeadView("网络状态异常");
            listView.pullDownRefreshSuccess();
        }
    }

    /**
     * 添加头部提示
     * @param msg
     */
    private void addHeadView(String msg) {
        listView.removeHeaderView(headView);
        headView.setText(msg);
        listView.addHeaderView(headView, null, true);
        Logger.e("getHeadcount--" + listView.getHeaderViewsCount());
    }

    /**
     * 上拉加载
     */
    @Override
    public void pullUpRefresh() {
        if (httpUtil.isNetOk()) {
            if (i > 1) {
                listView.setEndView();
            } else {
                isPullUp = true;
                requestDatas();
                listView.pullUpRefreshSuccess();
            }
            i++;
        }else {
            listView.removeFooterView(headView);
            headView.setText("网络状态异常");
            listView.addFooterView(headView,0, true);
            listView.pullUpRefreshSuccess();
        }
    }

    /**
     * handler 类
     */
    class GuanZhuHandler extends HttpHandler {

        public GuanZhuHandler(Context context) {
            super(context);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HttpHandler.GUANZHU_SUCCESS://从网络加载返回的数据
                    handlerResult(msg.obj.toString());
                    break;
                case HttpModel.NO_NETWORK:
                    addHeadView("当前无网络链接");
                    break;
                case HttpModel.UNKNOWN_NETWORK:
                    addHeadView("未知的网络链接");
                    break;
                case HttpModel.ERROR_NETWORK:
                    addHeadView("网络连接错误");
            }
        }
    }

    /**
     * @return 返回这个listVIew
     */
    public PullAndPushListView getGuanZhuView() {
        return listView;
    }
}

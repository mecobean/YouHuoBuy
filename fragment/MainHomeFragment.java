package cn.bjsxt.youhuo.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.xys.libzxing.zxing.activity.CaptureActivity;

import java.util.ArrayList;
import java.util.List;

import cn.bjsxt.youhuo.R;
import cn.bjsxt.youhuo.adapter.AdapterMainGV;
import cn.bjsxt.youhuo.adapter.HomeLvAdapter;
import cn.bjsxt.youhuo.bean.AdvertInfo;
import cn.bjsxt.youhuo.bean.HomeLvItemBean;
import cn.bjsxt.youhuo.bean.HomePageBean;
import cn.bjsxt.youhuo.bean.MainHomeGVBean;
import cn.bjsxt.youhuo.util.FileUtils;
import cn.bjsxt.youhuo.util.HttpHandler;
import cn.bjsxt.youhuo.util.HttpModel;
import cn.bjsxt.youhuo.util.HttpThread;
import cn.bjsxt.youhuo.util.JsonUtils;
import cn.bjsxt.youhuo.util.LogUtil;
import cn.bjsxt.youhuo.util.ToastUtils;
import cn.bjsxt.youhuo.view.AdvertView;
import cn.bjsxt.youhuo.view.MyGridView;
import cn.bjsxt.youhuo.view.PullAndPushListView;

/**
 * 1.顶部logo的动画 ---> 组合动画
 * 2.广告轮播
 * 从服务器获取json解析数据
 * 拼接图片的url
 * 从服务区请求图片
 * 自定义组合空间渲染显示图片
 */
public class MainHomeFragment extends BaseFragment implements View.OnClickListener, Animation.AnimationListener {

    private View rootView;
    private MyGridView gv;
    private ImageView logoE;
    private ImageView logoC;
    private Animation scale1;
    private Animation scale2;
    private RelativeLayout logoGroup;
    private FileUtils fileUtils;
    private MyHandler handler;
    private JsonUtils jsonUtils;
    private RelativeLayout advertGroup;
    private AdvertView advertView;
    private boolean isAnimationStart = false;
    /**
     *
     */
    private boolean start = true;

    private MyAnimationThread myAnimationThread;
    /**
     * listView
     */
    private PullAndPushListView refreshLv;
    /**
     * 广告轮播 gridView 放listView 的头部
     */
    private View headView;
    /**
     * home listView 的数据源
     */
    private List<HomeLvItemBean> homeLvList = new ArrayList<>();
    /**
     * home listView 的适配器
     */
    private HomeLvAdapter homeLvAdapter;
    /**
     * 上啦加载次数
     */
    private int i = 0;
    /**
     * 扫描二维码
     */
    private ImageButton sweep;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initUtils();
        rootView = inflater.inflate(R.layout.fragment_main_home, null);
        headView = View.inflate(context, R.layout.fragment_home_head, null);
        refreshLv = (PullAndPushListView) rootView.findViewById(R.id.refreshLv);
        refreshLv.addHeaderView(headView, null, false);
        getListViewDatas();
        initDatas();
        return rootView;
    }

    /**
     * 拿到listView的数据
     */
    private void getListViewDatas() {
        new HttpThread(context, HttpModel.HOME_URL, "parames={\"shop\":\"1\"}", handler, HttpHandler.HOME_SUCCESS).start();
    }

    /**
     * 初始化所有的数据
     */
    private void initDatas() {
        initViews();
        initAdvertisementDatas();
        initLogoAnimation();
        initGV();
        initListener();

    }

    public class MyHandler extends HttpHandler {

        public MyHandler(Context context) {
            super(context);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HttpHandler.ADVERT_SUCCESS:
                    String result = (String) msg.obj;
                    //解析返回的字符串
                    handlerResult(result);
                    break;
                case -2:
                    //判断当前是那个logo显示，显示的执行压缩动画
                    if (!isAnimationStart) {
                        if (logoC.getVisibility() == View.VISIBLE) {
                            logoC.startAnimation(scale1);
                        } else {
                            logoE.startAnimation(scale1);
                        }
                        isAnimationStart = true;
                    }
                    break;
                case HttpHandler.HOME_SUCCESS://首页数据返回
                    Logger.e("首页数据返回:" + msg.obj.toString());
                    handlerHome(msg.obj.toString());
                    break;

            }
        }
    }

    /**
     * 处理首页数据返回id数据
     *
     * @param result
     */
    private void handlerHome(String result) {
        HomePageBean homePageBean = new Gson().fromJson(result, HomePageBean.class);
        if (homePageBean.getSucessfully().equals(HttpModel.SUCCESSFULLY_NO)) {
            ToastUtils.toast("服务器错误");
        } else if (homePageBean.getSucessfully().equals(HttpModel.SUCCESSFULLY_OK)) {
            List<HomePageBean.BrandBean> brand = homePageBean.getBrand();
            homeLvList.add(getItemBrandList(brand));

            List<HomePageBean.MenBean> men = homePageBean.getMen();
            homeLvList.add(getItemMenList(men));

            List<HomePageBean.MenpantsBean> menpants = homePageBean.getMenpants();
            homeLvList.add(getItemMenPantsList(menpants));

            List<HomePageBean.AccessoriesBean> accessories = homePageBean.getAccessories();
            homeLvList.add(getItemAccessList(accessories));

            List<HomePageBean.OtherBean> other = homePageBean.getOther();
            homeLvList.add(getItemOtherList(other));

            if (homeLvAdapter == null) {
                homeLvAdapter = new HomeLvAdapter(context, homeLvList);
            }
            refreshLv.setAdapter(homeLvAdapter);
            refreshLv.setOnRefreshListener(new PullAndPushListView.OnRefreshListener() {
                @Override
                public void pullDownRefresh() {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
//                            getListViewDatas();
                            refreshLv.pullDownRefreshSuccess();
//                            homeLvAdapter.notifyDataSetChanged();
                            ToastUtils.toast("下拉刷新完毕");
                        }
                    }, 2000);
                }

                @Override
                public void pullUpRefresh() {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            i++;
                            if (i < 3) {
//                                getListViewDatas();
                                refreshLv.pullUpRefreshSuccess();
//                                homeLvAdapter.notifyDataSetChanged();
                                ToastUtils.toast("上拉加载完毕");
                            } else {
                                refreshLv.setEndView();
                            }
                        }
                    }, 2000);
                }
            });
        }
    }

    /**
     * 拿到其他  item数据
     */
    private HomeLvItemBean getItemOtherList(List<HomePageBean.OtherBean> other) {
        List<String> temp = new ArrayList<>();
        for (int i = 0; i < other.size(); i++) {
            HomePageBean.OtherBean otherBean = other.get(i);
            temp.add(otherBean.getImgpath());
        }
        return new HomeLvItemBean(temp.get(0), temp.get(1), temp.get(2), temp.get(3), temp.get(4), temp.get(5));

    }

    /**
     * 拿到accessries item数据
     */
    private HomeLvItemBean getItemAccessList(List<HomePageBean.AccessoriesBean> accessories) {
        List<String> temp = new ArrayList<>();
        for (int i = 0; i < accessories.size(); i++) {
            HomePageBean.AccessoriesBean accessoriesBean = accessories.get(i);
            temp.add(accessoriesBean.getImgpath());
        }
        return new HomeLvItemBean(temp.get(0), temp.get(1), temp.get(2), temp.get(3), temp.get(4), temp.get(5));
    }

    /**
     * 拿到menpants item数据
     */
    private HomeLvItemBean getItemMenPantsList(List<HomePageBean.MenpantsBean> menpants) {
        List<String> temp = new ArrayList<>();
        for (int i = 0; i < menpants.size(); i++) {
            HomePageBean.MenpantsBean menpantsBean = menpants.get(i);
            temp.add(menpantsBean.getImgpath());
        }
        return new HomeLvItemBean(temp.get(0), temp.get(1), temp.get(2), temp.get(3), temp.get(4), temp.get(5));
    }

    /**
     * 拿到menList
     */
    private HomeLvItemBean getItemMenList(List<HomePageBean.MenBean> men) {
        List<String> temp = new ArrayList<>();
        for (int i = 0; i < men.size(); i++) {
            HomePageBean.MenBean menBean = men.get(i);
            temp.add(menBean.getImgpath());
        }
        return new HomeLvItemBean(temp.get(0), temp.get(1), temp.get(2), temp.get(3), temp.get(4), temp.get(5));
    }

    /**
     * 拿到热门品牌item数据
     */
    private HomeLvItemBean getItemBrandList(List<HomePageBean.BrandBean> brand) {
        List<String> temp = new ArrayList<>();
        for (int i = 0; i < brand.size(); i++) {
            HomePageBean.BrandBean brandBean = brand.get(i);
            temp.add(brandBean.getImgpath());
        }
        return new HomeLvItemBean(temp.get(0), temp.get(1), temp.get(2), temp.get(3), temp.get(4), temp.get(5));
    }

    /**
     * 解析返回的字符串
     *
     * @param result 返回advertInfo 集合
     */
    private void handlerResult(String result) {
        List<AdvertInfo> infoList = jsonUtils.getAdvertParseJson(result);
        LogUtil.logE(LogUtil.INFO_TAG, infoList.toString());
        advertView = new AdvertView(getActivity(), infoList);
        RelativeLayout mRelativ = advertView.getViewContent();
        advertGroup.addView(mRelativ);
        advertView.startAdverThread();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (advertView != null)
            advertView.stopAdverThread();
        myAnimationThread = null;
        start = false;
    }

    /**
     * 初始化要使用的工具类
     */
    private void initUtils() {
        fileUtils = new FileUtils(getActivity());
        handler = new MyHandler(getActivity());
        jsonUtils = new JsonUtils();
    }

    /**
     * 初始化广告的显示
     */
    private void initAdvertisementDatas() {
        //1.获取json   --- 判断本地存不存在，不存在就网络访问获取
        if (fileUtils.isSaveFile(HttpModel.CACHE_FILE_NAME)) {//存在
            //直接从内部存储读取
            String result = fileUtils.readCacheJson(HttpModel.CACHE_FILE_NAME);
            //拿到数据发送handlerResult解析
            handlerResult(result);
        } else {//不存在，从网络请求数据
            new HttpThread(getContext(), HttpModel.ADVERTURL, "", handler, HttpHandler.ADVERT_SUCCESS).start();
        }
    }

    /**
     * 初始化监听
     */
    private void initListener() {

        logoGroup.setOnClickListener(this);
        //动画的监听
        scale1.setAnimationListener(this);
        scale2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isAnimationStart = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        sweep.setOnClickListener(this);
    }

    /**
     * 初始化toolBar的image动画，两个动画
     * 1.从完全显示到向中间压缩到不可见
     * 2.从中间不可见到放大原来大小
     */
    private void initLogoAnimation() {
        //压缩动画
        scale1 = new ScaleAnimation(1, 1, 1, 0, Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_SELF, 0.5f);
        scale1.setDuration(400);
        //放大动画
        scale2 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_SELF, 0.5f);
        scale2.setDuration(400);
        myAnimationThread = new MyAnimationThread();
        myAnimationThread.start();
    }

    class MyAnimationThread extends Thread {
        @Override
        public void run() {
            super.run();
            //无限轮播是死循环
            while (start) {//线程是否活着
                if (!isAnimationStart) {//是否用户手动滑动
                    handler.sendEmptyMessage(-2);
                    try {
                        Thread.sleep(8 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 初始化gridView
     */
    private void initGV() {
        List<MainHomeGVBean> list = new ArrayList<>();
        String[] strGv = getResources().getStringArray(R.array.main_home_gv);
        List<Integer> listID = new ArrayList<>();
        listID.add(R.mipmap.btn_xpdz_n);
        listID.add(R.mipmap.btn_qqyx_n);
        listID.add(R.mipmap.btn_cptj);
        listID.add(R.mipmap.btn_qbpl_n);
        listID.add(R.mipmap.btn_dpzn_n);
        listID.add(R.mipmap.btn_qxsc_n);
        listID.add(R.mipmap.btn_mxcp_n);
        listID.add(R.mipmap.btn_zkjx_n);
        for (int i = 0; i < strGv.length; i++) {
            MainHomeGVBean bean = new MainHomeGVBean();
            bean.setStr(strGv[i]);
            bean.setImgID(listID.get(i));
            list.add(bean);
        }
        LogUtil.logE(LogUtil.DEBUG_TAG, "strgv:" + strGv.length + "-->listid:" + listID.size() + "-->list:" + list.size());
        gv.setAdapter(new AdapterMainGV(context, list));
    }

    /**
     * 初始化控件
     */
    private void initViews() {
        gv = (MyGridView) headView.findViewById(R.id.gv_main_home);
        logoE = (ImageView) rootView.findViewById(R.id.toolbar_main_home_logoE);
        logoC = (ImageView) rootView.findViewById(R.id.toolbar_main_home_logoC);
        logoGroup = (RelativeLayout) rootView.findViewById(R.id.toolbar_main_logo);
        advertGroup = (RelativeLayout) rootView.findViewById(R.id.main_home_vp);
        sweep = (ImageButton) rootView.findViewById(R.id.toolbar_main_home_sweep);
    }

    @Override
    public void onClick(View v) {
        //判断当前是那个logo显示，显示的执行压缩动画
        switch (v.getId()) {
            case R.id.toolbar_main_logo:
                //判断当前是那个logo显示，显示的执行压缩动画
                if (!isAnimationStart) {
                    if (logoC.getVisibility() == View.VISIBLE) {
                        logoC.startAnimation(scale1);
                    } else {
                        logoE.startAnimation(scale1);
                    }
                    isAnimationStart = true;
                }
                break;
            case R.id.toolbar_main_home_sweep:
                startActivityForResult(new Intent(context, CaptureActivity.class),1);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != activity.RESULT_OK) return;
        if (requestCode == 1){
            String result = data.getStringExtra("result");
            ToastUtils.toast(result);
        }
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    /**
     * 当第一个动画执行完毕的时候 判断是那个logo是visible就gone，
     * 另一个visible，执行第二个动画i
     *
     * @param animation
     */
    @Override
    public void onAnimationEnd(Animation animation) {
        if (logoE.getVisibility() == View.VISIBLE) {
            logoE.setVisibility(View.GONE);
            logoC.setVisibility(View.VISIBLE);
            logoC.startAnimation(scale2);
        } else {
            logoC.setVisibility(View.GONE);
            logoE.setVisibility(View.VISIBLE);
            logoE.startAnimation(scale2);
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}

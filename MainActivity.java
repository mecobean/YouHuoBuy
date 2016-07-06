package cn.bjsxt.youhuo;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import cn.bjsxt.youhuo.activity.CartActivity;
import cn.bjsxt.youhuo.bean.CartListInfoBean;
import cn.bjsxt.youhuo.bean.VersionBean;
import cn.bjsxt.youhuo.dialog.LoginDialog;
import cn.bjsxt.youhuo.dialog.VersionDialog;
import cn.bjsxt.youhuo.event.DialogLoginSuccess;
import cn.bjsxt.youhuo.fragment.MainCategoryFragment;
import cn.bjsxt.youhuo.fragment.MainHomeFragment;
import cn.bjsxt.youhuo.fragment.MainMineFragment;
import cn.bjsxt.youhuo.fragment.MainSeeFragment;
import cn.bjsxt.youhuo.util.HttpHandler;
import cn.bjsxt.youhuo.util.HttpModel;
import cn.bjsxt.youhuo.util.HttpThread;
import cn.bjsxt.youhuo.util.LogUtil;
import cn.bjsxt.youhuo.util.ToastUtils;
import cn.bjsxt.youhuo.view.MySlidingPaneLayout;
/**
 * boys 主页面
 * 1.fragment 的管理
 * 如果要保存fragment的状态 最好用add hide show ，来管理，如果用replace会每次都创建
 * 操作fragment的时候，如果要找到之前的fragment可使用fragment的tag来实现
 * 通过fragmentManager.findFragmentByTag();方法来找tag
 * 2.沉浸式状态栏
 */
public class MainActivity extends FragmentActivity implements RadioGroup.OnCheckedChangeListener {
    private long lastTime = 0;//用于计算两次点击back按键的时间差
    /**
     * 沉浸式状态栏的父布局
     */
    private RelativeLayout statusBarGroup;
    private android.widget.RadioGroup mainbottomBar;
    /**
     * 填充沉浸式状态栏的view
     */
    private View statusBarChild;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    /**
     * 上次显示的fragment的tag(button的id)
     */
    private int lastFragmentID = 0;
    /**
     * 侧滑菜单
     */
    private MySlidingPaneLayout slidingPane;
    /**
     * 购物车的商品数量
     */
    private TextView cartGoodsNum;
    private HttpHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        initViews();
        handler = new MyMainHandler(this);
        initVersion();
        initStatusBar();
        initListeners();
        fragmentManager = getSupportFragmentManager();
        //默认显示首页
        changeFragment(R.id.main_bottomBar_home);
    }

    /**
     * 检查版本更新
     */
    private void initVersion() {
        new HttpThread(this,HttpModel.VERSIOON_URL,"",handler,HttpHandler.VERSION_SUCCESS,false).start();
    }

    /**
     * 根据布局bottomBar的ID 切换不同的fragment
     *
     * @param main_bottomBarID
     */
    private void changeFragment(int main_bottomBarID) {
        //先找这个ID 对应的fragment存不存在，不存在就创建，在创建的时候把id存入fragment的tag
        Fragment currentFragment = fragmentManager.findFragmentByTag(String.valueOf(main_bottomBarID));
        if (currentFragment == null) {
            fragmentTransaction = fragmentManager.beginTransaction();
            switch (main_bottomBarID) {
                case R.id.main_bottomBar_home://主页
                    fragmentTransaction.add(R.id.main_fragmentGroup, new MainHomeFragment(), String.valueOf(R.id.main_bottomBar_home));
                    break;
                case R.id.main_bottomBar_category://分类
                    fragmentTransaction.add(R.id.main_fragmentGroup, new MainCategoryFragment(), String.valueOf(R.id.main_bottomBar_category));
                    break;
                case R.id.main_bottomBar_see://逛
                    fragmentTransaction.add(R.id.main_fragmentGroup, new MainSeeFragment(), String.valueOf(R.id.main_bottomBar_see));
                    break;
                case R.id.main_bottomBar_mine://我的
                    fragmentTransaction.add(R.id.main_fragmentGroup, new MainMineFragment(), String.valueOf(R.id.main_bottomBar_mine));
                    break;
            }
            fragmentTransaction.commit();
        }
        //存在 ，隐藏上次的fragment,显示现在的fragment
        if (lastFragmentID != 0) {//表示不是第一次点击
            fragmentTransaction = fragmentManager.beginTransaction();
            //拿到上次fragment
            Fragment lastFragment = fragmentManager.findFragmentByTag(String.valueOf(lastFragmentID));
            if (currentFragment != null) {//再次判断这次fragment
                //显示这次的fragment
                fragmentTransaction.show(currentFragment);
            }
            //隐藏上次的fragment
            if (currentFragment != lastFragment)
                fragmentTransaction.hide(lastFragment);
            fragmentTransaction.commit();
        }
        //重新给上次的fragmentid赋值
        lastFragmentID = main_bottomBarID;
    }

    /**
     * 初始化监听
     */
    private void initListeners() {
        mainbottomBar.setOnCheckedChangeListener(this);
    }

    /**
     * 购物车按钮
     * 点击启动购物车activity
     */
    public void cart(View v) {
        //判断用户是否登陆
        if (MyApplication.getInstance().userInfoBean == null){
            LoginDialog loginDialog = new LoginDialog(this);
            loginDialog.setOnLoginListener(new LoginDialog.onLoginListener() {
                @Override
                public void loginSuccess() {
                    startActivity(new Intent(MainActivity.this, CartActivity.class));
                    overridePendingTransition(R.anim.activity_updown_enter_in, R.anim.activity_updown_quit_out);
                }
            });
            loginDialog.show();
        }else {
            startActivity(new Intent(this, CartActivity.class));
            overridePendingTransition(R.anim.activity_updown_enter_in, R.anim.activity_updown_quit_out);
        }
    }

    /**
     * 初始化控件
     */
    private void initViews() {
        this.mainbottomBar = (RadioGroup) findViewById(R.id.main_bottomBar);
        this.statusBarGroup = (RelativeLayout) findViewById(R.id.statusBarGroup);
        slidingPane = (MySlidingPaneLayout) findViewById(R.id.slidingPane);
        slidingPane.setSliderFadeColor(Color.TRANSPARENT);
        slidingPane.isSlideable();
        cartGoodsNum = (TextView) findViewById(R.id.main_bottomBar_goodsNum);
    }

    /**
     * 沉浸式状态栏
     */
    private void initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && getStatusBarHeight() > 0) {
            int statusBarHeight = getStatusBarHeight();
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            statusBarChild = new View(this);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, statusBarHeight);
            statusBarChild.setBackgroundColor(getResources().getColor(R.color.toolBar_bg));
            statusBarGroup.setLayoutParams(params);
            statusBarGroup.addView(statusBarChild);
        }
    }

    /**
     * 计算状态栏的高度
     *
     * @return 有高度返回该值否则返回0
     */
    public int getStatusBarHeight() {
        int identifier = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (identifier > 0) {
            return getResources().getDimensionPixelSize(identifier);
        }
        return 0;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        //根据不同的id 走不同的fragment
        if (checkedId != R.id.main_bottomBar_cart) {
            changeFragment(checkedId);
        }
    }

    /**
     * 返回上次页面添加activity转场动画
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_updown_quit_in, R.anim.activity_updown_enter_out);
    }

    //    @Override
//    public void onBackPressed() {
//        long currentTime = System.currentTimeMillis();
//        if (currentTime - lastTime <1500){
//            super.onBackPressed();
//        }else {
//            ToastUtils.toast("再次按下返回键退出");
//        }
//    }

    /**
     * 控制侧滑菜单的打开关闭
     *
     * @param v
     */
    public void switchPane(View v) {
        if (slidingPane.isOpen()) {
            slidingPane.closePane();
        } else {
            slidingPane.openPane();
        }
    }

    /**
     * 用户登录成功
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showGoodsNum(DialogLoginSuccess success) {
        ToastUtils.toast("回调了");
        //用户为已登录状态
        if (MyApplication.getInstance().userInfoBean != null) {
            ToastUtils.toast("购物车数据发生改变，请求网络");
            new HttpThread(this, HttpModel.CART_LIST_URL, "parames={\"userId\":\"6\"}", handler, HttpHandler.CART_LIST_SUCCESS, false).start();
        }
    }

    /**
     * handler
     */
    class MyMainHandler extends HttpHandler {

        public MyMainHandler(Context context) {
            super(context);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == HttpHandler.CART_LIST_SUCCESS) {
            LogUtil.logE(LogUtil.DEBUG_TAG,"购物车回调");
                handlerResult(msg.obj.toString());
            }
            if (msg.what == HttpHandler.VERSION_SUCCESS){
                //版本更新返回的数据
                handlerVersion(msg.obj.toString());
            }
        }
    }

    /**
     * 版本更新返回的数据
     */
    private void handlerVersion(String result) {
        VersionBean versionBean = new Gson().fromJson(result, VersionBean.class);
        if (HttpModel.SUCCESSFULLY_OK.equals(versionBean.getSucess())){
            //拿到当前版本与服务器版本比较
            try {
                //拿到包信息
                PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                if (Integer.parseInt(versionBean.getVersion()) > packageInfo.versionCode){
                    //服务器版本大于系统版本  弹出dialog 更新
                    Logger.e(versionBean.toString());
                    new VersionDialog(MainActivity.this,versionBean).show();
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }else {
            ToastUtils.toast("检查版本失败：{success,"+versionBean.getSucess()+"}"+"[404]");
        }
    }

    /**
     * 处理请求购物车返回的数据
     *
     * @param result
     */
    private void handlerResult(String result) {
        CartListInfoBean cartListInfoBean = new Gson().fromJson(result, CartListInfoBean.class);
        String scuess = cartListInfoBean.getScuess();
        int listNum = 0;
        if (HttpModel.SUCCESSFULLY_OK.equals(scuess)) {//数据正确返回
            //遍历 拿到所有商品的数量总和
            List<CartListInfoBean.CartBean> cart = cartListInfoBean.getCart();
            for (CartListInfoBean.CartBean bean : cart) {
                listNum += Integer.parseInt(bean.getNum());
            }
            //显示小红点
            cartGoodsNum.setText(listNum+"");
            cartGoodsNum.setEnabled(false);
            cartGoodsNum.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}

package cn.bjsxt.youhuo.activity;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dd.CircularProgressButton;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import cn.bjsxt.youhuo.LoginToken;
import cn.bjsxt.youhuo.MyApplication;
import cn.bjsxt.youhuo.R;
import cn.bjsxt.youhuo.bean.AddCartParamesBean;
import cn.bjsxt.youhuo.bean.AddCartResultBean;
import cn.bjsxt.youhuo.bean.CartListInfoBean;
import cn.bjsxt.youhuo.bean.GoodsDetailBean;
import cn.bjsxt.youhuo.bean.UserInfoBean;
import cn.bjsxt.youhuo.dialog.ChooseGoodsDialog;
import cn.bjsxt.youhuo.dialog.LoginDialog;
import cn.bjsxt.youhuo.event.DialogLoginEvent;
import cn.bjsxt.youhuo.event.DialogLoginSuccess;
import cn.bjsxt.youhuo.share.ShareDialog;
import cn.bjsxt.youhuo.share.ShareWebBean;
import cn.bjsxt.youhuo.util.HttpHandler;
import cn.bjsxt.youhuo.util.HttpModel;
import cn.bjsxt.youhuo.util.HttpThread;
import cn.bjsxt.youhuo.util.JsonUtils;
import cn.bjsxt.youhuo.util.LogUtil;
import cn.bjsxt.youhuo.util.SPUtils;
import cn.bjsxt.youhuo.util.ToastUtils;
import cn.bjsxt.youhuo.view.CircleHeadView;
import cn.bjsxt.youhuo.view.GoodsDetailScrollView;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;


public class GoodsDetailActivity extends BaseActivity implements View.OnClickListener, LoginDialog.onLoginListener, ChooseGoodsDialog.OnAddCartSureListener {

    /**
     * 返回按钮
     */
    private ImageButton back;
    /**
     * 分享按钮
     */
    private ImageButton share;
    /**
     * 自定义组合控件 继承自scrollView
     */
    private GoodsDetailScrollView mScrollView;
    /**
     * 购物车
     */
    private View cart;
    /**
     * 购物车的小红点
     */
    private TextView cartNum;
    /**
     * 添加到购物车按钮
     */
    private Button addCart;
    /**
     * 收藏按钮
     */
    private ImageButton like;
    private String goodsId;
    private HttpHandler handler;
    private JsonUtils jsonUtils;
    private LoginDialog loginDialog;
    private GoodsDetailBean goodsDetailBean;
    /**
     * 添加到购物车商品的数量
     */
    private int goodsNumber;
    private RelativeLayout rootView;
    /**
     * 添加购物车的动画的图片
     */
    private Bitmap addCartBitmap;
    private CircleHeadView circleHeadView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            goodsId = getIntent().getStringExtra("goodsId");
            ToastUtils.toast(goodsId);
        }
        initUtils();
        initView();
        initDatas();
        initListener();
    }


    /**
     * 初始化一些工具类
     */
    private void initUtils() {
        handler = new GoodsDetailHandler(this);
        jsonUtils = new JsonUtils();
    }

    /**
     * 用户登录成功
     */
    @Override
    public void loginSuccess() {
        //网络获取购物车的信息
        new HttpThread(this, HttpModel.CART_LIST_URL, "parames={\"userId\":\"6\"}", handler, HttpHandler.CART_LIST_SUCCESS).start();
    }

    /**
     * @param goodsNumber 添加商品的数量
     */
    @Override
    public void addCartSuccess(int goodsNumber, Bitmap bitmap) {
        this.goodsNumber = goodsNumber;
        this.addCartBitmap = bitmap;
        //添加一个圆形的ImageView
        addCartAnimationImage();
        //拿到动画的开始位置和结束位置
        getAnimationLocation();
    }

    /**
     * 拿到动画的开始位置和结束位置
     */
    private void getAnimationLocation() {
        //因为circleView刚添加完拿不到坐标 所有用post
        rootView.post(new Runnable() {
            @Override
            public void run() {
                //开始位置
                int[] start = new int[2];
                circleHeadView.getLocationOnScreen(start);
                int startX = start[0];
                int startY = start[1];
                //结束位置
                int[] end = new int[2];
                cart.getLocationOnScreen(end);
                int endX = end[0] + cart.getWidth() / 2;
                int endY = end[1] + 40;

                //两点的差
                TranslateAnimation addCartAnimation = new TranslateAnimation(0, endX - startX, 0, endY - startY);
                addCartAnimation.setFillAfter(true);
                addCartAnimation.setDuration(800);
                addCartAnimation.setInterpolator(new MyInterpolator());
                circleHeadView.startAnimation(addCartAnimation);
                addCartAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        rootView.removeView(circleHeadView);
//                        if (HttpModel.SUCCESSFULLY_NO.equals(addCartResultBean.getScuess())) {
//                            //添加失败
//                            ToastUtils.toast("添加失败，请检查您的网络连接");
//                        } else if (HttpModel.SUCCESSFULLY_OK.equals(addCartResultBean.getScuess())) {
//                            //添加成功
//                            ToastUtils.toast("添加成功");
//                        }
                        int i = Integer.parseInt(cartNum.getText().toString());
                        cartNum.setText("" + (i + goodsNumber));
                        //发送到MainActivity 显示购物车上的数量 。
                        EventBus.getDefault().post(new DialogLoginSuccess());
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });
    }

    /**
     * 自定义插值器
     */
    class MyInterpolator implements Interpolator {

        @Override
        public float getInterpolation(float input) {
            circleHeadView.setScaleX(1.3f - input);
            circleHeadView.setScaleY(1.3f - input);
            circleHeadView.setPivotX(0.5f);
            circleHeadView.setPivotY(0.5f);
            return input;
        }
    }

    /**
     * 添加购物车的动画的View
     */
    private void addCartAnimationImage() {
        //添加一个圆形的ImageView
        circleHeadView = new CircleHeadView(this);
        circleHeadView.setBitmap(addCartBitmap);
        circleHeadView.setColor(getResources().getColor(R.color.start_bg));
        circleHeadView.setBorderWidth(0);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        circleHeadView.setLayoutParams(params);
        rootView.addView(circleHeadView);
    }

    class GoodsDetailHandler extends HttpHandler {

        public GoodsDetailHandler(Context context) {
            super(context);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HttpHandler.GOODS_DETAIL_SUCCESS:
                    LogUtil.logE(LogUtil.DEBUG_TAG, "商品详情返回的数据" + msg.obj.toString());
                    handlerResult(msg.obj.toString());
                    break;
                case HttpHandler.CART_LIST_SUCCESS:
                    LogUtil.logE(LogUtil.DEBUG_TAG, "购物车列表返回的数据" + msg.obj.toString());
                    handlerCart(msg.obj.toString());
                    break;
            }
        }
    }


    /**
     * 处理购物车返回的数据
     *
     * @param result
     */
    private void handlerCart(String result) {
        CartListInfoBean cartListInfoBean = new Gson().fromJson(result, CartListInfoBean.class);
        String scuess = cartListInfoBean.getScuess();
        //判断服务器返回的数据是否正常返回
        int listNum = 0;//购物车的商品总数
        if ("ok".equals(scuess)) {
            List<CartListInfoBean.CartBean> cart = cartListInfoBean.getCart();
            for (CartListInfoBean.CartBean bean : cart) {
                listNum += Integer.parseInt(bean.getNum());
            }
            cartNum.setVisibility(View.INVISIBLE);
            if (listNum > 0) {
                LogUtil.logE(LogUtil.DEBUG_TAG, "购物车的商品总数：" + listNum);
                cartNum.setText(listNum + "");
                cartNum.setVisibility(View.VISIBLE);
            }
        }

    }

    /**
     * 处理商品详情返回的数据
     *
     * @param result
     */
    private void handlerResult(String result) {
        //解析服务器返回的json
        goodsDetailBean = jsonUtils.getGoodsDetailToJson(result);
        //设置数据
        mScrollView.setDatas(goodsDetailBean);
    }

    /**
     * 1.根据传递的商品id从服务器请求商品详情数据
     * 2.判断用户是否登录  如登录 从网络请求用户购物车的信息
     */
    private void initDatas() {
        new HttpThread(this, HttpModel.GOODS_DETAIL_URL, "parames={\"goods_id\":\"" + goodsId + "\"}", handler, HttpHandler.GOODS_DETAIL_SUCCESS).start();
        //请求购物车信息
        if (MyApplication.getInstance().userInfoBean != null) {
            new HttpThread(this, HttpModel.CART_LIST_URL, "parames={\"userId\":\"6\"}", handler, HttpHandler.CART_LIST_SUCCESS).start();
        }
    }

    private void initListener() {
        back.setOnClickListener(this);
        cart.setOnClickListener(this);
        addCart.setOnClickListener(this);
        like.setOnClickListener(this);
        share.setOnClickListener(this);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        //填充布局
        inflaterView(R.layout.activity_goods_detail, getResources().getColor(R.color.toolBar_bg));
        rootView = (RelativeLayout) childView;
        back = (ImageButton) childView.findViewById(R.id.toolbar_goods_detail_back);
        share = (ImageButton) childView.findViewById(R.id.toolbar_goods_detail_share);
        mScrollView = (GoodsDetailScrollView) childView.findViewById(R.id.goods_detail_scrollView);
        cart = childView.findViewById(R.id.goods_detail_cart);

        cartNum = (TextView) childView.findViewById(R.id.goods_detail_bottomBar_cartNum);
        addCart = (Button) childView.findViewById(R.id.goods_detail_bottomBar_addCart);
        like = (ImageButton) childView.findViewById(R.id.goods_detail_bottomBar_like);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_goods_detail_back://返回
                onBackPressed();
                break;
            case R.id.toolbar_goods_detail_share://分享
                new ShareDialog(this,new ShareWebBean(HttpModel.IMGURL+goodsDetailBean.getImgList().get(0)
                ,goodsDetailBean.getTitle(),goodsDetailBean.getTitle(),"http://www.yohobuy.com/")).show();
//                showShare();
                break;
            case R.id.goods_detail_cart://购物车
                break;
            case R.id.goods_detail_bottomBar_addCart://添加至购物车
                addCart();
                break;
            case R.id.goods_detail_bottomBar_like://喜欢  收藏
                break;

        }
    }

    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("分享");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
        oks.show(this);
    }

    /**
     * 添加到购物车
     */
    private void addCart() {
        checkLogin();
        if (MyApplication.getInstance().userInfoBean != null) {
            //添加到购物车 弹出dialog选择
            ChooseGoodsDialog chooseGoodsDialog = new ChooseGoodsDialog(this, goodsDetailBean);
            chooseGoodsDialog.show();
            chooseGoodsDialog.setOnAddCartSureListener(this);
        }
    }

    /**
     * 判断用户是否登陆
     */
    private void checkLogin() {
        UserInfoBean userInfoBean = MyApplication.getInstance().userInfoBean;
        if (userInfoBean == null) {//未登陆
            if (loginDialog == null) {
                loginDialog = new LoginDialog(this);
                loginDialog.setOnLoginListener(this);
            }
            loginDialog.show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}

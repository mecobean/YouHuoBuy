package cn.bjsxt.youhuo.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bjsxt.youhuo.LoginToken;
import cn.bjsxt.youhuo.MyApplication;
import cn.bjsxt.youhuo.R;
import cn.bjsxt.youhuo.bean.UserInfoBean;
import cn.bjsxt.youhuo.dialog.LoginDialog;
import cn.bjsxt.youhuo.event.DialogLoginSuccess;
import cn.bjsxt.youhuo.util.HttpHandler;
import cn.bjsxt.youhuo.util.HttpModel;
import cn.bjsxt.youhuo.util.HttpThread;
import cn.bjsxt.youhuo.util.LogUtil;
import cn.bjsxt.youhuo.util.SPUtils;
import cn.bjsxt.youhuo.util.ToastUtils;
import cn.bjsxt.youhuo.view.CircleHeadView;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * 个人中心fragment
 */
public class MainMineFragment extends BaseFragment implements HttpThread.OnLoadingDialogDismissListener, PlatformActionListener {

    @Bind(R.id.mine_userHead)
    CircleHeadView mineUserHead;
    @Bind(R.id.mine_userName)
    TextView mineUserName;
    @Bind(R.id.mine_userMail)
    TextView mineUserMail;
    @Bind(R.id.mine_user_loginBtn)
    Button mineUserLoginBtn;
    @Bind(R.id.mine_flipper)
    ViewFlipper mineFlipper;
    @Bind(R.id.login_qq)
    ImageView loginQq;
    @Bind(R.id.login_sina)
    ImageView loginSina;
    @Bind(R.id.login_wechat)
    ImageView loginWechat;
    private View rootView;
    private HttpHandler handler;
    /**
     * 保存用户选择的头像的Uri  (因为服务器没有返回，正常情况下服务器会返回用户的头像)
     */
    private Uri tempUri;
    private Platform platform;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main_mine, null);
        ButterKnife.bind(this, rootView);
        ShareSDK.initSDK(context);
        initUtils();
        initFlipper();
        initLogin();
        return rootView;
    }

    private void initUtils() {
        handler = new MineHandler(activity);
    }

    /**
     * 初始化用户登陆状态
     */
    private void initLogin() {
        if (MyApplication.getInstance().userInfoBean == null) {
            //未登陆
            mineUserHead.setVisibility(View.GONE);
            mineUserMail.setVisibility(View.GONE);
            mineUserName.setVisibility(View.GONE);
            mineUserLoginBtn.setVisibility(View.VISIBLE);
        } else {
            //已登录
            mineUserHead.setVisibility(View.VISIBLE);
            mineUserMail.setVisibility(View.VISIBLE);
            mineUserName.setVisibility(View.VISIBLE);
            mineUserLoginBtn.setVisibility(View.GONE);
            //加载用户头像 （应该从服务器请求）
            if (!TextUtils.isEmpty(MyApplication.getInstance().userInfoBean.getHeadPath())) {
                //用户头像路径存在
//                mineUserHead.setBitmap(BitmapFactory.decodeFile(String.valueOf(activity.getFileStreamPath(activity.getFilesDir() + "/head"))));
                try {
                    mineUserHead.setBitmap(BitmapFactory.decodeStream(new FileInputStream(activity.getFilesDir() + "/head")));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                //用户尚未设置头像 显示默认
                mineUserHead.setResourceId(R.mipmap.head_normal);
            }
        }

    }

    /**
     * 初始化ViewFlipper
     */
    private void initFlipper() {
        //设置动画执行完毕的间隔时间
        mineFlipper.setFlipInterval(3000);
        //进入的动画
        mineFlipper.setInAnimation(AnimationUtils.loadAnimation(activity, R.anim.mine_flipper_in));
        //退出的动画
        mineFlipper.setOutAnimation(AnimationUtils.loadAnimation(activity, R.anim.mine_flipper_out));
        //自动循环
        mineFlipper.setAutoStart(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.mine_userHead, R.id.mine_user_loginBtn, R.id.login_qq, R.id.login_sina, R.id.login_wechat})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mine_userHead://点击头像
                chooseHeadImg();
                break;
            case R.id.mine_user_loginBtn://点击登陆按钮
                LoginDialog loginDialog = new LoginDialog(activity);
                loginDialog.show();
                loginDialog.setOnLoginListener(new LoginDialog.onLoginListener() {
                    @Override
                    public void loginSuccess() {
                        mineUserHead.setVisibility(View.VISIBLE);
                        mineUserMail.setVisibility(View.VISIBLE);
                        mineUserName.setVisibility(View.VISIBLE);
                        mineUserLoginBtn.setVisibility(View.GONE);
                        //更新购物车 显示小红点
                        EventBus.getDefault().post(new DialogLoginSuccess());
                        //从网络加载用户头像
                        //加载用户头像 （应该从服务器请求）
                        if (!TextUtils.isEmpty(MyApplication.getInstance().userInfoBean.getHeadPath())) {
                            //用户头像路径存在
//                mineUserHead.setBitmap(BitmapFactory.decodeFile(String.valueOf(activity.getFileStreamPath(activity.getFilesDir() + "/head"))));
                            try {
                                mineUserHead.setBitmap(BitmapFactory.decodeStream(new FileInputStream(activity.getFilesDir() + "/head")));
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        } else {
                            //用户尚未设置头像 显示默认
                            mineUserHead.setResourceId(R.mipmap.head_normal);
                        }
                    }
                });
                break;
            case R.id.login_qq://qq登录
//                if (getUserToken())
//                    return;
                platform = ShareSDK.getPlatform(QQ.NAME);
                platform.setPlatformActionListener(this);
                platform.authorize();
                break;
            case R.id.login_sina://新浪登录
//                if (getUserToken())
//                    return;
                platform = ShareSDK.getPlatform(SinaWeibo.NAME);
                platform.setPlatformActionListener(this);
                platform.authorize();
                break;
            case R.id.login_wechat://微信登录
//                if (getUserToken())
//                    return;
                platform = ShareSDK.getPlatform(Wechat.NAME);
                platform.setPlatformActionListener(this);
                platform.authorize();
                break;
        }
    }

    private boolean getUserToken() {
        if (platform != null && platform.isAuthValid()) {
            final PlatformDb db = platform.getDb();
            LogUtil.logE(LogUtil.DEBUG_TAG, db.getUserName() + "--->" + db.getUserIcon());
            ToastUtils.toast("本地token");
            UserInfoBean userInfoBean = new UserInfoBean(db.getUserIcon(), db.getUserId(), db.getUserName());
            MyApplication.getInstance().userInfoBean = userInfoBean;
            mineUserHead.setVisibility(View.VISIBLE);
            mineUserMail.setVisibility(View.VISIBLE);
            mineUserName.setVisibility(View.VISIBLE);
            mineUserLoginBtn.setVisibility(View.GONE);
            mineUserName.setText(db.getUserName());
            Picasso.with(context).load(db.getUserIcon()).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
                    LogUtil.logE(LogUtil.DEBUG_TAG, bitmap + "");
                    mineUserHead.setBitmap(bitmap);
                }

                @Override
                public void onBitmapFailed(Drawable drawable) {
                }

                @Override
                public void onPrepareLoad(Drawable drawable) {

                }
            });
            return true;
        }
        return false;
    }

    /**
     * 点击头像跳转相册选择头像
     * (启动相机拍照 拿到的是缩略图的uri ,高清图需要先保存到本地在从本地读取)
     */
    private void chooseHeadImg() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != activity.RESULT_OK) return;
        if (requestCode == 1) {
            //通过路径拿到返回的图片 (返回的是图片路径)
            try {
                tempUri = data.getData();
                //拿到图片
                Bitmap bitmap = BitmapFactory.decodeStream(activity.getContentResolver().openInputStream(data.getData()));
                //转换成字符串
                String s = encodeBitMap(bitmap);
                //上传服务器
                HttpThread httpThread = new HttpThread(activity, HttpModel.USER_HEAD_URL, "", handler, HttpHandler.USERHEAD_SUCCESS, true);
                httpThread.setOnLoadingDialogDismissListener(this);
                httpThread.start();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 将图片进行Base64 编码成字符串 用于上传服务器
     *
     * @param bitmap
     * @return
     */
    public String encodeBitMap(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    /**
     * 联网请求 dialog执行完毕
     */
    @Override
    public void omDismiss() {
        //将用用户选择的头像设置上去
        try {
            mineUserHead.setBitmap(BitmapFactory.decodeStream(activity.getContentResolver().openInputStream(tempUri)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onComplete(final Platform platform, int i, HashMap<String, Object> hashMap) {
        final PlatformDb db = platform.getDb();
        LogUtil.logE(LogUtil.DEBUG_TAG,"用户名："+db.getUserName() + "--->用户头像路径：" + db.getUserIcon());
       activity.runOnUiThread(new Runnable() {
           @Override
           public void run() {
               ToastUtils.toast(platform.getName() + "授权成功");
               UserInfoBean userInfoBean = new UserInfoBean(db.getUserIcon(), db.getUserId(), db.getUserName());
               MyApplication.getInstance().userInfoBean = userInfoBean;
               mineUserHead.setVisibility(View.VISIBLE);
               mineUserMail.setVisibility(View.VISIBLE);
               mineUserName.setVisibility(View.VISIBLE);
               mineUserLoginBtn.setVisibility(View.GONE);
               mineUserName.setText(db.getUserName());
               Picasso.with(context).load(db.getUserIcon()).into(new Target() {
                   @Override
                   public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
                       LogUtil.logE(LogUtil.DEBUG_TAG, bitmap + "");
                       mineUserHead.setBitmap(bitmap);
                   }

                   @Override
                   public void onBitmapFailed(Drawable drawable) {
                   }

                   @Override
                   public void onPrepareLoad(Drawable drawable) {

                   }
               });
           }
       });
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        ToastUtils.toast(platform.getName() + "授权失败");
    }

    @Override
    public void onCancel(Platform platform, int i) {
        ToastUtils.toast(platform.getName() + "授权取消");
    }

    class MineHandler extends HttpHandler {

        public MineHandler(Context context) {
            super(context);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HttpHandler.USERHEAD_SUCCESS://上传头像返回
                    ToastUtils.toast(msg.obj.toString());
                    LogUtil.logE(LogUtil.DEBUG_TAG, msg.obj.toString());
                    //重新缓存token 因为用户头像已经发送到服务器 需要重新缓存用户头像
                    MyApplication.getInstance().userInfoBean.setHeadPath(activity.getFilesDir() + "/head");
                    SaveUserHead();
                    //存储用户token
                    LoginToken token = new LoginToken(MyApplication.getInstance().userInfoBean.getHeadPath(),
                            MyApplication.getInstance().userInfoBean.getId(),
                            MyApplication.getInstance().userInfoBean.getName(),
                            System.currentTimeMillis());
                    //缓存token
                    SPUtils.save(HttpModel.USER_TOKEN, HttpModel.USER_TOKEN, new Gson().toJson(token));
                    break;
            }
        }
    }

    /**
     * 保存用户头像到本地
     */
    private void SaveUserHead() {
        try {
            BitmapFactory.decodeStream(activity.getContentResolver().openInputStream(tempUri))
                    .compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(activity.getFilesDir() + "/head"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}

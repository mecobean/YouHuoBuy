package cn.bjsxt.youhuo.dialog;

import android.content.Context;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.dd.CircularProgressButton;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.bjsxt.youhuo.LoginToken;
import cn.bjsxt.youhuo.MyApplication;
import cn.bjsxt.youhuo.R;
import cn.bjsxt.youhuo.activity.GoodsDetailActivity;
import cn.bjsxt.youhuo.bean.UserInfoBean;
import cn.bjsxt.youhuo.event.DialogLoginEvent;
import cn.bjsxt.youhuo.util.HttpHandler;
import cn.bjsxt.youhuo.util.HttpModel;
import cn.bjsxt.youhuo.util.HttpThread;
import cn.bjsxt.youhuo.util.SPUtils;
import cn.bjsxt.youhuo.util.ToastUtils;

/**
 * 登陆dialog
 * 1。点击登陆拿到账户与密码 回调
 *
 */
public class LoginDialog extends BaseDialog implements View.OnClickListener, TextWatcher {

    private final View view;
    private final TextInputEditText accountEdt;
    private final TextInputEditText pswEdt;
    private final CircularProgressButton loginBtn;
    /**
     * 接口回调
     */
    private onLoginListener loginListener;

    public LoginDialog(Context context) {
        super(context);
        view = inflater.inflate(R.layout.dialog_login, null);
        accountEdt = (TextInputEditText) view.findViewById(R.id.dialog_login_account);
        pswEdt = (TextInputEditText) view.findViewById(R.id.dialog_login_psw);
        accountEdt.addTextChangedListener(this);
        pswEdt.addTextChangedListener(this);
        //progressButton
        loginBtn = (CircularProgressButton) view.findViewById(R.id.dialog_login_btn);
        loginBtn.setIndeterminateProgressMode(true);

        setContentView(view,activity.getWindowManager().getDefaultDisplay().getWidth(), WindowManager.LayoutParams.WRAP_CONTENT);
        loginBtn.setOnClickListener(this);
        setGravity(Gravity.CENTER);
        setAnimation(R.style.dialogLoginAnimation);
    }

    @Override
    public void onClick(View v) {
        final String account = accountEdt.getText().toString().trim();
        final String psw = pswEdt.getText().toString().trim();
        if (TextUtils.isEmpty(account)){
            accountEdt.setError("请输入账号");
            return;
        }
        if (TextUtils.isEmpty(psw)){
            pswEdt.setError("请输入密码");
            return;
        }

        if (loginBtn.getProgress() == 0) {
            loginBtn.setProgress(50);
        } else if (loginBtn.getProgress() == 100) {
            loginBtn.setProgress(0);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if ("admin".equals(account) && "admin".equals(psw)) {
                    //登陆成功
                    //设置按钮登陆成功
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loginBtn.setProgress(100);
                            dismiss();
                            ToastUtils.toast("登陆成功");
                            //保存用户登录状态
                            MyApplication.getInstance().userInfoBean = new UserInfoBean("", "6", "admin");
                            //存储用户token
                            LoginToken token = new LoginToken(MyApplication.getInstance().userInfoBean.getHeadPath(),
                                    MyApplication.getInstance().userInfoBean.getId(),
                                    MyApplication.getInstance().userInfoBean.getName(),
                                    System.currentTimeMillis()
                            );
                            SPUtils.save(HttpModel.USER_TOKEN, HttpModel.USER_TOKEN, new Gson().toJson(token));
                           loginListener.loginSuccess();
                        }
                    });
                } else {
                    //登陆失败
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loginBtn.setProgress(-1);
                            loginBtn.setErrorText("账号或密码错误");
                        }
                    });
                }
            }
        }, 2500);
    }

    /**
     * 接收消息
     *
     * @param loginEvent 登录事件
     */
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void loginResult(final DialogLoginEvent loginEvent) {
        try {
            Thread.sleep(3 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        int progress = loginBtn.getProgress();
        if (progress == 100 || progress ==-1){
            loginBtn.setProgress(0);
        }
    }

    /**
     * 接口回调
     */
    public interface onLoginListener{
        void loginSuccess();
    }
    public void setOnLoginListener(onLoginListener loginListener){
        this.loginListener = loginListener;
    }
}

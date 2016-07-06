package cn.bjsxt.youhuo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import cn.bjsxt.youhuo.activity.GirlsActivity;
import cn.bjsxt.youhuo.bean.UserInfoBean;
import cn.bjsxt.youhuo.event.DialogLoginEvent;
import cn.bjsxt.youhuo.event.DialogLoginSuccess;
import cn.bjsxt.youhuo.handler.MainCartHandler;
import cn.bjsxt.youhuo.util.HttpHandler;
import cn.bjsxt.youhuo.util.HttpModel;
import cn.bjsxt.youhuo.util.HttpThread;
import cn.bjsxt.youhuo.util.LogUtil;
import cn.bjsxt.youhuo.util.SPUtils;
import cn.bjsxt.youhuo.util.ToastUtils;

/**
 * 选择页面
 * 1.点击不同的选项 跳转不同页面
 * 2.转场动画
 * 3.6秒无操作 自动跳转至boys 主页面
 */
public class ChooseActivity extends Activity implements View.OnClickListener {
    private long lastTime = 0;
    private android.widget.Button choosebtnboys;
    private android.widget.Button choosebtngirls;
    private android.widget.Button choosebtnkids;
    private android.widget.Button choosebtnlife;
    private boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去标题 去状态栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_choose);

        initViews();
        checkLogin();
        initListener();
        startMainActivity();
    }

    /**
     * 检查用户的登录token是否过期
     * 默认7天过期
     */
    private void checkLogin() {
        //拿到保存的token
        String token = SPUtils.read(HttpModel.USER_TOKEN, HttpModel.USER_TOKEN);
        if (TextUtils.isEmpty(token)) {
            return;
        }

        //转换为loginToken对象
        LoginToken loginToken = new Gson().fromJson(token, LoginToken.class);
        //判断时间
        LogUtil.logE(LogUtil.DEBUG_TAG, "上次登录时间：" + loginToken.lastLoginTime);
        LogUtil.logE(LogUtil.DEBUG_TAG, "本次登录时间：" + System.currentTimeMillis());
        int time = (int) ((System.currentTimeMillis() - loginToken.lastLoginTime) / 1000 / 60);
        LogUtil.logE(LogUtil.DEBUG_TAG, "上次登录时间：" + loginToken.lastLoginTime + "--->本次登陆时间：" + loginToken.lastLoginTime + "--->登录时间差：" + time);
        if (time > 5) {//大于5分钟 过期
            ToastUtils.toast("登陆过期");
            //过期删除保存的token
            SPUtils.remove(HttpModel.USER_TOKEN, HttpModel.USER_TOKEN);
        } else { //没有过期保存用户登录状态
            ToastUtils.toast("登陆未过期");
            MyApplication.getInstance().userInfoBean = new UserInfoBean(
                    loginToken.getHeadPath(), loginToken.getId(), loginToken.getName());
//            //发送到MainActivity 显示购物车上的数量 。
//            EventBus.getDefault().post(new DialogLoginSuccess());
        }
    }

    private void startMainActivity() {
        new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (flag) {
                    startActivity(new Intent(ChooseActivity.this, MainActivity.class));
                    overridePendingTransition(R.anim.activity_updown_enter_in, R.anim.activity_updown_quit_out);
                }
                return true;
            }
        }).sendEmptyMessageDelayed(0, 6 * 1000);
    }

    private void initListener() {
        choosebtnboys.setOnClickListener(this);
        choosebtngirls.setOnClickListener(this);
        choosebtnkids.setOnClickListener(this);
        choosebtnlife.setOnClickListener(this);
    }

    //初始化控件
    private void initViews() {
        this.choosebtnlife = (Button) findViewById(R.id.choose_btn_life);
        this.choosebtnkids = (Button) findViewById(R.id.choose_btn_kids);
        this.choosebtngirls = (Button) findViewById(R.id.choose_btn_girls);
        this.choosebtnboys = (Button) findViewById(R.id.choose_btn_boys);
    }

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastTime < 1500) {
            flag = false;
            super.onBackPressed();
        } else {
            Toast.makeText(this, "再次按下返回键退出", Toast.LENGTH_SHORT).show();
            lastTime = currentTime;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.choose_btn_boys://男生
                flag = false;
                startActivity(new Intent(ChooseActivity.this, MainActivity.class));
                break;
            case R.id.choose_btn_girls://女生
                flag = false;
                startActivity(new Intent(ChooseActivity.this, GirlsActivity.class));
                break;
            case R.id.choose_btn_kids://小孩
                break;
            case R.id.choose_btn_life://创意生活
                break;
        }
        overridePendingTransition(R.anim.activity_updown_enter_in, R.anim.activity_updown_quit_out);
        flag = false;
    }
}

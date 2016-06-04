package cn.bjsxt.youhuo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * 欢迎页
 * 1.图片的动画 由进到远效果
 * 2.点击按钮停止动画，调转至选择页面
 * 3.activity的转场动画
 * 4.双击返回键退出
 */
public class WelcomeActivity extends Activity implements View.OnClickListener, Animation.AnimationListener {

    private android.widget.ImageView welcomeimg;
    private android.widget.Button welcomebtn;
    private Animation scaleAnimation;
    private boolean flag = true;
    private long lastTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去标题 隐藏状态栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        initViews();
        initListener();
    }

    //初始化监听
    private void initListener() {
        welcomebtn.setOnClickListener(this);
        scaleAnimation.setAnimationListener(this);
    }

    //初始化控件 并开始动画
    private void initViews() {
        this.welcomebtn = (Button) findViewById(R.id.welcome_btn);
        this.welcomeimg = (ImageView) findViewById(R.id.welcome_img);
        scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.welcome_img_anim_scale);
        scaleAnimation.setDuration(3500);
        scaleAnimation.setFillAfter(true);
        welcomeimg.startAnimation(scaleAnimation);
    }

    @Override
    public void onClick(View v) {
        //点击取消动画立即跳转
        scaleAnimation.cancel();
        flag = false;
        startActivity(new Intent(WelcomeActivity.this, ChooseActivity.class));
        overridePendingTransition(R.anim.choose_anim_in,R.anim.start_anim_out);
        finish();
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        //缩放动画执行完毕 跳转至选择界面
        if ( flag) {
            startActivity(new Intent(WelcomeActivity.this, ChooseActivity.class));
            overridePendingTransition(R.anim.choose_anim_in,R.anim.start_anim_out);
            finish();
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    /**
     * 双击返回键退出
     * 调用super.onBackPressed();系统就会自己调用finish方法
     */
    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastTime <1500){//连续按下back键的时间小于1.5秒，退出
           super.onBackPressed();
        }else {
            Toast.makeText(this,"再次按下返回键退出",Toast.LENGTH_SHORT).show();
            lastTime = currentTime;
        }
    }
}

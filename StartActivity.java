package cn.bjsxt.youhuo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

/**
 * 开始界面 2秒跳转
 * 注意：使用requestWindowFeature时Activity不能继承自AppCompatActivity，
 * 否则requestWindowFeature不生效 。。。。。。。
 */
public class StartActivity extends Activity {

    private android.widget.RelativeLayout start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_start);
        this.start = (RelativeLayout) findViewById(R.id.start);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(StartActivity.this,WelcomeActivity.class));
                overridePendingTransition(R.anim.welcome_anim_in,R.anim.start_anim_out);
                finish();
            }
        },2000);
    }
}

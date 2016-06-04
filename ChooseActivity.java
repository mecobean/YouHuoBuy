package cn.bjsxt.youhuo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

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
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_choose);
        initViews();
        initListener();
        startMainActivity();
    }

    private void startMainActivity() {
        new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (flag){
                    startActivity(new Intent(ChooseActivity.this,MainActivity.class));
                    overridePendingTransition(R.anim.activity_updown_enter_in,R.anim.activity_updown_quit_out);
                }
                return true;
            }
        }).sendEmptyMessageDelayed(0,6*1000);
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
        if (currentTime - lastTime <1500){
            flag = false;
            super.onBackPressed();
        }else {
            Toast.makeText(this,"再次按下返回键退出",Toast.LENGTH_SHORT).show();
            lastTime = currentTime;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.choose_btn_boys://男生
                startActivity(new Intent(ChooseActivity.this,MainActivity.class));
                overridePendingTransition(R.anim.activity_updown_enter_in,R.anim.activity_updown_quit_out);
                break;
             case R.id.choose_btn_girls://女生
                break;
             case R.id.choose_btn_kids://小孩
                break;
             case R.id.choose_btn_life://创意生活
                break;
        }
             flag = false;
    }
}

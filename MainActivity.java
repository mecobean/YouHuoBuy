package cn.bjsxt.youhuo;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import cn.bjsxt.youhuo.fragment.MainCategoryFragment;
import cn.bjsxt.youhuo.fragment.MainHomeFragment;
import cn.bjsxt.youhuo.fragment.MainMineFragment;
import cn.bjsxt.youhuo.fragment.MainSeeFragment;

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
    private RelativeLayout statusBarGroup;
    private android.widget.RadioGroup mainbottomBar;
    private View statusBarChild;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private int lastFragmentID = 0;//上次显示的fragment的tag(button的id)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        initViews();
        initStatusBar();
        initListeners();
        fragmentManager = getSupportFragmentManager();
        //默认显示首页
        changeFragment(R.id.main_bottomBar_home);
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
                    fragmentTransaction.add(R.id.main_fragmentGroup,new MainHomeFragment(), String.valueOf(R.id.main_bottomBar_home));
                    break;
                case R.id.main_bottomBar_category://分类
                    fragmentTransaction.add(R.id.main_fragmentGroup,new MainCategoryFragment(), String.valueOf(R.id.main_bottomBar_category));
                    break;
                case R.id.main_bottomBar_see://逛
                    fragmentTransaction.add(R.id.main_fragmentGroup,new MainSeeFragment(), String.valueOf(R.id.main_bottomBar_see));
                    break;
                case R.id.main_bottomBar_mine://我的
                    fragmentTransaction.add(R.id.main_fragmentGroup,new MainMineFragment(), String.valueOf(R.id.main_bottomBar_mine));
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
     * 初始化控件
     */
    private void initViews() {
        this.mainbottomBar = (RadioGroup) findViewById(R.id.main_bottomBar);
        this.statusBarGroup = (RelativeLayout) findViewById(R.id.statusBarGroup);
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
        if (checkedId != R.id.main_bottomBar_cart){
            changeFragment(checkedId);
        }
    }

    /**
     * 返回上次页面添加activity转场动画
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_updown_quit_in,R.anim.activity_updown_enter_out);
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
}

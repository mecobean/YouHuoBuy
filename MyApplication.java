package cn.bjsxt.youhuo;

import android.app.Application;

/**
 * Created by Mecono on 2016/6/3.
 */
public class MyApplication extends Application {
    private static MyApplication application ;
    public static MyApplication getInstance(){
        if (application == null){
            synchronized (MyApplication.class){
                if (application == null){
                    application = new MyApplication();
                }
            }
        }
        return application;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }
}

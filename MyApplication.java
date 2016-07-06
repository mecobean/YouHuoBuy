package cn.bjsxt.youhuo;

import android.app.Application;

import cn.bjsxt.youhuo.bean.UserInfoBean;
import cn.bjsxt.youhuo.util.HttpModel;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Mecono on 2016/6/3.
 */
public class MyApplication extends Application {
    public UserInfoBean userInfoBean;
    private static MyApplication application ;
    public Retrofit retrofit;
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
        retrofit = new Retrofit.Builder().baseUrl(HttpModel.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }
}

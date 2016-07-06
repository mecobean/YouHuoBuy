package cn.bjsxt.youhuo.share;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import cn.bjsxt.youhuo.MyApplication;
import cn.sharesdk.framework.TitleLayout;
import cn.sharesdk.framework.authorize.AuthorizeAdapter;
import cn.sharesdk.sina.weibo.SinaWeibo;

/**
 * 修改新浪微博授权页面 toolBar
 */
public class MyAuthorAdapter extends AuthorizeAdapter {
    @Override
    public void onCreate() {
        super.onCreate();
        String platformName = getPlatformName();
        if(platformName.equals(SinaWeibo.NAME)){
            getTitleLayout().getTvTitle().setText("我的新浪");
            getTitleLayout().getTvTitle().setBackgroundColor(Color.parseColor("#393939"));

            getTitleLayout().getChildAt(1).setVisibility(View.GONE);
            getTitleLayout().getBtnBack().setBackgroundColor(Color.parseColor("#393939"));

            hideShareSDKLogo();
        }
    }
}

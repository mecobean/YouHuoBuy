package cn.bjsxt.youhuo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.bjsxt.youhuo.R;
import cn.bjsxt.youhuo.adapter.AdapterMainGV;
import cn.bjsxt.youhuo.bean.AdvertInfo;
import cn.bjsxt.youhuo.bean.MainHomeGVBean;
import cn.bjsxt.youhuo.util.FileUtils;
import cn.bjsxt.youhuo.util.HttpHandler;
import cn.bjsxt.youhuo.util.HttpModel;
import cn.bjsxt.youhuo.util.HttpThread;
import cn.bjsxt.youhuo.util.JsonUtils;
import cn.bjsxt.youhuo.util.LogUtil;
import cn.bjsxt.youhuo.view.AdvertView;

/**
 * 1.顶部logo的动画 ---> 组合动画
 * 2.广告轮播
 * 从服务器获取json解析数据
 * 拼接图片的url
 * 从服务区请求图片
 * 自定义组合空间渲染显示图片
 */
public class GirlsHomeFragment extends GirlsBaseFragment {


    @Override
    protected void initDatas() {

    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {
        View inflate = inflater.inflate(R.layout.fragment_girls_cart, null);
        TextView tv = (TextView) inflate.findViewById(R.id.girl_fragment);
        tv.setText("Home");
        return inflate;
    }
}

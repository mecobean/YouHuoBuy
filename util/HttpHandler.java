package cn.bjsxt.youhuo.util;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

/**
 * 该项目里与服务器交互信息处理的handler的基类
 * what大于0 保存到本地 ，小于0不保存
 */
public class HttpHandler extends Handler {
    private Context context;

    public HttpHandler(Context context) {
        this.context = context;
    }

    /**
     * fragmentHome 广告网络请求的真确返回
     */
    public static final int ADVERT_SUCCESS = 200;
    /**
     * categoryFragmentChild 的boy选项卡网络请求正确返回
     */
    public static final int CATEGORY_BOY_SUCCESS = 201;
    /**
     * categoryFragmentChild 的girls选项卡网络请求正确返回
     */
    public static final int CATEGORY_GIRLS_SUCCESS = 202;
    /**
     * categoryFragmentChild 的life选项卡网络请求正确返回
     */
    public static final int CATEGORY_LIFE_SUCCESS = 203;
    /**
     * categoryFragmentChild 的二级菜单选项卡网络请求正确返回
     */
    public static final int CATEGORY_VALUE_SUCCESS = -204;
    /**
     * 品牌的广告数据正确返回
     */
    public static final int CATEGORY_BRAND_ADBERT_SUCCESS = 205;
    /**
     * 品牌分类  热门品牌数据成功返回
     */
    public static final int CATEGORY_HOT_BRAND_SUCCESS = 206;
    /**
     * 品牌分类  所有品牌数据成功返回
     */
    public static final int CATEGORY_ALL_BRAND_SUCCESS = 207;
    /**
     * 品牌列表数据正确返回
     */
    public static final int BRAND_INFO_SUCCESS = -208;
    /**
     * 品牌列表的商品详细信息正确返回
     */
    public static final int GOODS_DETAIL_SUCCESS = -209;
    /**
     * 购物车列表数据正确返回
     */
    public static final int CART_LIST_SUCCESS = -210;
    /**
     * 添加购物车正确返回
     */
    public static final int ADD_CART_SUCCESS = -211;
    /**
     * 添加购物车正确返回
     */
    public static final int HOME_SUCCESS = -212;
    /**
     * 关注分类数据正确返回
     */
    public static final int GUANZHU_SUCCESS = 213;

    /**
     * 逛页面 数据正确返回
     */
    public static final int SEE_CHILD_SUCCESS = 214;
    /**
     * 逛页面 广告数据正确返回
     */
    public static final int SEE_CHILD_ADVERT_SUCCESS = 215;
    /**
     * 版本更新 数据正确返回
     */
    public static final int VERSION_SUCCESS = -216;
    /**
     * 上传用户头像 数据正确返回
     */
    public static final int USERHEAD_SUCCESS = -217;

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case HttpModel.NO_NETWORK:
                ToastUtils.toast("当前无网络链接");
                break;
            case HttpModel.UNKNOWN_NETWORK:
                ToastUtils.toast("未知的网络链接");
                break;
            case HttpModel.ERROR_NETWORK:
                ToastUtils.toast("网络连接错误");
                break;
        }
    }
}

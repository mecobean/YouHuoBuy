package cn.bjsxt.youhuo.util;

/**
 * 定义一些错误信息状态
 * 定义一些常量值
 */
public class HttpModel {
    /**
     * 无网络
     */
    public static final int NO_NETWORK = 0;
    /**
     * 网络链接未知
     */
    public static final int UNKNOWN_NETWORK = 1;
    /**
     * 网络连接错误
     */
    public static final int ERROR_NETWORK = 2;
    /**
     * 用户登录token
     */
    public static final String USER_TOKEN = "token";

    /**
     * Main homeFragment 广告的图片的Json数据的文件名
     */
    public static final String CACHE_FILE_NAME = "data" + HttpHandler.ADVERT_SUCCESS + ".txt";


    /**
     * 获取服务器返回值地址前端
     */
    public static final String URL = "http://www.iwens.org/School_Sky/";
    /**
     * 获取服务器图片地址
     */
    public static final String IMGURL = "http://www.iwens.org/School_Sky/Img/";
    /**
     * 获取首页广告接口
     */
    public static final String ADVERTURL = URL + "yohoadvert.php";
    /**
     * 分类页面boy接口:
     */
    public static final String CATEGORY_BOY = "categoryboy.php";
    /**
     * 分类页面girl接口:
     */
    public static final String CATEGORY_GIRL = "categorygirl.php";
    /**
     * 分类页面lifestyle接口: :
     */
    public static final String CATEGORY_LIFE = "categorylife.php";
    /**
     * 分类页面toplistview 二级菜单 接口:
     * 参数:parames={\"id\":"+parentId+"}
     */
    public static final String CATEGORY_VALUE = "categoryvalue.php";
    /**
     * 服务器返回的字段
     * "sucessfully": "ok",
     * "boy": [
     * {
     * "_id": "1",
     * "name": "上衣",
     * "SexId": "1"
     * }
     */
    public static final String SUCCESSFULLY = "sucessfully";
    /**
     * category一级菜单返回正常  有数据
     */
    public static final String SUCCESSFULLY_OK = "ok";
    /**
     * category一级菜单返回正常  无数据
     */
    public static final String SUCCESSFULLY_NO = "no";
    /**
     * 服务器返回boy一级菜单的key
     */
    public static final String KEY_BOY = "boy";
    /**
     * 服务器返回girl一级菜单的key
     */
    public static final String KEY_GIRLS = "girl";
    /**
     * 服务器返回life一级菜单的key
     */
    public static final String KEY_LIFESTYLE = "life";
    /**
     * 服务器返回二级菜单的key
     */
    public static final String KEY_VALUE = "value";
    /**
     * 热门品牌接口: hotbrand.php
     */
    public static final String HOT_BRAND_URL = URL + "hotbrand.php";
    /**
     * 热门品牌接口正确返回的key
     */
    public static final String HOT_BRAND_KEY = "brand";
    /**
     * 所有品牌接口: allbrand.php
     * 参数: parames={\"page\":\"10\"}
     */
    public static final String ALL_BRAND_URL = URL + "allbrand.php";
    /**
     * 所有品牌接口正确返回的key
     */
    public static final String ALL_BRAND_KEY = "brand";
    /**
     * 品牌列表信息接口
     * parames={"page":"0","id":""+brandId+""}
     */
    public static String BRAND_INFO_URL = URL + "brandvalue.php";
    /**
     * 商品详情信息接口
     * 参数 ：parames={\"goods_id\":\""+id+"\"}
     */
    public static String GOODS_DETAIL_URL = URL + "goodsvalue.php";
    /**
     * 请求购物车列表接口
     * 参数：parames={\"userId\":\""+((MyApplication)getApplicationContext()).userId+"\"}
     */
    public static String CART_LIST_URL = URL + "cartlist.php";
    /**
     * 添加商品到购物车接口
     * 参数: parames={\"goodsId\":\""+list.get(0)._id+"\"," +
     * "\"userId\":\""+((MyApplication)getApplicationContext()).userId+"\"," +
     * "\"colorId\":\""+colorID+"\"," +
     * "\"sizeId\":\""+sizeID+"\"}
     */
    public static String ADD_CART_URL = URL + "addcart.php";
    /**
     * fragment home  的数据接口
     * 参数: parames={\"shop\":\"1\"}
     */
    public static String HOME_URL = URL + "homepager.php";
    /**
     * 获取关注列表接口: follow.php
     */
    public static String GUANZHU_URL = URL + "follow.php";
    /**
     * see chile 接口: news.php
     * 参数: parames={\"page\":\"10\"}
     */
    public static String SEE_CHILD_URL = URL + "news.php";
    /**
     * 购物车提交商品接口: UpOrder.php
     * 参数: parames={userid:1,goods[{goodsid:1,color:hong,size:x
     num:1},{goodsid:1,color:hong,size:x
     num:1}]}
     */
    public static String UPORDER_URL = URL + "UpOrder.php";
    /**
     * 版本更新 接口
     */
    public static String VERSIOON_URL = URL + "upVersion.php";
     /**
     * 上传用户头像的接口
     */
    public static String USER_HEAD_URL = URL + "yohouphead.php";

}

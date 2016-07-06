package cn.bjsxt.youhuo.util;

import cn.bjsxt.youhuo.MyApplication;

/**
 * sharePreferences 工具类
 */
public class SPUtils {
    /**
     * 保存文件
     * @param name  文件名
     * @param key   key
     * @param value value
     */
    public static void save(String name, String key, String value) {
        MyApplication.getInstance().getSharedPreferences(name, 0).edit().putString(key, value).commit();
    }

    /**
     * 读取文件
     * @param name 文件名
     * @param key key
     * 如果没有返回空  -- ""
     */
    public static String read(String name, String key) {
        return MyApplication.getInstance().getSharedPreferences(name, 0).getString(key, "");
    }
    /**
     * 删除文件
     * @param name 文件名
     * @param key key
     */
    public static void remove(String name, String key) {
        MyApplication.getInstance().getSharedPreferences(name, 0).edit().remove(key).commit();
    }

}

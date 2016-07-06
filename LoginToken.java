package cn.bjsxt.youhuo;

import cn.bjsxt.youhuo.bean.UserInfoBean;

/**
 * 用户token实体类
 * 标记用户是否登录过
 */
public class LoginToken extends UserInfoBean{
    /**
     * 上次登录的时间
     */
    public long lastLoginTime;
    public LoginToken(String headPath, String id, String name,long lastLoginTime) {
        super(headPath, id, name);
        this.lastLoginTime = lastLoginTime;
    }
}

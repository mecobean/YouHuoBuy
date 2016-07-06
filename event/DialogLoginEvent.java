package cn.bjsxt.youhuo.event;

import com.dd.CircularProgressButton;

/**
 * dialog 用户登陆的事件
 */
public class DialogLoginEvent extends BaseEvent{
    /**
     * 用户名
     */
    public String name;
    /**
     * 密码
     */
    public String psw;
    /**
     * 进度条登陆按钮
     */
    public CircularProgressButton loginBtn;

    public DialogLoginEvent() {
    }

    public DialogLoginEvent(CircularProgressButton loginBtn, String name, String psw) {
        this.loginBtn = loginBtn;
        this.name = name;
        this.psw = psw;
    }
}

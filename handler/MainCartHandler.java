package cn.bjsxt.youhuo.handler;

import android.content.Context;
import android.os.Message;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import cn.bjsxt.youhuo.bean.CartListInfoBean;
import cn.bjsxt.youhuo.event.CartListEvent;
import cn.bjsxt.youhuo.util.HttpHandler;
import cn.bjsxt.youhuo.util.ToastUtils;

/**
 * 接受/处理购物车发送过来的数据
 */
public class MainCartHandler extends HttpHandler {
    public MainCartHandler(Context context) {
        super(context);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (msg.what == HttpHandler.CART_LIST_SUCCESS) {
            handlerResult(msg.obj.toString());
        }
    }

    /**
     * 处理返回json
     */
    private void handlerResult(String result) {
        CartListInfoBean cartListInfoBean = new Gson().fromJson(result, CartListInfoBean.class);
        String scuess = cartListInfoBean.getScuess();
        //判断服务器返回的数据是否正常返回
        if ("ok".equals(scuess)) {
            //发送到购物车 适配显示
            EventBus.getDefault().post(new CartListEvent(cartListInfoBean));
        } else {
            ToastUtils.toast("网络异常");
        }
    }

}

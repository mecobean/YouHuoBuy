package cn.bjsxt.youhuo.event;

import cn.bjsxt.youhuo.bean.CartListInfoBean;

/**
 * 购物车请求数据的事件
 */
public class CartListEvent extends BaseEvent {
    /**
     * 购物车列表实体类
     */
    public CartListInfoBean cartListInfoBean;

    public CartListEvent(CartListInfoBean cartListInfoBean) {
        this.cartListInfoBean = cartListInfoBean;
    }

    public CartListEvent() {
    }
}


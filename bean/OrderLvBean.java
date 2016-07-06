package cn.bjsxt.youhuo.bean;


import java.io.Serializable;

/**
 * 确认订单的数据实体类
 */
public class OrderLvBean implements Serializable {
    public Object object;

    public OrderLvBean(Object object) {
        this.object = object;
    }
}

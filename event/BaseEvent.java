package cn.bjsxt.youhuo.event;

/**
 * 所有自定义事件的基类
 */
public class BaseEvent {
    Object object;

    public BaseEvent() {
    }

    public BaseEvent(Object object) {
        this.object = object;
    }
}

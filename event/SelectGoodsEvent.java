package cn.bjsxt.youhuo.event;

/**
 * 商品选中的事件
 */
public class SelectGoodsEvent  {
    /**
     * 是否全部选中
     */
    public boolean allSelect;
    /**
     * 选中商品的总价
     */
    public int prices ;
    /**
     * 选中商品的总数
     */
    public int num;

    public SelectGoodsEvent(boolean allSelect, int num, int prices) {
        this.allSelect = allSelect;
        this.num = num;
        this.prices = prices;
    }

    public SelectGoodsEvent() {
    }
}

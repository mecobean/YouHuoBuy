package cn.bjsxt.youhuo.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 提交订单的参数 实体类
 */
public class OrderParamesBean implements Serializable{

    /**
     * userid : 1
     * goods : [{"goodsid":1,"color":"hong","size":"x","num":1},{"goodsid":1,"color":"hong","size":"x","num":1}]
     */

    private String userid;
    /**
     * goodsid : 1
     * color : hong
     * size : x
     * num : 1
     */

    private List<Good> goods;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public List<Good> getGoods() {
        return goods;
    }

    public void setGoods(List<Good> goods) {
        this.goods = goods;
    }

    public static class Good {
        private String goodsid;
        private String color;
        private String size;
        private int num;

        public String getGoodsid() {
            return goodsid;
        }

        public void setGoodsid(String goodsid) {
            this.goodsid = goodsid;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }
    }
}

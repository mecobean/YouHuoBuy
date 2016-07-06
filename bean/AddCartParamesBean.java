package cn.bjsxt.youhuo.bean;

/**
 * 添加购物车 post请求的参数
 * parames={\"goodsId\":\""+list.get(0)._id+"\"," +
 "\"userId\":\""+((MyApplication)getApplicationContext()).userId+"\"," +
 "\"colorId\":\""+colorID+"\"," +
 "\"sizeId\":\""+sizeID+"\"}
 */
public class AddCartParamesBean {
    public String goodsId;
    public String userId;
    public String colorId;
    public String sizeId;

    public AddCartParamesBean() {
    }

    public AddCartParamesBean(String colorId, String goodsId, String sizeId, String userId) {
        this.colorId = colorId;
        this.goodsId = goodsId;
        this.sizeId = sizeId;
        this.userId = userId;
    }

    public String getColorId() {
        return colorId;
    }

    public void setColorId(String colorId) {
        this.colorId = colorId;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getSizeId() {
        return sizeId;
    }

    public void setSizeId(String sizeId) {
        this.sizeId = sizeId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

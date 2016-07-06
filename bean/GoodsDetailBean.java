package cn.bjsxt.youhuo.bean;

import java.util.List;

/**
 * 商品详细信息实体类
 */
public class GoodsDetailBean {
    private String id;//id
    private String title;//商品名称
    private String price;//商品的现价
    private String discount;//商品的折扣价
    private List<String> imgList;//viewpager的图片路径
    private List<String> imgvalueList;//listView的图片路径

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getImgList() {
        return imgList;
    }

    public void setImgList(List<String> imgList) {
        this.imgList = imgList;
    }

    public List<String> getImgvalueList() {
        return imgvalueList;
    }

    public void setImgvalueList(List<String> imgvalueList) {
        this.imgvalueList = imgvalueList;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "GoodsDetailBean{" +
                "discount='" + discount + '\'' +
                ", id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", price='" + price + '\'' +
                ", imgList=" + imgList.size() +
                ", imgvalueList=" + imgvalueList.size() +
                '}';
    }
}

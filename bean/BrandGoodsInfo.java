package cn.bjsxt.youhuo.bean;

import java.util.List;

/**
 * 商品列表实体类
 */
public class BrandGoodsInfo {


    /**
     * sucessfully : ok
     * brandname : nike
     * brandvalue : nike
     * brandimg : brand1.jpg
     * goods : [{"_id":"1","title":"奔跑吧兄弟 撕名牌魔术贴徽章T恤","price":"139.00","ShopId":"1","GoodsUrl":"http://item.yohobuy.com/product/pro_204497_273231/","time":"1439984566","discount":"139.00","SexId":"0","CategoryId":"0","ColorId":"0","advertId":"0","imgpath":"a1.jpg"},{"_id":"2","title":"CLOTtee Basic Logo Tee","price":"259.00","ShopId":"2","GoodsUrl":"http://item.yohobuy.com/product/pro_175259_237987/","time":"1439984567","discount":"259.00","SexId":"0","CategoryId":"0","ColorId":"0","advertId":"0","imgpath":"b1.jpg"},{"_id":"3","title":"Life·After Life 彩色星星满版印花短袖T恤","price":"159.00","ShopId":"3","GoodsUrl":"http://item.yohobuy.com/product/pro_157541_215711/","time":"1439984568","discount":"99.00","SexId":"0","CategoryId":"0","ColorId":"0","advertId":"0","imgpath":"c1.jpg"},{"_id":"4","title":"Disney Collection By STAYREAL 天使星米奇踢","price":"359.00","ShopId":"4","GoodsUrl":"http://item.yohobuy.com/product/pro_99419_143875/S","time":"1439984569","discount":"359.00","SexId":"0","CategoryId":"0","ColorId":"0","advertId":"0","imgpath":"d1.jpg"},{"_id":"5","title":"CLOTtee Suck My Clot Tee","price":"259.00","ShopId":"5","GoodsUrl":"http://item.yohobuy.com/product/pro_175269_238005/","time":"1439984570","discount":"199.00","SexId":"0","CategoryId":"0","ColorId":"0","advertId":"0","imgpath":"e1.jpg"},{"_id":"6","title":"MESS TEAM MESS TEE","price":"229.00","ShopId":"1","GoodsUrl":"http://item.yohobuy.com/product/pro_83052_123702/M","time":"1439984571","discount":"229.00","SexId":"0","CategoryId":"0","ColorId":"0","advertId":"0","imgpath":"f1.jpg"},{"_id":"7","title":"CLOTtee Basic Logo Tee","price":"259.00","ShopId":"1","GoodsUrl":"http://item.yohobuy.com/product/pro_182159_246653/","time":"1439984572","discount":"179.00","SexId":"0","CategoryId":"0","ColorId":"0","advertId":"0","imgpath":"g1.jpg"},{"_id":"8","title":"奔跑吧兄弟 印花织带长款T恤","price":"129.00","ShopId":"1","GoodsUrl":"http://item.yohobuy.com/product/pro_204499_273235/","time":"1439984573","discount":"129.00","SexId":"0","CategoryId":"0","ColorId":"0","advertId":"0","imgpath":"h1.jpg"},{"_id":"9","title":"CLOTtee Japanese Logo Tee","price":"259.00","ShopId":"1","GoodsUrl":"http://item.yohobuy.com/product/pro_184677_249497/","time":"1439984574","discount":"169.00","SexId":"0","CategoryId":"0","ColorId":"0","advertId":"0","imgpath":"i1.jpg"},{"_id":"10","title":"CLOTtee OUCH Tee","price":"259.00","ShopId":"1","GoodsUrl":"http://item.yohobuy.com/product/pro_185139_250205/","time":"1439984575","discount":"229.00","SexId":"0","CategoryId":"0","ColorId":"0","advertId":"0","imgpath":"j1.jpg"}]
     */

    private String sucessfully;
    private String brandname;
    private String brandvalue;
    private String brandimg;
    /**
     * _id : 1
     * title : 奔跑吧兄弟 撕名牌魔术贴徽章T恤
     * price : 139.00
     * ShopId : 1
     * GoodsUrl : http://item.yohobuy.com/product/pro_204497_273231/
     * time : 1439984566
     * discount : 139.00
     * SexId : 0
     * CategoryId : 0
     * ColorId : 0
     * advertId : 0
     * imgpath : a1.jpg
     */

    private List<GoodsBean> goods;

    public String getSucessfully() {
        return sucessfully;
    }

    public void setSucessfully(String sucessfully) {
        this.sucessfully = sucessfully;
    }

    public String getBrandname() {
        return brandname;
    }

    public void setBrandname(String brandname) {
        this.brandname = brandname;
    }

    public String getBrandvalue() {
        return brandvalue;
    }

    public void setBrandvalue(String brandvalue) {
        this.brandvalue = brandvalue;
    }

    public String getBrandimg() {
        return brandimg;
    }

    public void setBrandimg(String brandimg) {
        this.brandimg = brandimg;
    }

    public List<GoodsBean> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsBean> goods) {
        this.goods = goods;
    }
    /**
     *  存在于品牌列表返回数据中的实体类对象
     {
     "_id": "1",
     "title": "奔跑吧兄弟 撕名牌魔术贴徽章T恤",
     "price": "139.00",
     "ShopId": "1",
     "GoodsUrl": "http://item.yohobuy.com/product/pro_204497_273231/",
     "time": "1439984566",
     "discount": "139.00",
     "SexId": "0",
     "CategoryId": "0",
     "ColorId": "0",
     "advertId": "0",
     "imgpath": "a1.jpg"
     }
     */
    public static class GoodsBean {
        private String _id;
        private String title;
        private String price;
        private String ShopId;
        private String GoodsUrl;
        private String time;
        private String discount;
        private String SexId;
        private String CategoryId;
        private String ColorId;
        private String advertId;
        private String imgpath;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getShopId() {
            return ShopId;
        }

        public void setShopId(String ShopId) {
            this.ShopId = ShopId;
        }

        public String getGoodsUrl() {
            return GoodsUrl;
        }

        public void setGoodsUrl(String GoodsUrl) {
            this.GoodsUrl = GoodsUrl;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getSexId() {
            return SexId;
        }

        public void setSexId(String SexId) {
            this.SexId = SexId;
        }

        public String getCategoryId() {
            return CategoryId;
        }

        public void setCategoryId(String CategoryId) {
            this.CategoryId = CategoryId;
        }

        public String getColorId() {
            return ColorId;
        }

        public void setColorId(String ColorId) {
            this.ColorId = ColorId;
        }

        public String getAdvertId() {
            return advertId;
        }

        public void setAdvertId(String advertId) {
            this.advertId = advertId;
        }

        public String getImgpath() {
            return imgpath;
        }

        public void setImgpath(String imgpath) {
            this.imgpath = imgpath;
        }
    }
}

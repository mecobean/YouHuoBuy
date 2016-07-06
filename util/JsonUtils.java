package cn.bjsxt.youhuo.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.bjsxt.youhuo.bean.AdvertInfo;
import cn.bjsxt.youhuo.bean.BrandGoodsInfo;
import cn.bjsxt.youhuo.bean.CategoryAllBrandBean;
import cn.bjsxt.youhuo.bean.CategoryFirstMenuBean;
import cn.bjsxt.youhuo.bean.CategoryHotBrandBean;
import cn.bjsxt.youhuo.bean.CategorySecondMenuBean;
import cn.bjsxt.youhuo.bean.GoodsDetailBean;

/**
 * Json 解析工具类
 */
public class JsonUtils {
    public List<AdvertInfo> getAdvertParseJson(String json) {
        List<AdvertInfo> list = new ArrayList<>();
        try {
            JSONArray jay = new JSONArray(json);
            for (int i = 0; i < jay.length(); i++) {
                JSONObject job = jay.getJSONObject(i);
                AdvertInfo info = new AdvertInfo();
                info.set_id(job.getString("_id"));
                info.setAdvertId(job.getString("advertId"));
                info.setImgpath(job.getString("imgpath"));
                list.add(info);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * 根据不同的key 做不同的解析
     *
     * @param json 要解析的数据
     * @param key  每个选型卡的key
     */
    public List<CategoryFirstMenuBean> getFirstMenuParseJson(String json, String key) {
        List<CategoryFirstMenuBean> list = new ArrayList<>();
        if (json == null || json.equals("")) {
            //如果json为空直接返货这个空的list
            return list;
        }
        try {
            JSONObject jsonObject = new JSONObject(json);
            String code = jsonObject.getString(HttpModel.SUCCESSFULLY);
            if (code.equals(HttpModel.SUCCESSFULLY_NO)) {
                return list;
            }
            JSONArray jsonArray = jsonObject.getJSONArray(key);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject job = jsonArray.getJSONObject(i);
                CategoryFirstMenuBean bean = new CategoryFirstMenuBean();
                bean.set_id(job.getString("_id"));
                bean.setName(job.getString("name"));
                bean.setSexId(job.getString("SexId"));
                list.add(bean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 解析二级菜单返回的数据
     *
     * @param json 要解析数据
     */
    public List<CategorySecondMenuBean> getSecondMenuParseJson(String json) {
        List<CategorySecondMenuBean> list = new ArrayList<>();
        if (json == null || json.equals("")) {
            //如果json为空直接返货这个空的list
            return list;
        }
        try {
            JSONObject jsonObject = new JSONObject(json);
            String code = jsonObject.getString(HttpModel.SUCCESSFULLY);
            if (code.equals(HttpModel.SUCCESSFULLY_NO)) {
                return list;
            }
            JSONArray jsonArray = jsonObject.getJSONArray(HttpModel.KEY_VALUE);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject job = jsonArray.getJSONObject(i);
               CategorySecondMenuBean bean = new CategorySecondMenuBean();
                bean.set_id(job.getString("_id"));
                bean.setName(job.getString("name"));
                bean.setCategoryId(job.getString("CategoryId"));
                list.add(bean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 解析热门品牌返回的数据
     * @param result
     */
    public List<CategoryHotBrandBean> getHotBrandToJson(String result) {
        List<CategoryHotBrandBean> list = new ArrayList<>();
        if (result == null || result.equals("")) {
            //如果json为空直接返货这个空的list
            return list;
        }
        try {
            JSONObject jsonObject = new JSONObject(result);
            String code = jsonObject.getString(HttpModel.SUCCESSFULLY);
            if (HttpModel.SUCCESSFULLY_NO.equals(code)){
                return list;
            }
            JSONArray jsonArray = jsonObject.getJSONArray(HttpModel.HOT_BRAND_KEY);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject job = jsonArray.getJSONObject(i);
                CategoryHotBrandBean brandBean = new CategoryHotBrandBean();
                brandBean.set_id(job.getString("_id"));
                brandBean.setName(job.getString("name"));
                brandBean.setValue(job.getString("value"));
                brandBean.setLetter(job.getString("letter"));
                brandBean.setHotflag(job.getString("hotflag"));
                brandBean.setCategoryId(job.getString("categoryId"));
                brandBean.setImgpath(job.getString("imgpath"));
                list.add(brandBean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
    /**
     * 解析所有品牌返回的数据
     * @param result
     */
    public List<CategoryAllBrandBean> getAllBrandToJson(String result) {
        List<CategoryAllBrandBean> list = new ArrayList<>();
        if (result == null || result.equals("")) {
            //如果json为空直接返货这个空的list
            return list;
        }
        try {
            JSONObject jsonObject = new JSONObject(result);
            String code = jsonObject.getString(HttpModel.SUCCESSFULLY);
            if (HttpModel.SUCCESSFULLY_NO.equals(code)){
                return list;
            }
            JSONArray jsonArray = jsonObject.getJSONArray(HttpModel.HOT_BRAND_KEY);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject job = jsonArray.getJSONObject(i);
                CategoryAllBrandBean brandBean = new CategoryAllBrandBean();
                brandBean.set_id(job.getString("_id"));
                brandBean.setName(job.getString("name"));
                brandBean.setValue(job.getString("value"));
                brandBean.setLetter(job.getString("letter"));
                brandBean.setHotflag(job.getString("hotflag"));
                brandBean.setCategoryId(job.getString("categoryId"));
                list.add(brandBean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 解析商品列表返回的数据
     * @param result
     */
    public BrandGoodsInfo getBrandGoodsInfo(String result) {
        BrandGoodsInfo brandGoodsInfo = new BrandGoodsInfo();
        if (result == null || result.equals("")){
            return brandGoodsInfo;
        }
        try {
            JSONObject job = new JSONObject(result);
            if (job.getString(HttpModel.SUCCESSFULLY).equals(HttpModel.SUCCESSFULLY_NO)) {
                return brandGoodsInfo;
            }
            brandGoodsInfo.setBrandname(job.getString("brandname"));
            String goods = job.getString("goods");
            if (!goods.equals("[]")) {//如果返回的数据中是存在内容的
                JSONArray array = new JSONArray(goods);
                List<BrandGoodsInfo.GoodsBean> list = new ArrayList<BrandGoodsInfo.GoodsBean>();
                for (int i = 0; i < array.length(); i++) {
                    //获取在jsonArray中每一个jsonObject
                    JSONObject jsonObject = array.getJSONObject(i);
                    BrandGoodsInfo.GoodsBean bean = new BrandGoodsInfo.GoodsBean();
                    bean.set_id(jsonObject.getString("_id"));
                    bean.setPrice(jsonObject.getString("price"));
                    bean.setTitle(jsonObject.getString("title"));
                    bean.setDiscount(jsonObject.getString("discount"));
                    bean.setImgpath(jsonObject.getString("imgpath"));
                    list.add(bean);
                }
                //将解析出来的集合数据放在goodsInfo、对象中
                brandGoodsInfo.setGoods(list);
            }
        
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return brandGoodsInfo;
    }

    /**
     * 解析商品详细信息数据
     * @param result 要解析的json
     * @return 返回GoodsDetailBean
     */
    public GoodsDetailBean getGoodsDetailToJson(String result) {
       if (result == null || result.equals("")){
           return null;
       }
        GoodsDetailBean bean = new GoodsDetailBean();
        try {
            JSONObject jsonObject = new JSONObject(result);
            if (jsonObject.getString(HttpModel.SUCCESSFULLY).equals(HttpModel.SUCCESSFULLY_NO)){
                return null;
            }
            String goods = jsonObject.getString("goods");
            if (goods.equals("[]")){
                return null;
            }
            JSONArray goodsArray = new JSONArray(goods);
            JSONObject goodsJob = goodsArray.getJSONObject(0);
            bean.setId(goodsJob.getString("_id"));
            bean.setTitle(goodsJob.getString("title"));
            bean.setPrice(goodsJob.getString("price"));
            bean.setDiscount(goodsJob.getString("discount"));

            //解析viewpager的图片数据
            JSONArray imgArray = jsonObject.getJSONArray("img");
            List<String> imgList = new ArrayList<>();
            for (int i = 0; i < imgArray.length(); i++) {
                JSONObject imgJob = imgArray.getJSONObject(i);
                imgList.add(imgJob.getString("imgpath"));
            }
            bean.setImgList(imgList);

            //解析listView的图片数据
            JSONArray imgValueArray = jsonObject.getJSONArray("imgvale");
            List<String> imgValueList = new ArrayList<>();
            for (int i = 0; i < imgValueArray.length(); i++) {
                JSONObject imgValueJob = imgValueArray.getJSONObject(i);
                imgValueList.add(imgValueJob.getString("imgpath"));
            }
            bean.setImgvalueList(imgValueList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bean;
    }
}


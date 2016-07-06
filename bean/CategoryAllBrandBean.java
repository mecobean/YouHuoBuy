package cn.bjsxt.youhuo.bean;

/**
 * 所有品牌的数据实体类
 */
public class CategoryAllBrandBean {

    /**
     * _id : 1
     * name : nike
     * value : nike
     * letter : n
     * hotflag : 0
     * categoryId : null
     */

    private String _id;
    private String name;
    private String value;
    private String letter;
    private String hotflag;
    private Object categoryId;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public String getHotflag() {
        return hotflag;
    }

    public void setHotflag(String hotflag) {
        this.hotflag = hotflag;
    }

    public Object getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Object categoryId) {
        this.categoryId = categoryId;
    }
}

package cn.bjsxt.youhuo.bean;

/**
 * 二级菜单数据基类
 */
public class CategorySecondMenuBean {

    /**
     * _id : 1
     * name : TShirt
     * CategoryId : 1
     */

    private String _id;
    private String name;
    private String CategoryId;

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

    public String getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(String CategoryId) {
        this.CategoryId = CategoryId;
    }
}

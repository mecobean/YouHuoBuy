package cn.bjsxt.youhuo.bean;

/**
 * 一级菜单数据基类
 */
public class CategoryFirstMenuBean {

    /**
     * _id : 1
     * name : 上衣
     * SexId : 1
     */

    private String _id;
    private String name;
    private String SexId;
    /**
     * 是否选中
     */
    private boolean isSelect;

    public boolean getSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

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

    public String getSexId() {
        return SexId;
    }

    public void setSexId(String SexId) {
        this.SexId = SexId;
    }
}

package cn.bjsxt.youhuo.bean;

import java.util.List;

/**
 * order activity expandableListView data
 */
public class OrderExpandableLvBean {
    private String group;
    private List<String> child;

    public OrderExpandableLvBean() {
    }

    public OrderExpandableLvBean(String group, List<String> child) {
        this.group = group;
        this.child = child;
    }

    public List<String> getChild() {
        return child;
    }
    public void setChild(List<String> child) {
        this.child = child;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}

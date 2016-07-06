package cn.bjsxt.youhuo.bean;

/**
 * 用户信息实体类
 */
public class UserInfoBean {
    private String name;
    private String id;
    private String headPath;

    public UserInfoBean() {
    }

    public UserInfoBean(String headPath, String id, String name) {
        this.headPath = headPath;
        this.id = id;
        this.name = name;
    }

    public String getHeadPath() {
        return headPath;
    }

    public void setHeadPath(String headPath) {
        this.headPath = headPath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

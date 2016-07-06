package cn.bjsxt.youhuo.bean;

/**
 * 首页listView的item 的实体类
 */
public class HomeLvItemBean {
    private String firstIvPath;
    private String secondIvPath;
    private String thirdIvPath;
    private String fourIvPath;
    private String fiveIvPath;
    private String sixIvPath;

    public HomeLvItemBean(String firstIvPath, String fiveIvPath, String fourIvPath, String secondIvPath, String sixIvPath, String thirdIvPath) {
        this.firstIvPath = firstIvPath;
        this.fiveIvPath = fiveIvPath;
        this.fourIvPath = fourIvPath;
        this.secondIvPath = secondIvPath;
        this.sixIvPath = sixIvPath;
        this.thirdIvPath = thirdIvPath;
    }

    public HomeLvItemBean() {
    }

    public String getFirstIvPath() {
        return firstIvPath;
    }

    public void setFirstIvPath(String firstIvPath) {
        this.firstIvPath = firstIvPath;
    }

    public String getFiveIvPath() {
        return fiveIvPath;
    }

    public void setFiveIvPath(String fiveIvPath) {
        this.fiveIvPath = fiveIvPath;
    }

    public String getFourIvPath() {
        return fourIvPath;
    }

    public void setFourIvPath(String fourIvPath) {
        this.fourIvPath = fourIvPath;
    }

    public String getSecondIvPath() {
        return secondIvPath;
    }

    public void setSecondIvPath(String secondIvPath) {
        this.secondIvPath = secondIvPath;
    }

    public String getSixIvPath() {
        return sixIvPath;
    }

    public void setSixIvPath(String sixIvPath) {
        this.sixIvPath = sixIvPath;
    }

    public String getThirdIvPath() {
        return thirdIvPath;
    }

    public void setThirdIvPath(String thirdIvPath) {
        this.thirdIvPath = thirdIvPath;
    }

}

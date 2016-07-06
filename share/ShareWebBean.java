package cn.bjsxt.youhuo.share;

/**
 * 分享 web 数据参数实体类
 */
public class ShareWebBean {
    /**
     * 分享的文本
     */
    private String text;
    /**
     * 分享的标题
     */
    private String title;
    /**
     * 分享的图片路径
     */
    private String imgURL;
    /**
     * 分享的网页路径
     */
    private String webURL;

    public ShareWebBean() {
    }

    public ShareWebBean(String imgURL, String text, String title, String webURL) {
        this.imgURL = imgURL;
        this.text = text;
        this.title = title;
        this.webURL = webURL;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWebURL() {
        return webURL;
    }

    public void setWebURL(String webURL) {
        this.webURL = webURL;
    }
}

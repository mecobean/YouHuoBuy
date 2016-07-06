package cn.bjsxt.youhuo.bean;

/**
 *
 */
public class VersionBean {

    /**
     * sucess : ok
     * version : 2
     * versionvalue : 修复已有bug
     * url : http://192.168.1.207/yoho.apk
     */

    private String sucess;
    private String version;
    private String versionvalue;
    private String url;

    public String getSucess() {
        return sucess;
    }

    public void setSucess(String sucess) {
        this.sucess = sucess;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersionvalue() {
        return versionvalue;
    }

    public void setVersionvalue(String versionvalue) {
        this.versionvalue = versionvalue;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

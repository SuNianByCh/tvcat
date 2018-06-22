package com.tvcat.beans;

/**
 * Created by sunian on 2018/6/22.
 */

public class LookHistParseBean  {

    /**
     * url : http://m.tvcat.co/parseurl/error?url=http://m.iqiyi.com/v_19rr16bewk.html&token=105426426a014f98b4291d18e0971cda
     * type :
     * src_url : http://m.iqiyi.com/v_19rr16bewk.html
     * title : 锦衣卫之王者归来-电影-高清完整版视频在线观看–爱奇艺
     * success : 0
     * progress : 0
     */

    private String url;
    private String type;
    private String src_url;
    private String title;
    private String success;
    private String progress;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSrc_url() {
        return src_url;
    }

    public void setSrc_url(String src_url) {
        this.src_url = src_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }
}

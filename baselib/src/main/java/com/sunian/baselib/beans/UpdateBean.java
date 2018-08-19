package com.sunian.baselib.beans;

import java.util.List;

public class UpdateBean {

    /**
     * id : 2
     * version : 1.1.0
     * os : Android
     * changelog : ["测试版本更新","测试版本更新2"]
     * app_url : http://hb-assets.small-best.com/uploads/app_version/app_file/2/a700b60c-d61d-4759-bbe3-e2f69760c4d2.apk?e=1841060712&token=TL7vgIdADfCg9dJGncUGqvj51t0JfO8IORBBO9JX:u4Eeo39DqaVROFRfRsNmZHqiZRM=
     * must_upgrade : false
     */

    private int id;
    private String version;
    private String os;
    private String app_url;
    private boolean must_upgrade;
    private List<String> changelog;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getApp_url() {
        return app_url;
    }

    public void setApp_url(String app_url) {
        this.app_url = app_url;
    }

    public boolean isMust_upgrade() {
        return must_upgrade;
    }

    public void setMust_upgrade(boolean must_upgrade) {
        this.must_upgrade = must_upgrade;
    }

    public List<String> getChangelog() {
        return changelog;
    }

    public void setChangelog(List<String> changelog) {
        this.changelog = changelog;
    }
}

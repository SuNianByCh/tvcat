package com.sunian.baselib.beans;

import java.util.List;

public class ConfigBean {

    /**
     * explore_url : http://videoh5.eastday.com/?qid=lsshipin
     * kefu_url : http://www.sina.com.cn
     * aboutus_url : http://tvcat.small-best.com/p/aboutus
     * faq_url : http://tvcat.small-best.com/p/faq
     * ad_blacklist : ["http://img3.xthanbai.com/","http://123.56.6.60/crone"]
     * ad_script : var $el = $('a[id^=__a_z_]'); $el.hide();
     */

    private String explore_url;
    private String kefu_url;
    private String aboutus_url;
    private String faq_url;
    private String ad_script;
    private String download_url;
    private List<String> ad_blacklist;

    public String getExplore_url() {
        return explore_url;
    }

    public void setExplore_url(String explore_url) {
        this.explore_url = explore_url;
    }

    public String getKefu_url() {
        return kefu_url;
    }

    public void setKefu_url(String kefu_url) {
        this.kefu_url = kefu_url;
    }

    public String getAboutus_url() {
        return aboutus_url;
    }

    public void setAboutus_url(String aboutus_url) {
        this.aboutus_url = aboutus_url;
    }

    public String getFaq_url() {
        return faq_url;
    }

    public void setFaq_url(String faq_url) {
        this.faq_url = faq_url;
    }

    public String getAd_script() {
        return ad_script;
    }

    public void setAd_script(String ad_script) {
        this.ad_script = ad_script;
    }

    public List<String> getAd_blacklist() {
        return ad_blacklist;
    }

    public void setAd_blacklist(List<String> ad_blacklist) {
        this.ad_blacklist = ad_blacklist;
    }

    public String getDownload_url() {
        return download_url;
    }

    public void setDownload_url(String download_url) {
        this.download_url = download_url;
    }
}

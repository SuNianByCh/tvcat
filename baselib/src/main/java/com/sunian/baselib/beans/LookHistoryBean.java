package com.sunian.baselib.beans;

/**
 * 观看记录
 */
public class LookHistoryBean {

    /**
     * id : 18050650671407430
     * title : 《无问西东》“静坐听雨”正片片段
     * source_url : http://m.iqiyi.com/v_19rrfj85t4.html
     * progress :
     * time : 2018-05-06 14:04:31
     * provider : {"id":320908,"name":"爱奇艺","icon":"http://hb-assets.small-best.com/uploads/media_provider/1/large_b1cc4a3a-47fc-42cd-ba0b-fa0288749c98.jpg?e=1840947906&token=TL7vgIdADfCg9dJGncUGqvj51t0JfO8IORBBO9JX:igIIsd6i_Xq7iA_RX6a1tY3YLzY=","url":"http://m.iqiyi.com/dianying/"}
     */

    private String id;
    private String title;
    private String source_url;
    private String progress;
    private String time;
    private ProviderBean provider;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSource_url() {
        return source_url;
    }

    public void setSource_url(String source_url) {
        this.source_url = source_url;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public ProviderBean getProvider() {
        return provider;
    }

    public void setProvider(ProviderBean provider) {
        this.provider = provider;
    }

    public static class ProviderBean {
        /**
         * id : 320908
         * name : 爱奇艺
         * icon : http://hb-assets.small-best.com/uploads/media_provider/1/large_b1cc4a3a-47fc-42cd-ba0b-fa0288749c98.jpg?e=1840947906&token=TL7vgIdADfCg9dJGncUGqvj51t0JfO8IORBBO9JX:igIIsd6i_Xq7iA_RX6a1tY3YLzY=
         * url : http://m.iqiyi.com/dianying/
         */

        private int id;
        private String name;
        private String icon;
        private String url;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}

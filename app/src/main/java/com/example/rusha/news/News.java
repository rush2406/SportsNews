package com.example.rusha.news;

/**
 * Created by rusha on 5/24/2017.
 */

public class News {

    private String thumbnail;
    private String secname;
    private String webtitle;
    private String weburl;
    private String date;

    public News(String secname, String webtitle, String weburl, String date, String thumbnail) {
        this.secname = secname;
        this.webtitle = webtitle;
        this.weburl = weburl;
        this.date = date;
        this.thumbnail = thumbnail;
    }

    public String getWeburl() {
        return weburl;
    }

    public String getWebtitle() {

        return webtitle;
    }

    public String getSecname() {

        return secname;
    }

    public String getDate() {
        return date;
    }

    public String getThumbnail() {
        return thumbnail;
    }
}

package com.han.tools.crawler;

/**
 * Created by hanjunnan on 2017/9/3.
 */
public class NovalTemplate {

    private String listSelector;

    private String titleSelector;

    private String articleSelector;

    private String encoding;

    public NovalTemplate(String listSelector, String titleSelector, String articleSelector, String encoding) {
        this.listSelector = listSelector;
        this.titleSelector = titleSelector;
        this.articleSelector = articleSelector;
        this.encoding = encoding;
    }

    public String getListSelector() {
        return listSelector;
    }

    public String getTitleSelector() {
        return titleSelector;
    }

    public String getArticleSelector() {
        return articleSelector;
    }

    public String getEncoding() {
        return encoding;
    }
}

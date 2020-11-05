package com.han.tools.crawler;

import com.han.tools.util.HttpUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by hanjunnan on 2017/4/7.
 */
public class NovalCrawler {

    public static void main(String args[]) throws IOException {
        String url = "http://www.23us.so/files/article/html/12/12061/index.html";
        String file = "NRBDT.txt";
        NovalTemplate template = templateMap.get("23us");
        String baseUrl = "";

//        String url = "http://www.xs222.tw/html/14/14729/";
//        String file = "老衲要还俗.txt";
//
//        String baseUrl = "http://www.xs222.tw";
//        NovalTemplate template = templateMap.get("xs222");

        String startUrl = "http://www.xs222.tw/html/14/14729/9376884.html";

        boolean isStart = false;

        Map<String, String> map = getArticleList(url, baseUrl, template);
        PrintWriter writer = new PrintWriter(new FileWriter(file));
        for (Map.Entry<String, String> entry : map.entrySet()) {
            /*if (!isStart && !startUrl.equals(entry.getValue())) {
                continue;
            }*/

            isStart = true;

            while (!printAtricla(entry.getValue(), template, writer)) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        writer.flush();
        writer.close();
    }

    static Map<String, NovalTemplate> templateMap = new HashMap<String, NovalTemplate>();

    static {
        templateMap.put("23us", new NovalTemplate("table#at > tbody > tr > td.L > a", "dd > h1", "dd#contents", "UTF-8"));
        templateMap.put("xs222", new NovalTemplate("div#list > dl", "div.bookname > h1", "div#content", "GBK"));
    }

    public static Map<String, String> getArticleList(String listUrl, String baseUrl, NovalTemplate template) {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();

        try {
            HttpResponse resp = HttpUtils.getContent(0, 10, listUrl, null);
            HttpEntity entity = resp.getEntity();
            String respStr = EntityUtils.toString(entity, template.getEncoding());

            Document document = Jsoup.parse(respStr);
            Elements elements = document.select(template.getListSelector());

            for (Element element : elements) {
                String url = baseUrl + element.attr("href");
                String title = element.text();
                System.out.println(title + ":" + url);

                map.put(title, url);

                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }

    public static boolean printAtricla(String url, NovalTemplate template, PrintWriter writer) {
        String title = null;
        try {
            HttpResponse resp = HttpUtils.getContent(0, 10, url, null);
            HttpEntity entity = resp.getEntity();
            String respStr = EntityUtils.toString(entity, template.getEncoding());

            Document document = Jsoup.parse(respStr);
            title = document.select(template.getTitleSelector()).text();
            String content = document.select(template.getArticleSelector()).html().replaceAll("<br>", "").replaceAll("&nbsp;", "");

            writer.println(title);
            writer.println();
            writer.println(content);

            writer.flush();
            System.out.println(title + " : Finished");
            return true;
        } catch (Exception e) {
            System.err.println("[" + url + ":" + title + "] ERROR" + e.getMessage());
            return false;
        }
    }
}

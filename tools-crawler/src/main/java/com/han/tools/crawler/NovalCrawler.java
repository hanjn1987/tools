package com.han.tools.crawler;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.xml.transform.Templates;
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
//        String url = "http://www.23us.cc/html/101/101573/";
//        String file = "修真聊天群.txt";
//        NovalTemplate template = templateMap.get("23us");

        String url = "http://www.xs222.tw/html/14/14729/";
        String file = "老衲要还俗.txt";

        String baseUrl = "http://www.xs222.tw";
        NovalTemplate template = templateMap.get("xs222");

        String startUrl = "http://www.xs222.tw/html/14/14729/9376884.html";

        CloseableHttpClient httpClient = HttpClients.createDefault();
        boolean isStart = false;

        Map<String, String> map = getArticleList(url, baseUrl, template, httpClient);
        PrintWriter writer = new PrintWriter(new FileWriter(file));
        for (Map.Entry<String, String> entry : map.entrySet()) {
            /*if (!isStart && !startUrl.equals(entry.getValue())) {
                continue;
            }*/

            isStart = true;

            while (!printAtricla(entry.getValue(), template, writer, httpClient)) {
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
        templateMap.put("23us", new NovalTemplate("div#main > div.inner > dl.chapterlist", "div#main > div#BookCon > h1", "div#main > div#BookCon > div#content", "UTF-8"));
        templateMap.put("xs222", new NovalTemplate("div#list > dl", "div.bookname > h1", "div#content", "GBK"));
    }

    public static Map<String, String> getArticleList(String listUrl, String baseUrl, NovalTemplate template, CloseableHttpClient httpClient) {
        HttpGet httpGet = new HttpGet(listUrl);
        CloseableHttpResponse resp = null;

        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();

        try {
            resp = httpClient.execute(httpGet);
            HttpEntity entity = resp.getEntity();
            String respStr = EntityUtils.toString(entity, template.getEncoding());

            Document document = Jsoup.parse(respStr);
            Elements elements = document.select(template.getListSelector());
            Element list = elements.first();

            for (Element element : list.getElementsByTag("a")) {
                String url = baseUrl + element.attr("href");
                String title = element.text();
                System.out.println(title + ":" + url);

                map.put(title, url);

                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                resp.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return map;
    }

    public static boolean printAtricla(String url, NovalTemplate template, PrintWriter writer, CloseableHttpClient httpClient) {
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse resp = null;
        String title = null;
        try {
            resp = httpClient.execute(httpGet);
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
        } finally {
            try {
                resp.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}

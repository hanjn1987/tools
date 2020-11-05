package com.han.tools.util;

import org.apache.commons.collections.MapUtils;
import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class HttpUtils {

    private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    /**
     * 公用HttpClient,用于下发调用内部HTTP服务（EDI请求转发）,以及回传调用商家HTTP服务
     */
    private static HttpClient httpClient;
    private static RequestConfig requestConfig;

    static {
        ConnectionConfig connectionConfig = ConnectionConfig.custom()
                .setCharset(Consts.UTF_8)
                .build();

        PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager();
        manager.setDefaultConnectionConfig(connectionConfig);
        manager.setValidateAfterInactivity(100000);
        manager.setMaxTotal(64);
        manager.setDefaultMaxPerRoute(64);

        httpClient = HttpClientBuilder.create().setConnectionManager(manager).build();

        requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(10000)
                .setConnectTimeout(10000)
                .setSocketTimeout(50000)
                .build();
    }

    public static HttpResponse getContent(long tId, String url, Map<String, String> headers) {
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(requestConfig);
        if (MapUtils.isNotEmpty(headers)) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpGet.setHeader(entry.getKey(), entry.getValue());
            }
        }
        HttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(httpGet);
        } catch (Exception e) {
            logger.error(String.format("TID:%s|Get Http Response Error!!", tId, e));
        }

        return httpResponse;
    }

    public static HttpResponse getContent(long tId, int retry, String url, Map<String, String> headers) {
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(requestConfig);
        if (MapUtils.isNotEmpty(headers)) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpGet.setHeader(entry.getKey(), entry.getValue());
            }
        }
        HttpResponse httpResponse = null;
        while (retry > 0) {
            try {
                httpResponse = httpClient.execute(httpGet);
                if (null != httpResponse && 200 == httpResponse.getStatusLine().getStatusCode()) {
                    break;
                }
            } catch (Exception e) {
                logger.error(String.format("TID:%s|Retry:%s|Execute Get Error!", tId, retry, e));
            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                logger.error(String.format("TID:%s|Retry:%s|Thread Sleep Error!", tId, retry, e));
            }
            retry--;
        }

        return httpResponse;
    }

    public static HttpResponse postContent(long tId, int retry, String url, String content, Map<String, String> headers) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);
        if (MapUtils.isNotEmpty(headers)) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }
        }

        HttpResponse httpResponse = null;
        while (retry > -1) {
            try {
                httpPost.setEntity(new StringEntity(content));
                httpResponse = httpClient.execute(httpPost);
                if (null != httpResponse && 200 == httpResponse.getStatusLine().getStatusCode()) {
                    break;
                }
            } catch (Exception e) {
                logger.error(String.format("TID:%s|Retry:%s|Execute Post Error!", tId, retry, e));
            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                logger.error(String.format("TID:%s|Retry:%s|Thread Sleep Error!", tId, retry, e));
            }
            retry--;
        }

        return httpResponse;
    }
}

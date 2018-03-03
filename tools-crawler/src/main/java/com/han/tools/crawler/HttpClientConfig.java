package com.han.tools.crawler;

import org.apache.http.Consts;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import javax.annotation.PostConstruct;

/**
 * HttpClient初始化配置类
 * Created by hanjunnan on 2018/1/17.
 */
public class HttpClientConfig {

    /**
     * 公用HttpClient,用于下发调用内部HTTP服务（EDI请求转发）,以及回传调用商家HTTP服务
     */
    private static HttpClient httpClient;

    private static RequestConfig requestConfig;

    static  {
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
                .setConnectionRequestTimeout(5000)
                .setConnectTimeout(5000)
                .setSocketTimeout(20000)
                .build();
    }

    public static HttpClient getHttpClient() {
        return httpClient;
    }

    public static RequestConfig getRequestConfig() {
        return requestConfig;
    }
}

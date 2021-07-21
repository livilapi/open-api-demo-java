package com.paybank.javademo.modular.demo.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class HttpUtil {

    private static final Logger log = LoggerFactory.getLogger(HttpUtil.class);

    public static final String DEFAULT_CHARSET = "UTF-8";

    private static final RequestConfig REQUEST_CONFIG = RequestConfig.custom()
        .setConnectTimeout(30000)
        .setConnectionRequestTimeout(5000)
        .setSocketTimeout(30000).build();

    private static CloseableHttpClient createSslClientDefault()
        throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
        SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, (TrustStrategy) (chain, authType) -> true).build();
        HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;
        SSLConnectionSocketFactory sslref = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
        return HttpClients.custom().setSSLSocketFactory(sslref).build();
    }

    public static String doPostJson(String url, Object param) {
        return doPostJson(url, param, null, DEFAULT_CHARSET);
    }

    public static String doPostJson(String url, Object param, Map<String, String> headers, String charset) {
        if (StringUtils.isBlank(charset)) {
            charset = DEFAULT_CHARSET;
        }

        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        String result = "";
        try {
            if (url.toLowerCase().startsWith("https")) {
                httpClient = createSslClientDefault();
            } else {
                httpClient = HttpClients.createDefault();
            }
            HttpPost httpPost = new HttpPost(url);
            if (null != headers) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    httpPost.setHeader(entry.getKey(), entry.getValue());
                }
            }
            String json = JacksonUtil.obj2json(param);
            StringEntity requestEntity = new StringEntity(json, charset);
            requestEntity.setContentEncoding(charset);
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setEntity(requestEntity);
            httpPost.setConfig(REQUEST_CONFIG);
            response = httpClient.execute(httpPost);
            if (null != response.getEntity()) {
                result = EntityUtils.toString(response.getEntity(), DEFAULT_CHARSET);
            }
            if (response.getStatusLine().getStatusCode() != HttpStatus.OK.value()) {
                log.error("{} Network request error, status code:{}", url, response.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            log.error("{}Network request error:{}", url, e.getMessage());
        } finally {
            closeClient(response, httpClient);
        }
        return result;
    }

    private static void closeClient(CloseableHttpResponse response, CloseableHttpClient httpclient) {
        try {
            if (null != response) {
                if (null != response.getEntity()) {
                    EntityUtils.consume(response.getEntity());
                }
                response.close();
            }
            if(null != httpclient) {
                httpclient.close();
            }
        } catch (IOException e) {
            log.error("Close HttpClient Fail:{}", e);
        }
    }
}

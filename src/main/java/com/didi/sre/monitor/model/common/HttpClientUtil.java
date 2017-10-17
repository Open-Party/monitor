package com.didi.sre.monitor.model.common;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author soarpenguin on 17-8-27.
 */
public class HttpClientUtil {
    private static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

    /**
     * post request
     */
    @SuppressWarnings("unchecked")
    public static byte[] postRequest(String reqURL, Object data) throws Exception {
        byte[] responseBytes = null;

        HttpPost httpPost = new HttpPost(reqURL);
        CloseableHttpClient httpClient = HttpClients.custom().disableAutomaticRetries().build();	// disable retry

        try {
            // timeout
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(10000)
                    .setSocketTimeout(10000)
                    .setConnectTimeout(10000)
                    .build();

            httpPost.setConfig(requestConfig);

            // data
            if (data != null) {
                if(data.getClass().isArray() && byte[].class.isAssignableFrom(data.getClass())) {
                    httpPost.setEntity(new ByteArrayEntity((byte[])data, ContentType.DEFAULT_BINARY));
                } else if(Collection.class.isAssignableFrom(data.getClass())) {
                    List<NameValuePair> nvps = new ArrayList<>();

                    Set<String> keySet = ((Map<String, String>)data).keySet();
                    for (String key : keySet) {
                        nvps.add(new BasicNameValuePair(key, ((Map<String, String>)data).get(key)));
                    }

                    httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.DEF_CONTENT_CHARSET));
                } else {
                    throw new Exception("UnSupport type for request parameter.");
                }
            }
            // do post
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                responseBytes = EntityUtils.toByteArray(entity);
                EntityUtils.consume(entity);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        } finally {
            httpPost.releaseConnection();
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return responseBytes;
    }

    /**
     * read bytes from http request
     * @param request
     * @return
     * @throws IOException
     */
    public static final byte[] readBytes(HttpServletRequest request) throws IOException {
        request.setCharacterEncoding("UTF-8");
        int contentLen = request.getContentLength();
        InputStream is = request.getInputStream();
        if (contentLen > 0) {
            int readLen = 0;
            int readLengthThisTime = 0;
            byte[] message = new byte[contentLen];
            try {
                while (readLen != contentLen) {
                    readLengthThisTime = is.read(message, readLen, contentLen - readLen);
                    if (readLengthThisTime == -1) {
                        break;
                    }
                    readLen += readLengthThisTime;
                }
                return message;
            } catch (IOException e) {
                e.printStackTrace();
                throw e;
            }
        }
        return new byte[] {};
    }
}

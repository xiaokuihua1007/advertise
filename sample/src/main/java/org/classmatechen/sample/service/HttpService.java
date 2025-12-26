package org.classmatechen.sample.service;

import java.io.IOException;
import java.io.InputStream;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.springframework.stereotype.Component;

@Component
public class HttpService {

    @SuppressWarnings("deprecation")
    public String download(String url, String suffix, FileService fileService) throws Exception {

        String result;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {

            HttpGet httpGet = new HttpGet(url);
            httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");

            HttpClientResponseHandler<String> handler = response -> {
    
                String suffixx = suffix;
                int statusCode = response.getCode();
                if (statusCode != HttpStatus.SC_OK) {
                    EntityUtils.consume(response.getEntity());
                    return null;
                }

                HttpEntity entity = response.getEntity();
                if (entity == null) {
                    return null;
                }

                String contentType = entity.getContentType();
                if (null == suffixx && null != contentType) {
                    int index = contentType.lastIndexOf('/');
                    if (index > -1) {
                        suffixx = "." + contentType.substring(index + 1);
                    } else {
                        suffixx = "." + contentType;
                    }
                }
                // if (contentType == null || !contentType.contains("image")) {
                //     EntityUtils.consume(entity);
                //     return null;
                // }

                String path;
                try (InputStream stream = entity.getContent()) {

                    path = fileService.save(stream, suffixx);
                } catch (IOException e) {
                    throw e;
                } finally {
                    EntityUtils.consume(entity);
                }
                return path;
            };

            result = handler.handleResponse(httpClient.execute(httpGet));

        } catch (IOException e) {
            throw e;
        }
        if (null == result) {
            throw new RuntimeException();
        }
        return result;
    }
}

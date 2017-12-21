package com.navimee.services;

import com.navimee.contracts.services.HttpClient;
import com.navimee.logger.LogTypes;
import com.navimee.logger.Logger;
import com.navimee.models.entities.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.Callable;

@Service
public class HttpClientImpl implements HttpClient {

    private CloseableHttpAsyncClient client;

    public HttpClientImpl() {
        client = createClient();
    }

    @Override
    public Callable<JSONObject> GET(URI uri) {
        return () -> runRequest(new HttpGet(uri));
    }

    @Override
    public Callable<JSONObject> GET(HttpGet httpGet) {
        return () -> runRequest(httpGet);
    }

    @Override
    public void close() {
        try {
            client.close();
        } catch (IOException e) {
            Logger.LOG(new Log(LogTypes.EXCEPTION, e));
        }
    }

    private JSONObject runRequest(HttpGet request) {
        try {
            HttpResponse response = client.execute(request, null).get();
            HttpEntity entity = response.getEntity();
            String json = EntityUtils.toString(entity, "UTF-8");
            EntityUtils.consume(entity);

            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
                throw new Exception(json);

            return new JSONObject(json);
        } catch (Exception e) {
            Logger.LOG(new Log(LogTypes.EXCEPTION, e));
            return new JSONObject();
        } finally {
            request.releaseConnection();
        }
    }

    private CloseableHttpAsyncClient createClient() {
        HttpAsyncClientBuilder httpClientBuilder =
                HttpAsyncClients.custom()
                        .setMaxConnTotal(100)
                        .setMaxConnPerRoute(100)
                        .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy());

        //CloseableHttpAsyncClient httpClient = httpClientBuilder.build();
        CloseableHttpAsyncClient httpClient = HttpAsyncClients.createDefault();

        httpClient.start();

        return httpClient;
    }
}

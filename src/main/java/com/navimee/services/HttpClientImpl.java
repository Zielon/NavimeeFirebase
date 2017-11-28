package com.navimee.services;

import com.navimee.contracts.services.HttpClient;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class HttpClientImpl implements HttpClient {

    CloseableHttpAsyncClient client;

    public HttpClientImpl(){
        client = createClient();
    }

    @Override
    public Callable<JSONObject> GET(URI uri) {
        return () -> {
            HttpGet request = new HttpGet(uri);
            Future<HttpResponse> future = client.execute(request, null);
            HttpEntity entity = future.get().getEntity();
            String json = EntityUtils.toString(entity, "UTF-16");
            return new JSONObject(json);
        };
    }

    @Override
    public void close() {
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private CloseableHttpAsyncClient createClient() {
        IOReactorConfig ioReactor = IOReactorConfig.custom().setIoThreadCount(10).build();
        HttpAsyncClientBuilder httpClientBuilder =
                HttpAsyncClients.custom()
                        .setMaxConnTotal(10)
                        .setMaxConnPerRoute(1000)
                        .setDefaultIOReactorConfig(ioReactor)
                        .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy());

        CloseableHttpAsyncClient httpClient = httpClientBuilder.build();
        //CloseableHttpAsyncClient httpClient = HttpAsyncClients.createDefault();

        httpClient.start();

        return httpClient;
    }
}

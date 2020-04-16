package com.example;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class Client {
    private static final Logger log = LoggerFactory.getLogger(Client.class);
    private static final String SLOW_SERVICE_ENDPOINT = "http://localhost:8889/slow-endpoint";
    private static final String FAST_SERVICE_ENDPOINT = "http://localhost:8889/fast-endpoint";
    public static void main(String[] args) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder().url(SLOW_SERVICE_ENDPOINT).build();
        Instant connectionEnd = null;
        Instant connectionStart = null;
        try {
            log.info("starting connection");
            connectionStart = Instant.now();
            Response response = client.newCall(request).execute();
            log.info("RESPONSE: {}", new String(response.body().bytes()));
            log.info("ending connection");
            connectionEnd = Instant.now();
        } catch (IOException e) {
            e.printStackTrace();
            log.info("ending connection - error");
            connectionEnd = Instant.now();
        }
        log.info("conection total time: {}", Duration.between(connectionStart, connectionEnd).getSeconds());
    }
}

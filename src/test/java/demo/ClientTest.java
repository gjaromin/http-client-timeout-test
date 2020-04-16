package demo;


import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

@SpringBootTest(classes = Config.class)
public class ClientTest {

    private static final String SLOW_SERVICE_ENDPOINT = "http://localhost:8889/slow-endpoint";
    private static final String FAST_SERVICE_ENDPOINT = "http://localhost:8889/fast-endpoint";
    private static final String LARGE_FILE_SERVICE_ENDPOINT = "http://localhost:8889/large-file-endpoint";

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void test() {

        ResponseEntity<String> response
                = restTemplate.getForEntity(LARGE_FILE_SERVICE_ENDPOINT, String.class);

        System.out.println(response.getBody());
    }
}

@Configuration
class Config {

    @Bean
//    @Primary
    public RestTemplate restTemplate() {
        return new RestTemplate(getClientHttpRequestFactory());
    }

    private ClientHttpRequestFactory getClientHttpRequestFactory() {
        int timeout = 5000;
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory
                = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(2_000);
        clientHttpRequestFactory.setReadTimeout(2_000);
        return clientHttpRequestFactory;
    }

    @Bean
    @Primary
    public RestTemplate okhttp3Template() {
        RestTemplate restTemplate = new RestTemplate();

        // create the okhttp client builder
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        ConnectionPool okHttpConnectionPool = new ConnectionPool(50, 30, TimeUnit.SECONDS);
        builder.connectionPool(okHttpConnectionPool);
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(5, TimeUnit.SECONDS);
        builder.retryOnConnectionFailure(false);

        // embed the created okhttp client to a spring rest template
        restTemplate.setRequestFactory(new OkHttp3ClientHttpRequestFactory(builder.build()));

        return restTemplate;
    }
}

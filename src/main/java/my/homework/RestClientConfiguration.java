package my.homework;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class RestClientConfiguration {

    private final RestClientSettings restClientSettings;

    @Autowired
    public RestClientConfiguration(RestClientSettings restClientSettings) {
        this.restClientSettings = restClientSettings;
    }

    @Bean
    public RestTemplate getRestTemplate(ClientHttpRequestFactory clientHttpRequestFactory) {
        return new RestTemplate(clientHttpRequestFactory);
    }

    @Bean
    public ClientHttpRequestFactory getClientHttpRequestFactory(HttpClient httpClient) {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);

        factory.setConnectTimeout(restClientSettings.getConnectionTimeoutMillis());
        factory.setReadTimeout(restClientSettings.getReadTimeoutMillis());

        return factory;
    }

    @Bean
    public HttpClient getHttpClient() {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setDefaultMaxPerRoute(restClientSettings.getMaxConnectionsPerRoute());
        connectionManager.setMaxTotal(restClientSettings.getMaxTotalConnections());

        return HttpClientBuilder.create()
            .setConnectionManager(connectionManager)
            .build();
    }

}

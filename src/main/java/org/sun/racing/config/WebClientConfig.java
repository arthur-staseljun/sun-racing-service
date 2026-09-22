package org.sun.racing.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfig {

    @Value("${engine.service-url}")
    private String engineServiceUrl;

    @Value("${engine.service-fallback-url}")
    private String engineFallbackServiceUrl;

    @Value("${reporting.service-url}")
    private String reportingServiceUrl;

    @Value("${reporting.service-fallback-url}")
    private String reportingFallbackServiceUrl;

    @Bean
    public WebClient defaultWebClient() {
        return buildWebClient(engineServiceUrl, 1_000, 2_000, 2_000);
    }

    @Bean
    public WebClient fallbackWebClient() {
        return buildWebClient(engineFallbackServiceUrl, 1_000, 3_000, 3_000);
    }

    @Bean
    public WebClient defaultReportingWebClient() {
        return buildWebClient(reportingServiceUrl, 10_000, 20_000, 20_000);
    }

    @Bean
    public WebClient fallbackReportingWebClient() {
        return buildWebClient(reportingFallbackServiceUrl, 10_000, 30_000, 30_000);
    }

    private  WebClient buildWebClient(String url, int connectionTimeout,
                                      long readTimeout, long responseTimeout) {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectionTimeout)
                .responseTimeout(Duration.ofMillis(responseTimeout))
                .doOnConnected(connection ->
                        connection.addHandlerLast(new ReadTimeoutHandler(readTimeout, TimeUnit.MILLISECONDS)));

        return WebClient.builder()
                .baseUrl(url)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
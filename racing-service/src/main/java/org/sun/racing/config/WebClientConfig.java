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
    private String defaultServiceUrl;

    @Value("${engine.service-fallback-url}")
    private String fallbackServiceUrl;

    @Bean
    public WebClient defaultWebClient() {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10_000)
                .responseTimeout(Duration.ofMillis(20_000))
                .doOnConnected(connection ->
                        connection.addHandlerLast(new ReadTimeoutHandler(10_000, TimeUnit.MILLISECONDS)));

        return WebClient.builder()
                .baseUrl(defaultServiceUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    @Bean
    public WebClient fallbackWebClient() {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10_000)
                .responseTimeout(Duration.ofMillis(20_000))
                .doOnConnected(connection ->
                        connection.addHandlerLast(new ReadTimeoutHandler(20_000, TimeUnit.MILLISECONDS)));

        return WebClient.builder()
                .baseUrl(fallbackServiceUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
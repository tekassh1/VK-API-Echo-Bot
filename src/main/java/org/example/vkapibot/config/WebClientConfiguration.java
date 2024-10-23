package org.example.vkapibot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;


import java.time.Duration;

@Configuration
public class WebClientConfiguration {
    @Value("${vk.api.method.url}") private String baseUrl;
    @Value("${server.access.token}") String serverAccessToken;

    @Bean
    public WebClient getWebClient() {
        HttpClient client = HttpClient.create().responseTimeout(Duration.ofMillis(2000));

        return WebClient.builder()
                .baseUrl(baseUrl)
                .filter(ExchangeFilterFunction.ofRequestProcessor(
                        clientRequest -> {
                            ClientRequest newRequest = ClientRequest.from(clientRequest)
                                    .header("Authorization", "Bearer " + serverAccessToken)
                                    .build();
                            return Mono.just(newRequest);
                        }))
                .clientConnector(new ReactorClientHttpConnector(client))
                .build();
    }
}

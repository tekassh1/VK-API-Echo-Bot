package org.example.vkapibot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.vkapibot.controller.MainController;
import org.example.vkapibot.model.responses.users_response.UserData;
import org.example.vkapibot.model.responses.users_response.UsersResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import org.springframework.http.HttpStatusCode;

@Service
public class UsersService {

    WebClient webClient;

    @Value("${vk.api.get.user.method}") String getUserMethod;
    @Value("${vk.api.version}") String vkApiVersion;

    private static final Logger logger = LoggerFactory.getLogger(UsersService.class);

    @Autowired
    public UsersService(WebClient webClient) {
        this.webClient = webClient;
    }

    public UserData getUserData(int userId) throws JsonProcessingException {
        String res = webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(getUserMethod)
                        .queryParam("v", vkApiVersion)
                        .queryParam("user_ids", userId)
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse ->
                        Mono.error(new HttpClientErrorException(clientResponse.statusCode()))
                )
                .bodyToMono(String.class)
                .block();

        ObjectMapper objectMapper = new ObjectMapper();
        UsersResponse response = objectMapper.readValue(res, UsersResponse.class);

        if (response.getError() != null)
            logger.error("Request error: {}", response.getError().getErrorMessage());

        return new UserData(response.getResponse().getFirst().getFirstName(),
                            response.getResponse().getFirst().getLastName());
    }
}

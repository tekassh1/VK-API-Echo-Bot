package org.example.vkapibot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.vkapibot.model.responses.users_response.UserData;
import org.example.vkapibot.model.responses.users_response.UsersResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class UsersService {

    WebClient webClient;

    @Value("${vk.api.get.user.method}") String getUserMethod;
    @Value("${vk.api.version}") String vkApiVersion;

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
                .bodyToMono(String.class)
                .block();

        ObjectMapper objectMapper = new ObjectMapper();
        UsersResponse response = objectMapper.readValue(res, UsersResponse.class);

        return new UserData(response.getResponse().getFirst().getFirstName(),
                            response.getResponse().getFirst().getLastName());
    }
}

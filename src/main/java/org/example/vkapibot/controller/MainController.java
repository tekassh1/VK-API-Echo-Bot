package org.example.vkapibot.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.vkapibot.VkApiBotApplication;
import org.example.vkapibot.model.responses.DefaultResponse;
import org.example.vkapibot.model.responses.callback.Callback;
import org.example.vkapibot.model.responses.callback.RequestType;
import org.example.vkapibot.model.responses.error.VkError;
import org.example.vkapibot.model.responses.users_response.UserData;
import org.example.vkapibot.model.responses.users_response.UsersResponse;
import org.example.vkapibot.service.UsersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
public class MainController {

    @Value("${vk.api.version}") String vkApiVersion;
    @Value("${server.confirmation.token}") String serverConfirmationToken;
    @Value("${vk.api.send.message.method}") String vkApiSendMessageMethod;

    WebClient webClient;
    UsersService usersService;

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @Autowired
    public MainController(WebClient webClient, UsersService usersService) {
        this.webClient = webClient;
        this.usersService = usersService;
    }

    @PostMapping
    public ResponseEntity<String> botRequest(@RequestBody Callback callback) throws JsonProcessingException {
         if (callback.getType() == null) {
            return ResponseEntity.badRequest().body("Request type not set.");
        }
        else if (callback.getType().equals(RequestType.CONFIRMATION.label)) {
            return ResponseEntity.ok(serverConfirmationToken);
        }
        else if (callback.getType().equals(RequestType.MESSAGE_NEW.label)) {

            int userId = callback.getVkObject().getMessage().getFromId();
            String userMessageText = callback.getVkObject().getMessage().getText();
            int peerId = callback.getVkObject().getMessage().getPeerId();
            long randomId = System.currentTimeMillis();

            String res;

            if (peerId >= 2000000000) {
                UserData userData = usersService.getUserData(userId);
                String responseText = userData.getFirstName() + " " +
                                      userData.getLastName() +
                                      " сказал: " +
                                      userMessageText;

                res = webClient
                        .post()
                        .uri(uriBuilder -> uriBuilder
                                .path(vkApiSendMessageMethod)
                                .queryParam("v", vkApiVersion)
                                .queryParam("random_id", randomId)
                                .queryParam("peer_id", peerId)
                                .queryParam("message", responseText)
                                .build())
                        .retrieve()
                        .onStatus(HttpStatusCode::isError, clientResponse ->
                                Mono.error(new HttpClientErrorException(clientResponse.statusCode()))
                        )
                        .bodyToMono(String.class)
                        .block();
            }
            else {
                String responseText = "Вы сказали: " + userMessageText;

                res = webClient
                        .post()
                        .uri(uriBuilder -> uriBuilder
                                .path(vkApiSendMessageMethod)
                                .queryParam("v", vkApiVersion)
                                .queryParam("user_id", userId)
                                .queryParam("random_id", randomId)
                                .queryParam("message", responseText)
                                .build())
                        .retrieve()
                        .onStatus(HttpStatusCode::isError, clientResponse ->
                                Mono.error(new HttpClientErrorException(clientResponse.statusCode()))
                        )
                        .bodyToMono(String.class)
                        .block();
            }

            ObjectMapper objectMapper = new ObjectMapper();
            DefaultResponse response = objectMapper.readValue(res, DefaultResponse.class);
            if (response.getError() != null)
                logger.error("Request error: {}", response.getError().getErrorMessage());

            return ResponseEntity.ok("ok");
        }
        else {
            logger.warn("Bot doesn't support {} event.", callback.getType());
            return ResponseEntity.ok("ok");
        }
    }
}
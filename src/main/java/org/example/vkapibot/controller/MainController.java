package org.example.vkapibot.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.vkapibot.model.responses.callback.Callback;
import org.example.vkapibot.model.responses.callback.RequestType;
import org.example.vkapibot.model.responses.users_response.UserData;
import org.example.vkapibot.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
public class MainController {

    @Value("${vk.api.version}") String vkApiVersion;
    @Value("${server.confirmation.token}") String serverConfirmationToken;
    @Value("${vk.api.send.message.method}") String vkApiSendMessageMethod;

    WebClient webClient;
    UsersService usersService;

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

            if (peerId >= 2000000000) {
                UserData userData = usersService.getUserData(userId);
                String responseText = userData.getFirstName() + " " +
                                      userData.getLastName() +
                                      " сказал: " +
                                      userMessageText;

                String res = webClient
                        .post()
                        .uri(uriBuilder -> uriBuilder
                                .path(vkApiSendMessageMethod)
                                .queryParam("v", vkApiVersion)
                                .queryParam("random_id", randomId)
                                .queryParam("peer_id", peerId)
                                .queryParam("message", responseText)
                                .build())
                        .retrieve()
                        .bodyToMono(String.class)
                        .block();
            }
            else {
                String responseText = "Вы сказали: " + userMessageText;

                String res = webClient
                        .post()
                        .uri(uriBuilder -> uriBuilder
                                .path(vkApiSendMessageMethod)
                                .queryParam("v", vkApiVersion)
                                .queryParam("user_id", userId)
                                .queryParam("random_id", randomId)
                                .queryParam("message", responseText)
                                .build())
                        .retrieve()
                        .bodyToMono(String.class)
                        .block();
            }
            return ResponseEntity.ok("ok");
        }
        else {
            return ResponseEntity.ok("ok");
        }
    }
}
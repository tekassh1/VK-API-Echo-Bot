package org.example.vkapibot.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.vkapibot.VkApiBotApplication;
import org.example.vkapibot.model.responses.callback.Callback;
import org.example.vkapibot.model.responses.callback.RequestType;
import org.example.vkapibot.model.responses.error.Error;
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
import org.example.vkapibot.model.responses.Response;
import org.springframework.http.HttpStatus;

@RestController
public class MainController {

    private final String vkApiVersion;
    private final String serverConfirmationToken;
    private final String vkApiSendMessageMethod;
    private final String secret;
    
    WebClient webClient;
    UsersService usersService;

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    static final int GROUPS_STARTS_FROM = 2000000000;

    @Autowired
    public MainController(
        WebClient webClient,
        UsersService usersService,
        @Value("${vk.api.version}") String vkApiVersion,
        @Value("${server.confirmation.token}") String serverConfirmationToken,
        @Value("${vk.api.send.message.method}") String vkApiSendMessageMethod,
        @Value("${server.secret.key}") String secret
    ) {
        this.webClient = webClient;
        this.usersService = usersService;
        this.vkApiVersion = vkApiVersion;
        this.serverConfirmationToken = serverConfirmationToken;
        this.vkApiSendMessageMethod = vkApiSendMessageMethod;
        this.secret = secret;
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
            
            if (!secret.equals(callback.getSecret())) {
                logger.warn("Wrong secret key: {}", callback.getSecret());
                return new ResponseEntity<String>("Wrong secret.", HttpStatus.UNAUTHORIZED);
            }

            int userId = callback.getVkObject().getMessage().getFromId();
            String userMessageText = callback.getVkObject().getMessage().getText();
            int peerId = callback.getVkObject().getMessage().getPeerId();
            long randomId = System.currentTimeMillis();

            String res;
            
            // Для бесед должно показываться имя пользователя, а не "вы". (Беседы начинаются с 2000000000)

            String responseText;
            if (peerId >= GROUPS_STARTS_FROM) {
                UserData userData = usersService.getUserData(userId);
                responseText = userData.getFirstName() + " " + userData.getLastName() + " сказал(a): " + userMessageText;
            } else {
                responseText = "Вы сказали: " + userMessageText;
            }

            String recipientParam = (peerId >= GROUPS_STARTS_FROM) ? "peer_id" : "user_id";

            res = webClient
                    .post()
                    .uri(uriBuilder -> uriBuilder
                            .path(vkApiSendMessageMethod)
                            .queryParam("v", vkApiVersion)
                            .queryParam("random_id", randomId)
                            .queryParam(recipientParam, peerId >= GROUPS_STARTS_FROM ? peerId : userId)
                            .queryParam("message", responseText)
                            .build())
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, clientResponse ->
                            Mono.error(new HttpClientErrorException(clientResponse.statusCode()))
                    )
                    .bodyToMono(String.class)
                    .block();

            ObjectMapper objectMapper = new ObjectMapper();
            Response response = objectMapper.readValue(res, Response.class);
            if (response.getError() != null)
                logger.error("Request error: {}", response.getError().getErrorMessage());
            
            logger.info("Response from VK: {}", res);
            return ResponseEntity.ok("ok");
        }
        else {
            logger.warn("Bot doesn't support {} event.", callback.getType());
            return ResponseEntity.ok("ok");
        }
    }
}
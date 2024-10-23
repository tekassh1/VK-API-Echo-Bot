package org.example.vkapibot;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.vkapibot.controller.MainController;
import org.example.vkapibot.model.responses.callback.Callback;
import org.example.vkapibot.model.responses.callback.RequestType;
import org.example.vkapibot.model.responses.callback.VkObject;
import org.example.vkapibot.service.UsersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;

class MainControllerTest {

    @Mock
    private WebClient webClient;

    @Mock
    private UsersService usersService;

    private MainController mainController;

    // Значения для теста
    private static final String SERVER_CONFIRMATION_TOKEN = "test-confirmation-token";
    private static final String VK_API_VERSION = "5.200";
    private static final String VK_API_SEND_MESSAGE_METHOD = "/test/messages.send";
    private static final String SECRET_KEY = "test-secret-key";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Создание экземпляра контроллера с тестовыми значениями
        mainController = new MainController(webClient, usersService, VK_API_VERSION, SERVER_CONFIRMATION_TOKEN, VK_API_SEND_MESSAGE_METHOD, SECRET_KEY);
    }

    @Test
    void testBotRequest_WithNullType() {
        Callback callback = new Callback();
        callback.setType(null);

        try {
            ResponseEntity<String> response = mainController.botRequest(callback);
            assertEquals(400, response.getStatusCodeValue());
            assertEquals("Request type not set.", response.getBody());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testBotRequest_ConfirmationType() {
        Callback callback = new Callback();
        callback.setType(RequestType.CONFIRMATION.label);

        try {
            ResponseEntity<String> response = mainController.botRequest(callback);
            assertEquals(200, response.getStatusCodeValue());
            assertEquals(SERVER_CONFIRMATION_TOKEN, response.getBody());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testBotRequest_MessageNewType_InvalidSecret() {
        Callback callback = new Callback();
        callback.setType(RequestType.MESSAGE_NEW.label);
        callback.setSecret("invalid-secret");

        VkObject vkObject = new VkObject();
        callback.setVkObject(vkObject);

        try {
            ResponseEntity<String> response = mainController.botRequest(callback);
            assertEquals(401, response.getStatusCodeValue());
            assertEquals("Wrong secret.", response.getBody());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testBotRequest_UnsupportedType() {
        Callback callback = new Callback();
        callback.setType("unsupported_type");

        try {
            ResponseEntity<String> response = mainController.botRequest(callback);
            assertEquals(200, response.getStatusCodeValue());
            assertEquals("ok", response.getBody());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}

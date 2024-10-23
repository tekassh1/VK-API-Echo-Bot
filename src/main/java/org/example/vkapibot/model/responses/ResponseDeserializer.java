package main.java.org.example.vkapibot.model.responses;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.example.vkapibot.model.responses.ResponseBody;

public class ResponseDeserializer extends JsonDeserializer<ResponseBody> {
    @Override
    public ResponseBody deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JsonProcessingException {
        
        ResponseBody responseBody = new ResponseBody();
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        
        if (node.isInt()) {
            int messageId = node.asInt();
            return new ResponseBody(messageId, null);
        }
        else if (node.isArray()) {
            ObjectMapper objectMapper = new ObjectMapper();
            List<MessageResponse> responses = new ArrayList<>();

            for (JsonNode objNode : node) {
                MessageResponse response = objectMapper.treeToValue(objNode, MessageResponse.class);
                responses.add(response);
            }

            responseBody.setResponses(responses);
        }
        return responseBody;
    }
}
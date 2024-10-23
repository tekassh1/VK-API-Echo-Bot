package org.example.vkapibot.model.responses;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import main.java.org.example.vkapibot.model.responses.MessageResponse;
import main.java.org.example.vkapibot.model.responses.ResponseDeserializer;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonDeserialize(using = ResponseDeserializer.class)
public class ResponseBody {
    private int messageId;
    private List<MessageResponse> responses;
}

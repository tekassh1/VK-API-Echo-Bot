package main.java.org.example.vkapibot.model.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {
    
    @JsonProperty("peer_id")
    private int peerId;
    
    @JsonProperty("message_id")
    private int messageId;
    
    @JsonProperty("conversation_message_id")
    private int conversationMessageId;
    
    @JsonProperty("error")
    private String error;
}
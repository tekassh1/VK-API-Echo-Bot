package org.example.vkapibot.model.responses.callback;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private int date;

    @JsonProperty("from_id")
    private int fromId;

    private int id;
    private int out;
    private int version;
    private List<String> attachments;
    private int conversationMessageId;

    @JsonProperty("fwd_messages")
    private List<String> fwdMessages;

    private boolean important;

    @JsonProperty("is_hidden")
    private boolean isHidden;

    @JsonProperty("peer_id")
    private int peerId;

    @JsonProperty("random_id")
    private int randomId;

    private String text;

    @JsonProperty("is_mentioned_user")
    private boolean isMentionedUser;
}
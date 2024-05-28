package org.example.vkapibot.model.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Response {

    @JsonProperty("cmid")
    private int cmid;

    @JsonProperty("message_id")
    private int messageId;
}

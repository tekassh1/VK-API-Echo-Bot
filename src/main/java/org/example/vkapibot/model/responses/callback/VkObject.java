package org.example.vkapibot.model.responses.callback;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VkObject {
    Message message;

    @JsonProperty("client_info")
    ClientInfo clientInfo;
}
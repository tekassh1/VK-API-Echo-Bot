package org.example.vkapibot.model.responses.callback;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.vkapibot.model.responses.error.Error;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Callback {

    @JsonProperty("group_id")
    private String groupId;

    private String type;

    private String event_id;

    private String v;

    @JsonProperty("object")
    private VkObject vkObject;

    private Error error;

    private String secret;
}
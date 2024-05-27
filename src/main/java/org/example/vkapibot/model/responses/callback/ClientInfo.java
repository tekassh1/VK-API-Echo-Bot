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
public class ClientInfo {

    @JsonProperty("button_actions")
    private List<String> buttonActions;

    private boolean keyboard;

    @JsonProperty("inline_keyboard")
    private boolean inlineKeyboard;

    private boolean carousel;

    @JsonProperty("lang_id")
    private int langId;
}
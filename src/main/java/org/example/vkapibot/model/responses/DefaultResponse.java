package org.example.vkapibot.model.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.vkapibot.model.responses.error.VkError;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DefaultResponse {

    private VkError error;
}
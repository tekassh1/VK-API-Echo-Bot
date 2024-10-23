package org.example.vkapibot.model.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.vkapibot.model.responses.error.Error;
import org.example.vkapibot.model.responses.ResponseBody;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Response {

    private Error error;
    private ResponseBody response;
}
package org.example.vkapibot.model.responses.users_response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.vkapibot.model.responses.error.Error;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsersResponse {

    private ArrayList<UserData> response;
    private Error error;
}
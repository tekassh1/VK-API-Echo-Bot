package org.example.vkapibot.model.responses.users_response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserData {
    int id;

    @JsonProperty("first_name")
    String firstName;

    @JsonProperty("last_name")
    String lastName;

    @JsonProperty("can_access_closed")
    boolean canAccessClosed;

    @JsonProperty("is_closed")
    boolean isClosed;

    public UserData(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
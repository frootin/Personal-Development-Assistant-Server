package ru.sfu.objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserDto {
    private String email;
    private String username;
    private String password;
    @JsonProperty("repeat_password")
    private String repeatPassword;
    private String timezone;

    // getters and setters here...
}

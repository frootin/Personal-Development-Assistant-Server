package ru.sfu.objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequestDto {
    //@NotNull @Email @Length(min = 5, max = 50)
    private String email;

    //@NotNull @Length(min = 5, max = 10)
    private String password;

    private String username;

    // getters and setters are not shown...
}

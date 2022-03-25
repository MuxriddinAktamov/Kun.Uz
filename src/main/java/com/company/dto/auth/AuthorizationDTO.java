package com.company.dto.auth;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Setter
@Getter
public class AuthorizationDTO {

    @NotEmpty(message = "Login qani")
    @Size(min = 6, max = 15, message = "Login 5-15 oraliqda bo'lishi kerak")
    private String login;
    @NotEmpty(message = "Password qani")
    @Size(min = 5, max = 15, message = "password 5-15 oraliqda emas")
    private String password;

}

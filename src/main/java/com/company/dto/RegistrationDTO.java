package com.company.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegistrationDTO {

    private Integer id;
    @NotEmpty(message = "Kalla name qani")
    private String name;
    @NotEmpty(message = "Kalla surname qani")
    private String surname;
    @Email
    private String email;
    @NotEmpty(message = "Login qani")
    @Size(min = 6, max = 15, message = "Login 5-15 oraliqda bo'lishi kerak")
    private String login;
    @NotEmpty(message = "Password qani")
    @Size(min = 5, max = 15, message = "Password 5-15 oraliqda emas")
    private String password;

}

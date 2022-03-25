package com.company.dto;

import com.company.enums.ProfileRole;
import com.company.enums.ProfileStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileDTO {
    private Integer id;
    @NotEmpty(message = "name is Null or is Empty")
    private String name;
    @NotEmpty(message = "surname is Null or is Empty")
    private String surname;
    @NotEmpty(message = "Login is Null or is Empty")
    @Size(min = 5, max = 15, message = "Login 5-15 oralig'ida bo'lsa kerak")
    private String login;
    @NotEmpty(message = "password is Null or is Empty")
    @Size(min = 5, max = 15, message = "password 5-15 oralig'ida bo'lsa kerak")
    private String pswd;
    @NotEmpty(message = "Email is NUll or is Empty")
    private String email;
    @NotEmpty(message = "role is empty or is null")
    private ProfileRole role;
    @NotEmpty(message = "status is Null or is Empty")
    private ProfileStatus status;

    private String jwt; // token
}

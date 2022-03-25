package com.company.dto;

import com.company.enums.ProfileRole;
import com.company.enums.ProfileStatus;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Getter
@Setter
public class ProfileFilterDTO {
    private Integer profileId;
    @NotEmpty(message = "name is Null or is Empty")
    private String name;
    @NotEmpty(message = "surname is Null or is Empty")
    private String surname;
    @NotEmpty(message = "Email is NUll or is Empty")
    private String email;
    //    @NotEmpty(message = "role is empty or is null")
    private ProfileRole role;
    //    @NotEmpty(message = "status is Null or is Empty")
    private ProfileStatus status;
    @Past
    private LocalDate fromDate;
    @FutureOrPresent
    private LocalDate toDate;

}

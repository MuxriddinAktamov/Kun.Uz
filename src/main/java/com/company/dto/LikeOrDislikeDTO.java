package com.company.dto;

import com.company.enums.ActionStatus;
import com.company.enums.LikeOrDislikeStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LikeOrDislikeDTO {

    private Integer id;
    //    @NotEmpty(message = "action Id is Null or is empty")
    @Positive
    private Integer actionId;
    @NotEmpty(message = "actionStatus is Null or is empty")
    private ActionStatus actionStatus;
    //    @NotEmpty(message = "profileId is Null or Is empty")
    @Positive
    private Integer profileId;
    @PastOrPresent
    private LocalDateTime created_date;
    @NotEmpty(message = "status is Null or Is empty")
    private LikeOrDislikeStatus status;

}

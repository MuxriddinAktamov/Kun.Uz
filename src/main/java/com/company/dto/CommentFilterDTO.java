package com.company.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Getter
@Setter
public class CommentFilterDTO {
    private Integer commentId;
    @NotEmpty(message = "content is Null or is Empty")
    private String content;
    @Positive
    private Integer profileId;
    @Positive
    private Integer articleId;
    @Past
    private LocalDate fromDate;
    @FutureOrPresent
    private LocalDate toDate;

}

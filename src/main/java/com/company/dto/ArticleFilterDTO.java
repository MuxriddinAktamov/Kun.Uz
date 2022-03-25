package com.company.dto;

import com.company.enums.ArticleStatus;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Getter
@Setter
public class ArticleFilterDTO {
    @NotEmpty(message = "Title is Null or is Empty")
    private String title;
    @Positive
    private Integer profileId;
    @Positive
    private Integer articleId;
    //    @NotEmpty(message = "status is Null or is Empty")
    private ArticleStatus status;
    @Past
    private LocalDate fromDate;
    @FutureOrPresent
    private LocalDate toDate;
}

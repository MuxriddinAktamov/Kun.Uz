package com.company.dto;

import com.company.entity.RegionEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleDTO {
    private Integer id;

    @NotEmpty(message = "Title is empty or null")
    @NotNull
    private String title;
    @NotEmpty(message = "Content is Empty or null")
    private String content;
    @Positive
    private Integer profileId;
    //    @NotEmpty(message = "profileId is null or empty")
    @Positive
    private Integer region;

    // status
    @PastOrPresent
    private LocalDateTime createdDate;
    @PastOrPresent
    private LocalDateTime publishedDate;

}

package com.company.dto;

import com.company.entity.ArticleEntity;
import com.company.entity.ProfileEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentDTO {

    private Integer id;
    @NotEmpty(message = "Content is Empty or null")
    private String content;
    //    @NotEmpty(message = "articleId is null or is empty")
    @Positive
    private Integer articleId;
    //    @NotEmpty(message = "profileId is null or is empty")
    @Positive
    private Integer profileId;
    @PastOrPresent
    private LocalDateTime creare_date;

}

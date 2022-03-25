package com.company.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "coment")
public class CommentEntity extends BaseEntity {

    @Column
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private ArticleEntity articleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private ProfileEntity profileId;


}

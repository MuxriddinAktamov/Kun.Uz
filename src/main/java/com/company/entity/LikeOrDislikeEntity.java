package com.company.entity;

import com.company.enums.ActionStatus;
import com.company.enums.LikeOrDislikeStatus;
import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "LikeOrDislike")
public class LikeOrDislikeEntity extends BaseEntity {

    @JoinColumn(name = "action_id")
    private Integer actionId;

    @Enumerated(EnumType.STRING)
    @Column
    private ActionStatus actionStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profileId")
    private ProfileEntity profileId;

    @Enumerated(EnumType.STRING)
    @Column
    private LikeOrDislikeStatus status;
}

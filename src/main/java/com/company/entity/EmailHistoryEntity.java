package com.company.entity;

import com.company.enums.EmailUsedStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "emailHistory")
public class EmailHistoryEntity extends BaseEntity {

    @Column
    private String toEmail;

    @Column
    private String fromEmail;

    @Enumerated(EnumType.STRING)
    @Column
    private EmailUsedStatus status;

    @Column
    private LocalDateTime used_Date;

}

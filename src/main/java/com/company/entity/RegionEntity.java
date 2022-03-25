package com.company.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.TimeZone;

@Setter
@Getter
@Entity
@Table(name = "region")
public class RegionEntity extends BaseEntity{
    @Column
    private String region;


}

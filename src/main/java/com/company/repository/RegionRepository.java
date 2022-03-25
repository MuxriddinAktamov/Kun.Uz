package com.company.repository;

import com.company.entity.RegionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

public interface RegionRepository extends JpaRepository<RegionEntity, Integer> {

    Optional<RegionEntity> findByRegion(String region);

    @Transactional
    @Modifying
    @Query("update RegionEntity p set p.region=:region, p.createdDate=:date where p.region=:regionfrom")
    int update(@Param("region") String region, @Param("date")
            LocalDateTime date, @Param("regionfrom") String regionfrom);

    @Transactional
    @Modifying
    @Query(value = "delete from RegionEntity s where s.region=:region")
    int deleteByRegion(@Param("region") String region);
}

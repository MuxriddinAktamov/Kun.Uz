package com.company.repository;

import com.company.entity.EmailHistoryEntity;
import com.company.enums.EmailUsedStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EmailHistoryRepository extends JpaRepository<EmailHistoryEntity,Integer> {
    Optional<EmailHistoryEntity> findByToEmail(String email);

    @Query("select s from EmailHistoryEntity s where s.createdDate between :date and :date2")
    List<EmailHistoryEntity> findByCrated_date(@Param("date") LocalDateTime dateTime, @Param("date2") LocalDateTime dateTime1);

    @Query("select s from EmailHistoryEntity s order by s.createdDate desc ")
    Optional<EmailHistoryEntity> findByIdOrderByIdDesc();

//    Optional<EmailHistoryEntity> findTop1ByOrderByCrated_dateDesc();

    @Query("select s from EmailHistoryEntity s where s.status=:status")
    List<EmailHistoryEntity> findByStatus(@Param("status") EmailUsedStatus status, Pageable pageable);
}

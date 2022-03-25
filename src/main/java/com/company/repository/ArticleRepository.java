package com.company.repository;

import com.company.entity.ArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<ArticleEntity, Integer>, JpaSpecificationExecutor<ArticleEntity> {


    Optional<ArticleEntity> findByProfileId(Integer profileId);

    Optional<ArticleEntity> findByContent(String content);

    @Transactional
    @Modifying
    @Query("update ArticleEntity p set p.title=:title,p.publisher.id=:publishId,p.content=:content,p.createdDate=:createdate," +
            "p.publishedDate=:pulishdate,p.profile.id=:profileid,p.region.id=:region where p.content=:content")
    Optional<ArticleEntity> update(@Param("title")String title, @Param("publishId")Integer publisher,
                                   @Param("createdate") LocalDateTime createdate, @Param("pulishdate")LocalDateTime pulishdate,
                                   @Param("profileid")Integer profileId, @Param("content")String content, @Param("region")Integer region);

    @Transactional
    @Modifying
    @Query(value = "delete from ArticleEntity s where s.content=:content")
    int deleteByContent(@Param("content") String content);
}

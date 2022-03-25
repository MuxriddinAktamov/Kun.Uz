package com.company.repository;

import com.company.entity.ArticleEntity;
import com.company.entity.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<CommentEntity, Integer>, JpaSpecificationExecutor<CommentEntity> {


    Page<CommentEntity> findAll(Pageable pageable);

    @Query("Select s from CommentEntity s where s.profileId=:profileId")
    Page<CommentEntity> findByProfileId(@Param("profileId") Integer profileId, Pageable pageable);

    @Query("Select s from CommentEntity s where s.articleId=:articleId")
    Page<CommentEntity> findByArticleId(@Param("articleId") Integer articleId, Pageable pageable);

    Optional<CommentEntity> findByContent(String content);

    @Transactional
    @Modifying
    @Query("update CommentEntity p set p.articleId.id=:articleId,p.profileId.id=:profileId,p.content=:content1,p.createdDate=:createdate" +
            " where p.content=:content")
    Optional<CommentEntity> update(@Param("articleId") Integer articleId, @Param("profileId") Integer profileId,
                                   @Param("createdate") LocalDateTime createdate, @Param("content1") String content1, @Param("content") String content);

    @Transactional
    @Modifying
    @Query(value = "delete from CommentEntity s where s.content=:content")
    int deleteByContent(@Param("content") String content);
}

package com.company.repository;

import com.company.dto.ArticleFilterDTO;
import com.company.dto.CommentFilterDTO;
import com.company.entity.ArticleEntity;
import com.company.entity.CommentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CommentCustomRepositoryIml {
    @Autowired
    private EntityManager entityManager;

    public PageImpl filter1(int page, int size, CommentFilterDTO filterDTO) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder builder = new StringBuilder("SELECT a FROM CommentEntity a ");
        StringBuilder builderCount = new StringBuilder("SELECT count(a) FROM CommentEntity a ");

        if (filterDTO.getCommentId() != null) {
            builder.append("where a.id =:id");
            builderCount.append(" where a.id =:id");
            params.put("id", filterDTO.getCommentId());
        } else {
            builder.append("Where a.id >" + 0);
            builderCount.append("Where a.id >" + 0);
            params.put("id", filterDTO.getCommentId());
        }

        if (filterDTO.getArticleId() != null) {
            builder.append(" and a.articleId.id =:articleId");
            builderCount.append(" and a.articleId.id =:articleId");
            params.put("articleId", filterDTO.getArticleId());
        }
        if (filterDTO.getContent() != null && !filterDTO.getContent().isEmpty()) {
            builder.append(" and a.content =:content");
            builderCount.append(" and a.content =:content");
            params.put("content", filterDTO.getContent());
        }

        if (filterDTO.getProfileId() != null) {
            builder.append(" and a.profileId.id =:profileId");
            builderCount.append(" and a.profileId.id =:profileId");
            params.put("profileId", filterDTO.getProfileId());
        }
        if (filterDTO.getFromDate() != null) {
            builder.append(" and a.createdDate >=:fromDate");
            builderCount.append(" and a.createdDate >=:fromDate");
            params.put("fromDate", LocalDateTime.of(filterDTO.getFromDate(), LocalTime.MIN));
        }

        if (filterDTO.getToDate() != null) {
            builder.append(" and a.createdDate <=:toDate");
            builderCount.append(" and a.createdDate <=:toDate");
            params.put("toDate", LocalDateTime.of(filterDTO.getToDate(), LocalTime.MAX));
        }

        Query query = entityManager.createQuery(builder.toString());
        query.setFirstResult((page - 1) * size);
        query.setMaxResults(size);

        for (Map.Entry<String, Object> entrySet : params.entrySet()) {
            query.setParameter(entrySet.getKey(), entrySet.getValue());
        }
        List<CommentEntity> commentList = query.getResultList();


        query = entityManager.createQuery(builderCount.toString());
        for (Map.Entry<String, Object> entrySet : params.entrySet()) {
            query.setParameter(entrySet.getKey(), entrySet.getValue());
        }
        Long totalCount = (Long) query.getSingleResult();

        return new PageImpl(commentList, PageRequest.of(page, size), totalCount);
    }

    public PageImpl<CommentEntity> filter(int page, int size, CommentFilterDTO dto) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<CommentEntity> criteriaQuery = criteriaBuilder.createQuery(CommentEntity.class);
        Root<CommentEntity> root = criteriaQuery.from(CommentEntity.class);
        criteriaQuery.select(root);
        Predicate nameAndIdPre = fixed(root, dto);
//        Predicate ordercommentId = (Predicate) criteriaBuilder.desc(root.get("commentId"));
//        Predicate orderArticleid = (Predicate) criteriaBuilder.desc(root.get("articleId"));
//        Predicate orderProfileId = (Predicate) criteriaBuilder.desc(root.get("profileId"));
//        Predicate orderDatetId = (Predicate) criteriaBuilder.desc(root.get("fromDate"));

//        Predicate ordercommentIdAsc = (Predicate) criteriaBuilder.asc(root.get("commentId"));
//        Predicate orderArticleidAsc = (Predicate) criteriaBuilder.asc(root.get("articleId"));
//        Predicate orderProfileIdAsc = (Predicate) criteriaBuilder.asc(root.get("profileId"));
//        Predicate orderDatetIdAsc = (Predicate) criteriaBuilder.asc(root.get("fromDate"));

//        Predicate nameAndIdPre = criteriaBuilder.and(namePred, idPredicate);

        //        criteriaQuery.orderBy((Order) ordercommentId, (Order) orderArticleid, (Order) orderProfileId, (Order) orderDatetId);
//        criteriaQuery.orderBy(DescOrder);
        criteriaQuery.where(nameAndIdPre);
//        DescOrder(root);
//        AscOrder(root);
        Query query = entityManager.createQuery(criteriaQuery.toString());
        query.setFirstResult((page - 1) * size);
        query.setMaxResults(size);
        Long total = (Long) query.getSingleResult();

        List<CommentEntity> commentList = entityManager.createQuery(criteriaQuery).getResultList();
        return new PageImpl(commentList, PageRequest.of(page, size), total);
    }


    public CriteriaQuery<CommentEntity> DescOrder(Root<CommentEntity> root) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<CommentEntity> criteriaQuery = criteriaBuilder.createQuery(CommentEntity.class);

        Order ordercommentId = criteriaBuilder.desc(root.get("id"));
        Order orderArticleid = criteriaBuilder.desc(root.get("articleId.id"));
        Order orderProfileId = criteriaBuilder.desc(root.get("profileId.id"));
        Order orderDatetId = criteriaBuilder.desc(root.get("createdDate.id"));

        return criteriaQuery.orderBy(ordercommentId, orderArticleid, orderProfileId, orderDatetId);
    }

    public CriteriaQuery<CommentEntity> AscOrder(Root<CommentEntity> root) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<CommentEntity> criteriaQuery = criteriaBuilder.createQuery(CommentEntity.class);

        Order ordercommentId = criteriaBuilder.asc(root.get("id"));
        Order orderArticleid = criteriaBuilder.asc(root.get("articleId"));
        Order orderProfileId = criteriaBuilder.asc(root.get("profileId"));
        Order orderDatetId = criteriaBuilder.asc(root.get("createdDate"));
        return criteriaQuery.orderBy(ordercommentId, orderArticleid, orderProfileId, orderDatetId);
    }

    public Predicate fixed(Root<CommentEntity> root, CommentFilterDTO dto) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        Predicate contentPred = criteriaBuilder.equal(root.get("content"), dto.getContent());
        Predicate comentIdPredicate = criteriaBuilder.equal(root.get("id"), dto.getCommentId());
        Predicate profilePred = criteriaBuilder.equal(root.get("profileId"), dto.getProfileId());
        Predicate articlePred = criteriaBuilder.equal(root.get("articleId"), dto.getArticleId());
        Predicate datePredicate = criteriaBuilder.between(root.get("createdDate"), dto.getFromDate(), dto.getToDate());

        return criteriaBuilder.and(contentPred, comentIdPredicate,
                profilePred, articlePred, datePredicate);

    }
}

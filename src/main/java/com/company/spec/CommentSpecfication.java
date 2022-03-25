package com.company.spec;

import com.company.entity.ArticleEntity;
import com.company.entity.CommentEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class CommentSpecfication {
    public static Specification<CommentEntity> isNotNull(String field) {
        return ((root, query, criteriaBuilder) -> {
            return criteriaBuilder.isNotNull(root.get(field));
        });
    }
    public static Specification<CommentEntity> id(String field, Integer id) {
        return ((root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get(field),id);
        });
    }
    public static Specification<CommentEntity> date(LocalDate date1, LocalDate date2) {
        return ((root, query, criteriaBuilder) -> {
            return criteriaBuilder.between(root.get("createdDate"), date1,date2);
        });
    }
    public static Specification<CommentEntity> equal(String field, String value) {
        return ((root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get(field),value);
        });
    }
}

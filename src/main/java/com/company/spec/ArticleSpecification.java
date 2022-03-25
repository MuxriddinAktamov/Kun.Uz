package com.company.spec;

import com.company.entity.ArticleEntity;
import com.company.enums.ArticleStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class ArticleSpecification {

    public static Specification<ArticleEntity> status(ArticleStatus status) {
        return ((root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("status"), status);
        });
    }

    public static Specification<ArticleEntity> title(String title) {
        return ((root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("title"), title);
        });
    }

    public static Specification<ArticleEntity> equal(String field, Integer id) {
        return ((root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get(field), id);
        });
    }
    public static Specification<ArticleEntity> date( LocalDate date1,LocalDate date2) {
        return ((root, query, criteriaBuilder) -> {
            return criteriaBuilder.between(root.get("created_date"), date1,date2);
        });
    }

}

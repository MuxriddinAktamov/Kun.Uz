package com.company.spec;

import com.company.entity.ArticleEntity;
import com.company.entity.ProfileEntity;
import com.company.enums.ProfileRole;
import com.company.enums.ProfileStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class ProfileSpecification {

    public static Specification<ProfileEntity> status(ProfileStatus status){
        return ((root, query, criteriaBuilder) -> {
           return criteriaBuilder.equal(root.get("status"),status);
        });
    }
    public static Specification<ProfileEntity> id(Integer id){
        return ((root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("id"),id);
        });
    }
    public static Specification<ProfileEntity> equal(String name,String value) {
        return ((root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get(name), value);
        });
    }
    public static Specification<ProfileEntity> role(ProfileRole role){
        return ((root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("role"),role);
        });
    }
    public static Specification<ProfileEntity> date(LocalDate date1, LocalDate date2) {
        return ((root, query, criteriaBuilder) -> {
            return criteriaBuilder.between(root.get("created_date"), date1,date2);
        });
    }
}

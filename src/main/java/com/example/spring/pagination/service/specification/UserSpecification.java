package com.example.spring.pagination.service.specification;

import com.example.spring.pagination.dao.entity.UserEntity;
import com.example.spring.pagination.model.criteria.UserCriteria;
import com.example.spring.pagination.util.PredicateUtil;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import static com.example.spring.pagination.util.PredicateUtil.applyLikePattern;

@AllArgsConstructor
public class UserSpecification implements Specification<UserEntity> {
    private UserCriteria userCriteria;

    @Override
    public Predicate toPredicate(Root<UserEntity> root,
                                 CriteriaQuery<?> query,
                                 CriteriaBuilder criteriaBuilder) {
        var predicates = PredicateUtil.builder()
                .addNullSafety(userCriteria.getBirthPlace(),
                        birthPlace -> criteriaBuilder.like(root.get(UserEntity.Fields.birthPlace), applyLikePattern(birthPlace)))
                .addNullSafety(userCriteria.getAgeFrom(),
                        ageFrom -> criteriaBuilder.greaterThanOrEqualTo(root.get(UserEntity.Fields.age), ageFrom))
                .addNullSafety(userCriteria.getAgeTo(),
                        ageTo -> criteriaBuilder.lessThanOrEqualTo(root.get(UserEntity.Fields.age), ageTo))
                .build();

        return criteriaBuilder.and(predicates);
    }
}

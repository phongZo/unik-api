package com.lv.api.storage.criteria;

import com.lv.api.storage.model.Rank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RankCriteria {
    private Long id;
    private String name;
    private Long target;

    public Specification<Rank> getSpecification() {
        return (root, criteriaQuery, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(getId() != null) {
                predicates.add(cb.equal(root.get("id"), getId()));
            }

            if(!StringUtils.isEmpty(getName())) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + getName().toLowerCase() + "%"));
            }

            if(getTarget() != null) {
                predicates.add(cb.equal(root.get("target"), getTarget()));
            }

            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    public Specification<Rank> getSpecificationAutoComplete() {
        return (root, criteriaQuery, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(!StringUtils.isEmpty(getName())) {
                predicates.add(cb.like(cb.lower(root.get("name")), getName().toLowerCase() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }
}

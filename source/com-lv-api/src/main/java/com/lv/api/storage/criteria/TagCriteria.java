package com.lv.api.storage.criteria;

import com.lv.api.storage.model.Tag;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TagCriteria {
    private Long id;
    private String tag;

    public Specification<Tag> getSpecification() {
        return (root, criteriaQuery, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (getId() != null) {
                predicates.add(cb.equal(root.get("id"), getId()));
            }

            if (!StringUtils.isEmpty(getTag())) {
                predicates.add(cb.like(cb.lower(root.get("tag")), "%" + getTag().toLowerCase() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    public Specification<Tag> getSpecificationAutoComplete() {
        return (root, criteriaQuery, cb) -> cb.like(cb.lower(root.get("tag")), getTag().toLowerCase() + "%");
    }
}

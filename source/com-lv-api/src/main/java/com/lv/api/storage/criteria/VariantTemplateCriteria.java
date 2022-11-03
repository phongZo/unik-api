package com.lv.api.storage.criteria;

import com.lv.api.storage.model.VariantTemplate;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class VariantTemplateCriteria {
    private Long id;
    private String name;

    public Specification<VariantTemplate> getSpecification() {
        return (root, criteriaQuery, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (getId() != null) {
                predicates.add(cb.equal(root.get("id"), getId()));
            }

            if (getName() != null) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + getName().toLowerCase() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }
}

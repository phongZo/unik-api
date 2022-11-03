package com.lv.api.storage.criteria;

import com.lv.api.storage.model.Variant;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class VariantCriteria {
    private Long id;
    private String name;
    private Double price;

    public Specification<Variant> getSpecification() {
        return (root, criteriaQuery, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (getId() != null) {
                predicates.add(cb.equal(root.get("id"), getId()));
            }

            if (getName() != null) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + getName().toLowerCase() + "%"));
            }

            if (getPrice() != null) {
                predicates.add(cb.equal(root.get("price"), getPrice()));
            }

            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }
}

package com.lv.api.storage.criteria;

import com.lv.api.storage.model.Location;
import com.lv.api.validation.LocationKind;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class LocationCriteria {
    private Long id;
    private String name;
    private Long parentId;
    @LocationKind
    private Integer kind;

    public Specification<Location> getSpecification() {
        return (root, criteriaQuery, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (getId() != null) {
                predicates.add(cb.equal(root.get("id"), getId()));
            }
            if (getName() != null) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + getName().toLowerCase() + "%"));
            }
            if (getParentId() != null) {
                predicates.add(cb.equal(root.get("parent"), getParentId()));
            }
            if (getKind() != null) {
                predicates.add(cb.equal(root.get("kind"), getKind()));
            }
            if (predicates.isEmpty()) {
                predicates.add(cb.isNull(root.get("parent")));
            }
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    public Specification<Location> getSpecificationAutoComplete() {
        return (root, criteriaQuery, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (getName() != null) {
                predicates.add(cb.like(cb.lower(root.get("name")), getName().toLowerCase() + "%"));
            }
            if (getParentId() != null) {
                predicates.add(cb.equal(root.get("parent"), getParentId()));
            } else {
                predicates.add(cb.isNull(root.get("parent")));
            }
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }
}

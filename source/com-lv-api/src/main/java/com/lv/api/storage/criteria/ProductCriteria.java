package com.lv.api.storage.criteria;

import com.lv.api.storage.model.Product;
import com.lv.api.storage.model.ProductConfig;
import com.lv.api.validation.ProductKind;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class ProductCriteria {
    private Long id;
    private Long categoryId;
    private List<String> tags;
    private String description;
    private String name;
    private Double fromPrice;
    private Double toPrice;
    private Boolean isSoldOut;
    private Long parentProduct;
    @ProductKind
    private Integer kind;
    private List<String> variantNames;

    public Specification<Product> getSpecification() {
        return (root, criteriaQuery, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (getId() != null) {
                predicates.add(cb.equal(root.get("id"), getId()));
            }

            if (getCategoryId() != null) {
                predicates.add(cb.equal(root.get("categoryId"), getCategoryId()));
            }

            if (getTags() != null) {
                this.tags = getTags().stream().map(tag -> tag.toLowerCase().trim()).collect(Collectors.toList());
                List<Predicate> predicatesOr = new ArrayList<>();
                for (String tag : tags) {
                    predicatesOr.add(cb.like(cb.lower(root.get("tags")), "%" + tag + "%"));
                }
                predicates.add(cb.or(predicatesOr.toArray(new Predicate[]{})));
            }

            if (getDescription() != null) {
                predicates.add(cb.like(cb.lower(root.get("description")), "%" + getDescription().toLowerCase() + "%"));
            }

            if (getName() != null) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + getName().toLowerCase() + ""));
            }

            if (getFromPrice() != null) {
                predicates.add(cb.ge(root.get("price"), getFromPrice()));
            }

            if (getToPrice() != null) {
                predicates.add(cb.le(root.get("price"), getToPrice()));
            }

            if (getIsSoldOut() != null) {
                predicates.add(cb.equal(root.get("isSoldOut"), getIsSoldOut()));
            }

            if (getParentProduct() != null) {
                predicates.add(cb.equal(root.get("parentProduct"), getParentProduct()));
            } else {
                predicates.add(cb.isNull(root.get("parentProduct")));
            }

            if (getKind() != null) {
                predicates.add(cb.equal(root.get("kind"), getKind()));
            }

            if (getVariantNames() != null) {
                Join<Product, ProductConfig> productConfigJoin = root.join("productConfigs", JoinType.INNER);
                this.variantNames = getVariantNames().stream().map(v -> v.toLowerCase().trim()).collect(Collectors.toList());
                predicates.add(cb.lower(productConfigJoin.join("variants").get("name")).in(getVariantNames()));
            }
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }
}

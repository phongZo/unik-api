package com.unik.api.storage.criteria;

import com.unik.api.storage.model.Customer;
import com.unik.api.storage.model.Product;
import com.unik.api.storage.model.ProductCategory;
import com.unik.api.storage.model.ProductConfig;
import com.unik.api.validation.ProductKind;
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
    private Boolean isSaleOff;
    private List<String> tags;
    private String description;
    private String name;
    private Double fromPrice;
    private Double toPrice;
    private Boolean isSoldOut;
    private Long parentProduct;
    private Long customerId;
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
                Join<Product, ProductCategory> productCategoryJoin = root.join("category", JoinType.INNER);
                predicates.add(cb.equal(productCategoryJoin.get("id"), getCategoryId()));
            }
            if(getCustomerId() != null){
                Join<Product, Customer> productCustomerJoin = root.join("customersLiked", JoinType.INNER);
                predicates.add(cb.equal(productCustomerJoin.get("id"), getCustomerId()));
                criteriaQuery.distinct(true);
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

            if (getIsSaleOff() != null){
                criteriaQuery.orderBy(cb.desc(root.get("soldAmount")));
                predicates.add(cb.equal(root.get("isSaleOff"), getIsSaleOff()));
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

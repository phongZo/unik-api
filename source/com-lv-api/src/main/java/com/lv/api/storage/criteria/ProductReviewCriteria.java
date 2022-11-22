package com.lv.api.storage.criteria;

import com.lv.api.storage.model.ProductReview;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
@Data
public class ProductReviewCriteria {
    private Integer star;
    public Specification<ProductReview> getSpecification() {
        return new Specification<ProductReview>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<ProductReview> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if(getStar() != null) {
                    predicates.add(cb.equal(root.get("star"), getStar()));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}

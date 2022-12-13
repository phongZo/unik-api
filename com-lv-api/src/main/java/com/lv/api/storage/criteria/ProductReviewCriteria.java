package com.lv.api.storage.criteria;

import com.lv.api.storage.model.Customer;
import com.lv.api.storage.model.CustomerPromotion;
import com.lv.api.storage.model.Product;
import com.lv.api.storage.model.ProductReview;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
@Data
public class ProductReviewCriteria {
    private Integer star;
    private Long productId;
    public Specification<ProductReview> getSpecification() {
        return new Specification<ProductReview>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<ProductReview> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if(getProductId() != null){
                    Join<ProductReview, Product> joinReview = root.join("product", JoinType.INNER);
                    predicates.add(cb.equal(joinReview.get("id"), getProductId()));
                }
                if(getStar() != null) {
                    predicates.add(cb.equal(root.get("star"), getStar()));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}

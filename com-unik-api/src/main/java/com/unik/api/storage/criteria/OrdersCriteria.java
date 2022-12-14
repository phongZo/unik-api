package com.unik.api.storage.criteria;

import com.unik.api.storage.model.*;
import com.unik.api.storage.model.Customer;
import com.unik.api.storage.model.Orders;
import com.unik.api.storage.model.Store;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class OrdersCriteria {
    private Long id;
    private Long storeId;
    private Long customerId;
    private Integer state;
    private Integer notState;
    private String code;
    private Integer paymentMethod;
    private Integer status;
    private Date from;
    private Date to;

    public Specification<Orders> getSpecification() {
        return new Specification<Orders>() {
            private static final long seriaunikersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Orders> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if(getId() != null){
                    predicates.add(cb.equal(root.get("id"), getId()));
                }
                if (getPaymentMethod() != null){
                    predicates.add(cb.equal(root.get("paymentMethod"),getPaymentMethod()));
                }
                if (getState() != null){
                    predicates.add(cb.equal(root.get("state"),getState()));
                }
                if (getNotState() != null){
                    predicates.add(cb.notEqual(root.get("state"),getNotState()));
                }
                if(getStatus() != null) {
                    predicates.add(cb.equal(root.get("status"), getStatus()));
                }
                if(getCustomerId() != null) {
                    Join<Customer, Orders> joinCustomer = root.join("customer", JoinType.INNER);
                    predicates.add(cb.equal(joinCustomer.get("id"), getCustomerId()));
                }
                if(getStoreId() != null) {
                    Join<Store, Orders> joinStore = root.join("store", JoinType.INNER);
                    predicates.add(cb.equal(joinStore.get("id"), getStoreId()));
                }
                if(getCode() != null) {
                    predicates.add(cb.like(cb.lower(root.get("code")), "%" + getCode().toLowerCase() + "%"));
                }
                if(getFrom() != null){
                    predicates.add(cb.greaterThanOrEqualTo(root.get("createdDate"), getFrom()));
                }
                if(getTo() != null){
                    predicates.add(cb.lessThanOrEqualTo(root.get("createdDate"), getTo()));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}

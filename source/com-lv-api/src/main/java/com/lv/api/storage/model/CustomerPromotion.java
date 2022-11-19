package com.lv.api.storage.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = TablePrefix.PREFIX_TABLE + "customer_promotion")
public class CustomerPromotion extends Auditable<String>{
    @Id
    private Long id;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "promotion_id", referencedColumnName = "id")
    @MapsId
    private Promotion promotion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private Date expireDate;
    private Boolean isInUse = false;
}

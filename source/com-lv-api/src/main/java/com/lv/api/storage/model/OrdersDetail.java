package com.lv.api.storage.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = TablePrefix.PREFIX_TABLE+"order_details")
public class OrdersDetail{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Orders orders;

    private Boolean isReviewed = false;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    private Double price;
    private Integer amount;
    private String note;
}

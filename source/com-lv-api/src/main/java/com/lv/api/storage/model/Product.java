package com.lv.api.storage.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = TablePrefix.PREFIX_TABLE + "product")
public class Product extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private ProductCategory category;

    @Column(name = "tag")
    private String tags;

    @Column(name = "description")
    private String description;

    @Column(name = "avg_star")
    private Integer avgStar;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private Double price;

    private Double salePrice;
    private Boolean isSaleOff = false;

    @Column(name = "image")
    private String image;

    @Column(name = "is_sold_out")
    private Boolean isSoldOut;

    @Column(name = "kind")
    private Integer kind;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_id")
    private Product parentProduct;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = ProductConfig.class)
    @JoinColumn(name = "product_id")
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    List<ProductConfig> productConfigs = new ArrayList<>();
}

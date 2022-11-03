package com.lv.api.storage.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = TablePrefix.PREFIX_TABLE + "variant_template")
public class VariantTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "variantTemplate", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<VariantConfig> variantConfigs = new ArrayList<>();
}

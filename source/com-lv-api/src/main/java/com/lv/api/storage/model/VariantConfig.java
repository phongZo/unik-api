package com.lv.api.storage.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = TablePrefix.PREFIX_TABLE + "variant_config")
public class VariantConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "choice_kind")
    private Integer choiceKind;

    @Column(name = "is_required")
    private Boolean isRequired;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "variant_template_id")
    private VariantTemplate variantTemplate;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = TablePrefix.PREFIX_TABLE + "variant_template_variant_join",
            joinColumns = @JoinColumn(name = "variant_config_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "variant_id", referencedColumnName = "id")
    )
    private List<Variant> variants = new ArrayList<>();
}

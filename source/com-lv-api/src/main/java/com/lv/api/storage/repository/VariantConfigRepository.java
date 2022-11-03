package com.lv.api.storage.repository;

import com.lv.api.storage.model.VariantConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VariantConfigRepository extends JpaRepository<VariantConfig, Long> {
    List<VariantConfig> getVariantConfigsByVariantTemplateId(Long variantTemplateId);
}

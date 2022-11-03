package com.lv.api.storage.repository;

import com.lv.api.storage.model.VariantTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface VariantTemplateRepository extends JpaRepository<VariantTemplate, Long>, JpaSpecificationExecutor<VariantTemplate> {
}

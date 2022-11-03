package com.lv.api.storage.repository;

import com.lv.api.storage.model.ProductConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductConfigRepository extends JpaRepository<ProductConfig, Long>, JpaSpecificationExecutor<ProductConfig> {
}

package com.lv.api.storage.repository;

import com.lv.api.storage.model.OrdersDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface OrdersDetailRepository extends JpaRepository<OrdersDetail, Long>, JpaSpecificationExecutor<OrdersDetail> {
    List<OrdersDetail> findAllById(Long id);
}

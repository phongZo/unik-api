package com.lv.api.storage.repository;

import com.lv.api.storage.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long>, JpaSpecificationExecutor<Location> {
    Optional<Location> findLocationById(Long id);
    List<Location> findLocationByKind(Integer kind);
    List<Location> findLocationByParentId(Long parentId);
}

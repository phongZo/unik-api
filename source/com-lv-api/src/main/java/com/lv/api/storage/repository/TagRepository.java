package com.lv.api.storage.repository;

import com.lv.api.storage.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long>, JpaSpecificationExecutor<Tag> {
    Long countByTag(String tag);

    Long countByTagAndIdNot(String tag, Long id);
}

package com.prgrms.team03linkbookbe.tag.repository;

import com.prgrms.team03linkbookbe.tag.entity.Tag;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByName(String name);
}

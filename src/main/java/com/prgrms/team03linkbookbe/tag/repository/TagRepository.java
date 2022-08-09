package com.prgrms.team03linkbookbe.tag.repository;

import com.prgrms.team03linkbookbe.tag.entity.Tag;
import com.prgrms.team03linkbookbe.tag.entity.TagCategory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByName(TagCategory name);
}

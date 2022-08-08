package com.prgrms.team03linkbookbe.rootTag.repository;

import com.prgrms.team03linkbookbe.rootTag.entity.RootTag;
import com.prgrms.team03linkbookbe.rootTag.entity.RootTagCategory;
import java.util.Optional;
import javax.persistence.OptimisticLockException;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RootTagRepository extends JpaRepository<RootTag, Long> {

    Optional<RootTag> findByName(RootTagCategory name);

}

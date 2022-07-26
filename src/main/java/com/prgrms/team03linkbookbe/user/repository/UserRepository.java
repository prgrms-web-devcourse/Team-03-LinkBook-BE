package com.prgrms.team03linkbookbe.user.repository;

import com.prgrms.team03linkbookbe.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}

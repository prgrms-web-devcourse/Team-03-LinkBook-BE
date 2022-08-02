package com.prgrms.team03linkbookbe.user.repository;

import com.prgrms.team03linkbookbe.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Boolean existsByEmail (String email);

    @Query("SELECT u FROM User u JOIN FETCH u.interests WHERE u.email = :email")
    Optional<User> findByEmailFetchJoinInterests(@Param("email") String email);
}

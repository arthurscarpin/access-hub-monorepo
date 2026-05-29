package com.arthurscarpin.acs.infrastructure.persistence.repository.table;

import com.arthurscarpin.acs.infrastructure.persistence.entity.table.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    boolean existsByEmail(String email);

    @Query("SELECT u FROM UserEntity u LEFT JOIN FETCH u.scopes WHERE u.email = :email")
    Optional<UserEntity> findByEmailWithScopes(@Param("email") String email);

    @EntityGraph(attributePaths = "scopes")
    Page<UserEntity> findAll(Pageable pageable);
}

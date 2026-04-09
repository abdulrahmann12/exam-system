package com.exam.exam_system.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.exam.exam_system.entities.Token;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByToken(String token);

    @Query("SELECT t FROM Token t JOIN FETCH t.user WHERE t.token = :token")
    Optional<Token> findByTokenWithUser(@Param("token") String token);

    @Query("select t from Token t where t.user.userId = :userId and t.expired = false and t.revoked = false")
    List<Token> findAllValidTokensByUser(@Param("userId") Long userId);

    
    @Modifying
    @Query("""
        update Token t
        set t.expired = true, t.revoked = true
        where t.user.userId = :userId
          and t.expired = false
          and t.revoked = false
    """)
    void revokeAllRefreshTokensByUser(@Param("userId") Long userId);

}
package com.exam.exam_system.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.exam.exam_system.Entities.Token;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByToken(String token);
    @Query("""
    	    SELECT t FROM Token t WHERE t.user.id = :userId AND t.expired = false AND t.revoked = false
    	""")
    	List<Token> findAllValidTokenByUser(Long userId);
    
    @Query("select t from Token t where t.user.userId = :userId and t.expired = false and t.revoked = false")
    List<Token> findAllValidTokensByUser(@Param("userId") Long userId);

}
package com.iscte_meta_systems.evoting_server.repositories;

import com.iscte_meta_systems.evoting_server.entities.OAuthToken;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OAuthTokenRepository extends JpaRepository<OAuthToken, Long> {

    OAuthToken findOAuthTokenByToken(String token);

    @Modifying
    @Transactional
    @Query("DELETE FROM OAuthToken o WHERE o.token = :token")
    void deleteByToken(@Param("token") String token);
}

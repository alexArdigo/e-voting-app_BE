package com.iscte_meta_systems.evoting_server.repositories;

import com.iscte_meta_systems.evoting_server.entities.OAuthToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OAuthTokenRepository extends JpaRepository<OAuthToken, Long> {
    boolean existsOAuthTokenByToken(String clientToken);
}

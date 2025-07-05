package com.iscte_meta_systems.evoting_server.security;

import com.iscte_meta_systems.evoting_server.entities.OAuthToken;
import com.iscte_meta_systems.evoting_server.entities.Voter;
import com.iscte_meta_systems.evoting_server.entities.VoterHash;
import com.iscte_meta_systems.evoting_server.repositories.OAuthTokenRepository;
import com.iscte_meta_systems.evoting_server.repositories.VoterHashRepository;
import com.iscte_meta_systems.evoting_server.repositories.VoterRepository;
import com.iscte_meta_systems.evoting_server.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class VoterAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private OAuthTokenRepository oAuthTokenRepository;

    @Autowired
    private VoterHashRepository voterHashRepository;
    @Autowired
    private VoterRepository voterRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String id = authentication.getName();
        String token = authentication.getCredentials().toString();

        Voter existingVoter = voterRepository.findVoterById(Long.valueOf(id));

        if (existingVoter == null || existingVoter.getId() == null)
            throw new AuthenticationException("Voter not found") {};

        OAuthToken existingToken = oAuthTokenRepository.findOAuthTokenByToken(token);

        if (existingToken == null || existingToken.getId() == null)
            throw new AuthenticationException("Token not found") {};


        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_VOTER"));

        return new UsernamePasswordAuthenticationToken(id, token, authorities);

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}

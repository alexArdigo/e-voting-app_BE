package com.iscte_meta_systems.evoting_server.security;

import com.iscte_meta_systems.evoting_server.entities.Voter;
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
    private VoterRepository voterRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String nif = authentication.getName();
        String pin = authentication.getCredentials().toString();

        Voter voter = voterRepository.findVoterByNif(Long.valueOf(nif));

        if (voter == null || voter.getId() == null)
            throw new AuthenticationException("Voter not found") {};


        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + voter.getRole()));

        return new UsernamePasswordAuthenticationToken(nif, pin, authorities);

        /*if (user != null) {
            List<GrantedAuthority> roles = new ArrayList<>();
            roles.add(new SimpleGrantedAuthority("ROLE_USER"));
            if(user.getRole() == Role.ADMIN) {
                roles.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            }
            return new UsernamePasswordAuthenticationToken(username, password, roles);
        }

         */
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}

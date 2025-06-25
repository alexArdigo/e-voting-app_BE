package com.iscte_meta_systems.evoting_server.security;

import com.iscte_meta_systems.evoting_server.entities.User;
import com.iscte_meta_systems.evoting_server.model.LoginDTO;
import com.iscte_meta_systems.evoting_server.model.UserRegisterDTO;
import com.iscte_meta_systems.evoting_server.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class UserAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        LoginDTO loginDTO = new LoginDTO(username, password);

        User user = userService.login(loginDTO);

        if (user == null || !user.getIsAuthorized()) {
            throw new BadCredentialsException("User not found or not authorized");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole()));

        return new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), authorities);

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

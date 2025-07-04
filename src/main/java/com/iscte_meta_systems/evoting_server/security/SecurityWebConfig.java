package com.iscte_meta_systems.evoting_server.security;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityWebConfig {

    @Autowired
    UserAuthenticationProvider userAuthenticationProvider;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of(
                "http://localhost:5173",
                "http://localhost:5174"
        ));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors(corsConfigurer -> {
            corsConfigurer.configurationSource(corsConfigurationSource());
        });

        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        httpSecurity.authorizeHttpRequests(auth -> {
            auth.dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll();
            // Put OAuth first - this is important!
            auth.requestMatchers("/oauth/**").permitAll();
            auth.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll(); // Add this for CORS preflight

            auth.requestMatchers(HttpMethod.POST, "/login").permitAll();
            //auth.requestMatchers("/oauth/**").permitAll();
            auth.requestMatchers(HttpMethod.POST, "/registerAdmin", "/registerViewer").permitAll();
            auth.requestMatchers(HttpMethod.GET, "/elections", "/elections/{id}", "/elections/{id}/ballot").permitAll();
            auth.requestMatchers(HttpMethod.GET, "/elections/{id}/candidates", "/candidate/{id}").permitAll();
            auth.requestMatchers(HttpMethod.GET, "/organisations", "/parties", "/uniparties", "/organisations/{id}").permitAll();

            // Admin
            auth.requestMatchers(HttpMethod.POST, "/elections", "/candidates", "/organisations").hasRole("ADMIN");
            auth.requestMatchers(HttpMethod.POST, "/elections/{id}/startElection", "/elections/{id}/endElection").hasRole("ADMIN");
            auth.requestMatchers(HttpMethod.GET, "/pendingAuthorization").hasRole("ADMIN");
            auth.requestMatchers(HttpMethod.GET, "/approveViewer/{viewerId}").hasRole("ADMIN");

            // Authenticated voters
            auth.requestMatchers(HttpMethod.POST, "/elections/{id}/castVote").authenticated();
            auth.requestMatchers(HttpMethod.GET, "/findUserByUsername").authenticated();
            auth.requestMatchers(HttpMethod.GET, "/voters/info").permitAll();
            auth.requestMatchers(HttpMethod.GET, "/voters/has-voted").authenticated();

            auth.requestMatchers("/**").permitAll();

            //auth.requestMatchers("**").denyAll();
        });

        httpSecurity.formLogin(loginConfig -> {
            loginConfig.loginPage("/login");
            loginConfig.loginProcessingUrl("/login");
            loginConfig.successHandler((request, response, authentication) -> {
                response.setStatus(200);
            });
            loginConfig.failureHandler((request, response, authentication) -> {
                response.setStatus(401);
            });
        });

        httpSecurity.logout(
                logout -> {
                    logout.logoutUrl("/logout");
                    logout.deleteCookies("JSESSIONID");
                    logout.logoutSuccessHandler((request, response, authentication) -> {
                        response.setStatus(HttpServletResponse.SC_OK);
                    });
                });

        httpSecurity.authenticationProvider(userAuthenticationProvider);

        return httpSecurity.build();
    }
}

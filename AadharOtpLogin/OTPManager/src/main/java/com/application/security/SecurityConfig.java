package com.application.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {
    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "password";

    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager user = new InMemoryUserDetailsManager();
        user.createUser(User.withUsername(DEFAULT_USERNAME)
                .password(DEFAULT_PASSWORD)
                .roles("ADMIN")
                .build());
        return user;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence charSequence) {
                return charSequence.toString();
            }

            @Override
            public boolean matches(CharSequence charSequence, String s) {
                return s.equals(charSequence.toString());
            }
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests((auth) ->
                        auth
                                .requestMatchers("api/login/generate-otp/**", "api/login/verify-otp", "api/login/unlock-account/**").permitAll()
                                .requestMatchers("/**").hasRole("ADMIN")
                                .anyRequest().denyAll()
                ).csrf(AbstractHttpConfigurer::disable)
                .httpBasic(withDefaults());
        return http.build();
    }
}

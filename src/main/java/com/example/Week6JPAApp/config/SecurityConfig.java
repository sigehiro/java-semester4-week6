package com.example.Week6JPAApp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Security filter chain configuration
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authorize) -> authorize
            .requestMatchers("/restaurant/home").permitAll()
            .requestMatchers("/restaurant/menu/**").hasAnyRole("USER", "ADMIN")
            .requestMatchers("/restaurant/admin/**").hasRole("ADMIN")
            .anyRequest().authenticated()
        )
            // Use default login page for form-based authentication
            // Configure logout behavior
            .formLogin(Customizer.withDefaults())
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/restaurant/home")
            );
        return http.build();
    }

    // In-memory user details configuration
    @Bean
    public UserDetailsService userDetailsService() {
        // Define an admin user with the role ADMIN
        UserDetails admin1 = User.withUsername("owner")
                .password(passwordEncoder().encode("password"))
                .roles("ADMIN")
                .build();

        // Define a normal user with the role USER
        UserDetails user1 = User.withUsername("guest")
                .password(passwordEncoder().encode("password"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(admin1, user1);
    }

    // Ignore security for certain static resources (e.g., CSS files, H2 console)
    @Bean
    public WebSecurityCustomizer ignoreResources() {
        return (webSecurity) -> webSecurity.ignoring().requestMatchers("/css/**", "/h2-console/**");
    }

    // Password encoder bean using BCrypt for secure password hashing
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
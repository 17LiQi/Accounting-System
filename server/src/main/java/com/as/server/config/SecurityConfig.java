package com.as.server.config;

import com.as.server.exception.CustomAccessDeniedHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final CustomAccessDeniedHandler accessDeniedHandler;

    public SecurityConfig(CustomAccessDeniedHandler accessDeniedHandler) {
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthFilter) throws Exception {
        http
                .cors().and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests(auth -> auth
                        .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .antMatchers("/login").permitAll()
                        .antMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        .antMatchers(HttpMethod.GET, "/accounts/**").hasAnyRole("ADMIN", "USER")
                        .antMatchers("/accounts/**").hasRole("ADMIN")

                        .antMatchers(HttpMethod.GET, "/users").hasAnyRole("ADMIN", "USER")       // 允许 /users GET 普通用户访问
                        .antMatchers(HttpMethod.GET, "/users/*").hasAnyRole("ADMIN", "USER")    // 允许 /users/{id} GET 普通用户访问
                        .antMatchers("/users/**").hasRole("ADMIN")                             // 其他 /users 路径需要 ADMIN

                        .antMatchers("/sub-accounts/**").hasAnyRole("ADMIN", "USER")
                        
                        .antMatchers(HttpMethod.GET, "/transaction-types/**").hasAnyRole("ADMIN", "USER")
                        .antMatchers("/transaction-types/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler)
                .and()
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

package com.utp.spring.config;

import com.utp.spring.security.JWTAuthenticationFilter;
import com.utp.spring.security.JWTAuthorizationFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@AllArgsConstructor
public class WebSecuritryConfig {

    private final UserDetailsService userDetailsService;
    private final JWTAuthorizationFilter jwtAuthorizationFilter;



    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authManager) throws Exception {
        JWTAuthenticationFilter jwtAuthenticationFilter = new JWTAuthenticationFilter();
        jwtAuthenticationFilter.setAuthenticationManager(authManager);
        jwtAuthenticationFilter.setFilterProcessesUrl("/login");

        return http
                .cors()
                .and()
                .csrf().disable()
                .authorizeHttpRequests()
	                .requestMatchers("/api/productos/menu/**").permitAll()
	                .requestMatchers("/api/productos/imagen/**").permitAll()
	                .requestMatchers("/api/carrito/crear/**").permitAll()
	                .requestMatchers("/api/usuarios/email/**").permitAll()
	                .requestMatchers("/api/usuarios/registrar").permitAll()
	                .requestMatchers("/api/categorias/lista").permitAll()
	                .requestMatchers("/api/productos/lista").hasAuthority("admin")
	                .requestMatchers("/api/productos/agregar").hasAuthority("admin")
	                .requestMatchers("/api/productos/update/**").hasAuthority("admin")
	                .requestMatchers("/api/productos/delete/**").hasAuthority("admin")
	                .requestMatchers("/api/usuarios/lista").hasAuthority("admin")
	                .requestMatchers("/api/usuarios/privilegio/**").hasAuthority("admin")
	                .requestMatchers("/api/usuarios/cambioprivilegio").hasAuthority("admin")
	                .requestMatchers("/api/usuarios/delete").hasAuthority("admin")
	                .requestMatchers("/api/clientes/lista").hasAuthority("admin")
	                .requestMatchers("/api/empleados/**").hasAuthority("admin")
	                .requestMatchers("/api/proveedores/**").hasAuthority("admin")
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(jwtAuthenticationFilter)
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
    /*@Bean
    UserDetailsService userDetailsService(){
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("admin")
                .password(passwordEncoder().encode("admin"))
                .roles()
                .build());
        return manager;
    }*/

    @Bean
    AuthenticationManager authManager(HttpSecurity http, PasswordEncoder passwordEncoder) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public static void main(String[] args) {
        System.out.println("pass: "+new BCryptPasswordEncoder().encode("1234"));
    }



}

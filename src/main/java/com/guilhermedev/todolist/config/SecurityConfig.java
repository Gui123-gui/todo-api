package com.guilhermedev.todolist.config;

import com.guilhermedev.todolist.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor // gera o construtor para os campos "final" abaixo
public class SecurityConfig {

    // Dependência que sabe buscar o User pelo email (você já criou)
    private final CustomUserDetailsService customUserDetailsService;

    // Algoritmo de hash de senha - usado em dois lugares: ao salvar (AuthService.register)
    // e ao validar login (DaoAuthenticationProvider abaixo)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Conecta CustomUserDetailsService + PasswordEncoder.
    // É essa peça que faltava - sem ela, authenticationManager.authenticate()
    // não sabe como buscar o usuário nem como comparar a senha.
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    // Objeto que o AuthService usa para disparar a autenticação.
    // Por baixo dos panos, ele delega para o authenticationProvider acima.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // Define as regras de acesso da API
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                // Token JWT não usa sessão - cada requisição se autentica sozinha
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll() // login e registro são públicos
                        .anyRequest().authenticated()             // todo o resto exige token válido
                );

        return http.build();
    }
}
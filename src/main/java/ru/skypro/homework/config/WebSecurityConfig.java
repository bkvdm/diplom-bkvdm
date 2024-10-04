package ru.skypro.homework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private static final String[] AUTH_WHITELIST = {
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/webjars/**",
            "/login",
            "/register",
            "/h2-console/**",
            "/swagger-ui/index.html#/",
            "/error",
            "/avatar_user/**",
            "/image_ad/**"
    };

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(Collections.singletonList("*")); // Для теста: разрешение всех источников
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
//            configuration.setAllowedOriginPatterns(Arrays.asList("http://localhost:3000")); // Используем allowedOriginPatterns
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type", "X-Requested-With", "method"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // CORS настройки
                .csrf(csrf -> csrf.disable()) // Отключение CSRF
                .authorizeHttpRequests(authorization ->
                        authorization
                                .requestMatchers(AUTH_WHITELIST).permitAll() // Разрешаем публичные запросы
                                .requestMatchers("/ads/**", "/users/**").authenticated() // Остальные требуют аутентификации
                )
                .httpBasic(withDefaults()) // Используем базовую HTTP-аутентификацию
                .headers(headers -> headers
//                        .addHeaderWriter(new ContentSecurityPolicyHeaderWriter("default-src 'self'; script-src 'self' https://trustedscripts.example.com")) // Временно убран, в целях тестирования
                                .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin) // Защита от Clickjacking
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

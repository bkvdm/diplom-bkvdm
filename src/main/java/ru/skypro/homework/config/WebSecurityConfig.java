package ru.skypro.homework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.http.HttpMethod;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Конфигурация безопасности веб-приложения.
 * <p>
 * Данный класс настраивает параметры безопасности приложения, включая
 * аутентификацию, разрешения для различных URL, а также настройки CORS.
 * </p>
 */
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
            "/image_ad/**",
            "/src/images/**",
            "/images/**"
    };

    /**
     * Настраивает CORS (Cross-Origin Resource Sharing) для веб-приложения.
     * <p>
     * Позволяет запросы из браузера с определенного источника (в данном случае
     * http://localhost:3000) к API приложения, разрешая определенные методы
     * HTTP и заголовки.
     * </p>
     *
     * @return объект WebMvcConfigurer с настроенными CORS.
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:3000")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }

    /**
     * Конфигурирует цепочку фильтров безопасности.
     * <p>
     * Настраивает параметры аутентификации и авторизации для
     * различных URL-адресов, включая разрешенные маршруты для
     * публичного доступа, а также маршруты, требующие аутентификации.
     * </p>
     *
     * @param http объект HttpSecurity для настройки.
     * @return настроенная цепочка фильтров безопасности.
     * @throws Exception если возникает ошибка при конфигурации безопасности.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(AUTH_WHITELIST).permitAll()
                        .requestMatchers("/avatar_user/**").permitAll()
                        .requestMatchers("/ads/**", "/users/**").authenticated()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // Разрешаем все OPTIONS запросы
                        .requestMatchers(AUTH_WHITELIST).permitAll()
                )
                .cors(withDefaults())
                .httpBasic(withDefaults());
        return http.build();
    }

    /**
     * Создает и настраивает объект PasswordEncoder для шифрования паролей.
     * <p>
     * Использует BCrypt для шифрования паролей пользователей.
     * </p>
     *
     * @return экземпляр PasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

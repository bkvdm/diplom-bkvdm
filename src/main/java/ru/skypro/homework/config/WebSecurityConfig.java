package ru.skypro.homework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.ContentSecurityPolicyHeaderWriter;
import ru.skypro.homework.dto.Role;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class WebSecurityConfig {

    private static final String[] AUTH_WHITELIST = {
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs",
            "/webjars/**",
            "/login",
            "/register",
            "/h2-console/**",
            "/swagger-ui/index.html#/",
            "/error"
    };

//    @Bean
//    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
//        UserDetails user =
//                User.builder()
//                        .username("user@gmail.com")
//                        .password("password")
//                        .passwordEncoder(passwordEncoder::encode)
//                        .roles(Role.USER.name())
//                        .build();
//        return new InMemoryUserDetailsManager(user);
//    }



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorization ->
                        authorization
                                .requestMatchers(AUTH_WHITELIST).permitAll()
//                                .requestMatchers("/ads/**", "/users/**").authenticated()
                                .requestMatchers("/ads/**").hasRole("USER") // Доступ для пользователей с ролью USER
                                .requestMatchers("/users/**").hasRole("ADMIN") // Доступ для пользователей с ролью ADMIN
                                .anyRequest().authenticated() // Все остальные применяют аутентификации
                )
                .cors(withDefaults())  // Настройка CORS
                .httpBasic(withDefaults())  // Настройка базовой аутентификации
//                .httpBasic(withDefaults -> withDefaults.disable());  // Отключаем базовую аутентификацию
                .headers(headers -> headers
                        .addHeaderWriter(new ContentSecurityPolicyHeaderWriter("frame-ancestors 'self'"))
                );
        return http.build();
    }

    //    @Bean
    //    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    //        http
    //                .csrf(csrf -> csrf.disable())
    //                .authorizeHttpRequests(authorization ->
    //                        authorization
    //                                .requestMatchers(AUTH_WHITELIST).permitAll()
    //                                .requestMatchers("/ads/**").hasRole("USER") // Доступ для пользователей с ролью USER
    //                                .requestMatchers("/users/**").hasRole("ADMIN") // Доступ для пользователей с ролью ADMIN
    //                                .anyRequest().authenticated() // Все остальные запросы требуют аутентификации
    //                )
    //                .cors(withDefaults())  // Настройка CORS
    //                .httpBasic(withDefaults())  // Настройка базовой аутентификации
    //                .headers(headers -> headers
    //                        .addHeaderWriter(new ContentSecurityPolicyHeaderWriter("frame-ancestors 'self'"))
    //                );
    //        return http.build();
    //    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

//...

//@Configuration
//public class WebSecurityConfig {
//
//    private static final String[] AUTH_WHITELIST = {
//            "/swagger-resources/**",
//            "/swagger-ui.html",
//            "/swagger-ui/**",
//            "/v3/api-docs",
//            "/webjars/**",
//            "/login",
//            "/register",
//            "/h2-console/**",
//            "/swagger-ui/index.html#/",
//            "/error"
//    };
//
//    @Bean
//    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
//        UserDetails user =
//                User.builder()
//                        .username("user@gmail.com")
//                        .password("password")
//                        .passwordEncoder(passwordEncoder::encode)
//                        .roles(Role.USER.name())
//                        .build();
//        return new InMemoryUserDetailsManager(user);
//    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(authorization ->
//                        authorization
//                                .requestMatchers(AUTH_WHITELIST).permitAll()
//                                .requestMatchers("/ads/**", "/users/**").authenticated()
//                )
//                .cors(withDefaults())  // Настройка CORS
////                .httpBasic(withDefaults())  // Настройка базовой аутентификации
//                .httpBasic(withDefaults -> withDefaults.disable());  // Отключаем базовую аутентификацию
////                .headers(headers -> headers
////                        .addHeaderWriter(new ContentSecurityPolicyHeaderWriter("frame-ancestors 'self'"))
////                );
//        return http.build();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//}

//...


//@Configuration
//public class WebSecurityConfig {
//
//    private static final String[] AUTH_WHITELIST = {
//            "/swagger-resources/**",
//            "/swagger-ui.html",
//            "/v3/api-docs",
//            "/webjars/**",
//            "/login",
//            "/register",
////            "/db-console/**",
//            "/h2-console/**",
//            "/error?continue"
//    };
//
//    @Bean
//    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
//        UserDetails user =
//                User.builder()
//                        .username("user@gmail.com")
//                        .password("password")
//                        .passwordEncoder(passwordEncoder::encode)
//                        .roles(Role.USER.name())
//                        .build();
//        return new InMemoryUserDetailsManager(user);
//    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(authorization ->
//                        authorization
//                                .requestMatchers(AUTH_WHITELIST).permitAll()
//                                .requestMatchers("/ads/**", "/users/**").authenticated()
//                )
//                .cors(withDefaults())  // Настройка CORS
//                .httpBasic(withDefaults());  // Настройка базовой аутентификации
//        return http.build();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//}


//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@Configuration
//public class WebSecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//            .authorizeHttpRequests(authorize -> authorize
//                .requestMatchers("/h2-console/**").permitAll()  // Разрешить доступ к H2 Console
//                .anyRequest().authenticated()
//            )
//            .csrf(csrf -> csrf
//                .ignoringRequestMatchers("/h2-console/**")  // Отключить CSRF для H2 Console
//            )
//            .headers(headers -> headers
//                .frameOptions().sameOrigin()  // Разрешить загрузку фреймов с того же источника
//            )
//            .formLogin(withDefaults());
//        return http.build();
//    }
//}

//
//@Configuration
//public class WebSecurityConfig {
//
//    private static final String[] AUTH_WHITELIST = {
//            "/swagger-resources/**",
//            "/swagger-ui.html",
//            "/v3/api-docs",
//            "/webjars/**",
//            "/login",
//            "/register",
//            "/db-console/**"
//    };
//
//    @Bean
//    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
//        UserDetails user =
//                User.builder()
//                        .username("user@gmail.com")
//                        .password("password")
//                        .passwordEncoder(passwordEncoder::encode)
//                        .roles(Role.USER.name())
//                        .build();
//        return new InMemoryUserDetailsManager(user);
//    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable())  // Изменён способ отключения CSRF
//                .authorizeHttpRequests(authorization ->
//                        authorization
//                                .requestMatchers(AUTH_WHITELIST).permitAll()
//                                .requestMatchers("/ads/**", "/users/**").authenticated()
//                )
//                .cors(cors -> cors.configure(http))  // Настройка CORS
//                .httpBasic(withDefaults());  // Настройка базовой аутентификации
//        return http.build();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//}

package ru.skypro.homework.filter;


import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Component
public class BasicAuthCorsFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        httpServletResponse.addHeader("Access-Control-Allow-Credentials", "true");
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}

//@Component
//public class BasicAuthCorsFilter extends OncePerRequestFilter {
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest httpServletRequest,
//                                    HttpServletResponse httpServletResponse,
//                                    FilterChain filterChain)
//            throws ServletException, IOException {
//
//        httpServletResponse.addHeader("Access-Control-Allow-Credentials", "true");
//        httpServletResponse.addHeader("Access-Control-Allow-Origin", "*"); // Замените '*' на разрешенный источник
//        httpServletResponse.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
//        httpServletResponse.addHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
//
//        // Обработка предзапросов
//        if ("OPTIONS".equalsIgnoreCase(httpServletRequest.getMethod())) {
//            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
//            return;
//        }
//
//        filterChain.doFilter(httpServletRequest, httpServletResponse);
//    }
//}


//@Component
//public class BasicAuthCorsFilter extends OncePerRequestFilter {
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest httpServletRequest,
//                                    HttpServletResponse httpServletResponse,
//                                    FilterChain filterChain)
//            throws ServletException, IOException {
//        httpServletResponse.addHeader("Access-Control-Allow-Credentials", "true");
//        filterChain.doFilter(httpServletRequest, httpServletResponse);
//    }
//}

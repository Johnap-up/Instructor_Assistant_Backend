package org.example.config;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.entity.RestBean;
import org.example.entity.vo.response.AuthorizeVO;
import org.example.filter.JwtAuthorizedFilter;
import org.example.utils.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.io.PrintWriter;

@Configuration
public class SecurityConfiguration {
    @Resource
    private JwtUtil jwtUtil;
    @Resource
    JwtAuthorizedFilter jwtAuthorizedFilter;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/api/auth/**").permitAll();
                    auth.requestMatchers(new AntPathRequestMatcher("/static/**")).permitAll();
                    auth.anyRequest().authenticated();
                })
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(conf -> conf
                        .loginProcessingUrl("/api/auth/login")
                        .failureHandler(this::onAuthenticationFailure)
                        .successHandler(this::onAuthenticationSuccess)
                )
                .logout(conf -> conf
                        .logoutUrl("/api/auth/logout")
                        .logoutSuccessHandler(this::onLogoutSuccess)
                )
                .sessionManagement(conf -> conf
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthorizedFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(conf -> conf
                        .authenticationEntryPoint(this::onUnauthorized)
                        .accessDeniedHandler(this::onForbiddenHandle)
                )
                .build();
    }
    public void onForbiddenHandle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        final PrintWriter writer = response.getWriter();
        writer.write(RestBean.forbidden(accessDeniedException.getMessage()).asJsonString());
    }
    public void onUnauthorized(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        final PrintWriter writer = response.getWriter();
        writer.write(RestBean.unauthorized(exception.getMessage()).asJsonString());
    }
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        final PrintWriter writer = response.getWriter();
        writer.write(RestBean.failure(401,"登录失败，账号或密码错误").asJsonString());
    }
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        User user = (User) authentication.getPrincipal();
        String token = jwtUtil.createJwt(user);

        AuthorizeVO authorizeVO = new AuthorizeVO();
        authorizeVO.setExpire(jwtUtil.getExpireTime());
        authorizeVO.setRole("");
        authorizeVO.setToken(token);
        authorizeVO.setUsername(user.getUsername());
        final PrintWriter writer = response.getWriter();

        writer.write(RestBean.success(authorizeVO).asJsonString());
    }
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        final PrintWriter writer = response.getWriter();
        String authorization = request.getHeader("Authorization");
        if (jwtUtil.invalidateJwt(authorization)){
            writer.write(RestBean.success("退出成功").asJsonString());
        }else {
            writer.write(RestBean.failure(400,"退出失败").asJsonString());
        }
    }
}

package org.example.config;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.entity.RestBean;
import org.example.entity.dto.Account;
import org.example.entity.vo.response.AuthorizeVO;
import org.example.filter.JwtAuthorizedFilter;
import org.example.service.AccountService;
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
    @Resource
    AccountService service;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/api/auth/**", "/error").permitAll();     //error是给错误页面放行，如在发送验证码时邮箱输入错误那么就会转发到error上去
                    auth.requestMatchers("/api/user/**", "/api/image/**", "/student/**").permitAll();       //后续需要修改
                    auth.requestMatchers("/image/**").permitAll();                    //图片放行
                    auth.requestMatchers(new AntPathRequestMatcher("/static/**")).permitAll();
                    auth.anyRequest().authenticated();
                })
                .formLogin(conf -> conf
                        .loginProcessingUrl("/api/auth/login")
                        .failureHandler(this::onAuthenticationFailure)
                        .successHandler(this::onAuthenticationSuccess)
                )
                .logout(conf -> conf
                        .logoutUrl("/api/auth/logout")
                        .logoutSuccessHandler(this::onLogoutSuccess)
                )
                .exceptionHandling(conf -> conf
                        .accessDeniedHandler(this::onForbiddenHandle)
                        .authenticationEntryPoint(this::onUnauthorized)
                )
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(conf -> conf
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthorizedFilter, UsernamePasswordAuthenticationFilter.class)
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
        writer.write(RestBean.failure(401,"登录失败，账号或密码或身份错误").asJsonString());
    }
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        User user = (User) authentication.getPrincipal();
        Account account = service.findByUsernameOrEmail(user.getUsername());
        String role = account.getRole();
//        System.out.println(request.getParameterMap().entrySet());
//        System.out.println(request.getParameter("hello"));
//        System.out.println(Arrays.toString(request.getParameterValues("remember")));
        String token = jwtUtil.createJwt(user, account.getId(), account.getUsername(), role);
        AuthorizeVO authorizeVO = account.asViewObject(AuthorizeVO.class, authorizeVO1 -> {
            authorizeVO1.setExpire(jwtUtil.getExpireTime());
            authorizeVO1.setToken(token);
        });
        final PrintWriter writer = response.getWriter();

        writer.write(RestBean.success(authorizeVO).asJsonString());
    }
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        final PrintWriter writer = response.getWriter();
        String authorization = request.getHeader("Authorization");
        System.out.println("SecurityConfiguration: " + authorization);
        if (jwtUtil.invalidateJwt(authorization)){
            writer.write(RestBean.success("退出成功").asJsonString());
        }else {
            writer.write(RestBean.failure(400,"退出失败").asJsonString());
        }
    }
}

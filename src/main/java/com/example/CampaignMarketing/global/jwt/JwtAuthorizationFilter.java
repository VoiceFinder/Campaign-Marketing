package com.example.CampaignMarketing.global.jwt;

import com.example.CampaignMarketing.domain.user.entity.Role;
import com.example.CampaignMarketing.global.exception.ErrorCode;
import com.example.CampaignMarketing.global.redis.RedisUtil;
import com.example.CampaignMarketing.global.response.ApiResponse;
import com.example.CampaignMarketing.global.security.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Slf4j(topic = "JWT 검증 및 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final ObjectMapper mapper = new ObjectMapper();


    public JwtAuthorizationFilter(JwtUtil jwtUtil, RedisUtil redisUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.redisUtil = redisUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtUtil.resolveToken(req);
        try {
            if (accessToken != null && jwtUtil.validateToken(accessToken)) {
                // Access Token이 유효한 경우, 사용자 인증 정보 설정
                String email = jwtUtil.getUserInfoFromToken(accessToken).getSubject();
                setAuthentication(email);
            }
        } catch (ExpiredJwtException expiredEx) {
            // Access Token이 만료된 경우
            handleExpiredToken(res, expiredEx);
            return;
        }

        filterChain.doFilter(req, res);
    }

    private void handleExpiredToken(HttpServletResponse res, ExpiredJwtException expiredEx) throws IOException {
        String email = expiredEx.getClaims().getSubject();
        String storedRefreshToken = redisUtil.getRefreshToken(email);
        if (storedRefreshToken == null || !jwtUtil.validateToken(storedRefreshToken)) {
            sendErrorResponse(res, ErrorCode.EXPIRED_JWT_TOKEN, "다시 로그인 해주세요.");
            return;
        }
        Long id = Long.parseLong(expiredEx.getClaims().get("identify").toString());
        Role role = Role.valueOf((String) expiredEx.getClaims().get("auth"));
        String newAccessToken = jwtUtil.createAccessToken(id, email, role);
        jwtUtil.addJwtToCookie(newAccessToken, res);
        setAuthentication(email);
    }

    private void sendErrorResponse(HttpServletResponse res, ErrorCode errorCode, String message) throws IOException {
        ApiResponse apiResponse = ApiResponse.of(errorCode.getCode(), errorCode.getMessage(), message);
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        res.getWriter().write(new ObjectMapper().writeValueAsString(apiResponse));
    }

    // 인증 처리
    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

}
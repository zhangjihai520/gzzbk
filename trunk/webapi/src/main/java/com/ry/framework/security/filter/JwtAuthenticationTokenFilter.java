package com.ry.framework.security.filter;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.ry.common.utils.SecurityUtils;
import com.ry.common.utils.StringUtils;
import com.ry.framework.security.LoginUser;
import com.ry.framework.security.service.TokenService;

/**
 * token过滤器 验证token有效性
 *
 * @author
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Autowired
    private TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String path = request.getRequestURI().toLowerCase();

        if (path.contains("/logout") || path.contains("/student/get") || path.contains("/busscommon/") || path.contains("/login")|| path.contains("/rtmp/")  || path.contains("/register") || path.contains("/druid") || path.contains("/captchaimage")) {
            chain.doFilter(request, response);
            return;
        }
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (StringUtils.isNotNull(loginUser)) {
            if (StringUtils.isNull(SecurityUtils.getAuthentication())) {
                tokenService.verifyToken(loginUser);
                if (loginUser.getUserType() == 0) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        } else {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            ObjectMapper mapper = new ObjectMapper();
            response.getWriter().write(mapper.writeValueAsString("身份信息失效，请重新登录！"));
            response.getWriter().close();
            return;
        }
        chain.doFilter(request, response);
    }

    private List<String> getWhiteList() {
        List<String> list = new LinkedList<>();
        return list;
    }
}

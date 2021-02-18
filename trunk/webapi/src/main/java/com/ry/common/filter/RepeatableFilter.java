package com.ry.common.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ry.framework.security.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import com.ry.common.utils.StringUtils;
import org.springframework.stereotype.Component;

/**
 * Repeatable 过滤器
 *
 * @author
 */
@WebFilter(filterName = "requestBaseFilter", urlPatterns = "/*")
@Component
@Slf4j
public class RepeatableFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Autowired
    private TokenService tokenService;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;
        ServletRequest requestWrapper = null;
       /** String path = ((HttpServletRequest) request).getRequestURI().toLowerCase();
        if (path.contains("/login") || path.contains("/register") || path.contains("/getdepartmentlist") || path.contains("/getpolicelist") || path.contains("/getdictionarydata") || path.contains("/sendmessage")|| path.contains("/captchaimage")) {
            chain.doFilter(request, response);
            return;
        }
        var loginUser = tokenService.getLoginUser(request);
        if (StringUtils.isNotNull(loginUser)) {
            tokenService.verifyToken(loginUser);
        } else {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            ObjectMapper mapper = new ObjectMapper();
            response.getWriter().write(mapper.writeValueAsString("Invalid token"));
            response.getWriter().close();
            return;
        }**/
        if (request instanceof HttpServletRequest && StringUtils.equalsAnyIgnoreCase(request.getContentType(),
                MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE)) {
            requestWrapper = new RepeatedlyRequestWrapper((HttpServletRequest) request, response);
        }
        if (null == requestWrapper) {
            chain.doFilter(request, response);
        } else {
            chain.doFilter(requestWrapper, response);
        }

    }

    @Override
    public void destroy() {

    }
}

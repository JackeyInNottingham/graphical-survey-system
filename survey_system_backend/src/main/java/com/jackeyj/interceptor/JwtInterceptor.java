package com.jackeyj.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jackeyj.common.Result;
import com.jackeyj.utils.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * this filter is used to filtrate
 * @author jiyaofei
 */
public class JwtInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String token = request.getHeader("token");

        try {
            JwtUtil.verify(token);
            return true;
        } catch (Exception e) {
            Result result = Result.fail(e.getMessage(),
                    HttpStatus.UNAUTHORIZED.value());
            String json = new ObjectMapper().writeValueAsString(result);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().println(json);
            return false;
        }
    }
}

package com.jackeyj.aspect;

/**
 * the aspect of authentication
 *
 * @author jiyaofei
 */

import com.jackeyj.common.Result;
import com.jackeyj.utils.JwtUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class AuthTokenAspect {

    @Pointcut(value = "@annotation(com.jackeyj.annotation.RequireAdmin)")
    public void checkAdminPermission() {
    }

    @Around("checkAdminPermission()")
    public Object authenticateRole(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("token");
        if (token == null || "".equals(token)){
            return Result.fail(HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                    HttpStatus.UNAUTHORIZED.value());
        }

        String role = null;
        try {
            role = JwtUtil.getClaim(token, "role").asString();
        } catch (Exception e) {
            return Result.fail(e.getMessage(),
                    HttpStatus.UNAUTHORIZED.value());
        }

        if (!"admin".equals(role)) {
            return Result.fail(HttpStatus.FORBIDDEN.getReasonPhrase(),
                    HttpStatus.FORBIDDEN.value());
        }

        return joinPoint.proceed();
    }

}

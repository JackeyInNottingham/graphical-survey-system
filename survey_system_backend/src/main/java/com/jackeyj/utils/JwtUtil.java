package com.jackeyj.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.jackeyj.entity.vo.UserInfoVo;

import java.util.Date;
import java.util.Map;

/**
 * jwt utility
 *
 * @author jiyaofei
 */
public class JwtUtil {

    private static final String SECRET_KEY = "SIMPLE_KEY";

    /**
     * expire time
     * unit: second
     */
    private static Long EXPIRY_TIME = 3600L;

    public static String createJwt(UserInfoVo userInfo) {
        JWTCreator.Builder builder = JWT.create();

        String token = builder
                .withClaim("username", userInfo.getUsername())
                .withClaim("role", userInfo.getRole())
                .withClaim("userId", userInfo.getId())
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRY_TIME * 1000))
                .sign(Algorithm.HMAC256(SECRET_KEY));

        return token;
    }

    public static DecodedJWT verify(String token) throws RuntimeException{
        return JWT.require(Algorithm.HMAC256(SECRET_KEY)).build().verify(token);
    }

    public static Claim getClaim(String token, String claim){
        DecodedJWT jwt = verify(token);
        Map<String, Claim> claims = jwt.getClaims();
        Claim res = claims.get(claim);
        return res;
    }

}

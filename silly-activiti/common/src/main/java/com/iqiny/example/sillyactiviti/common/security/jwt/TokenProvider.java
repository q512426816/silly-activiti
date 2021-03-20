/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-parent 1.0.3-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.example.sillyactiviti.common.security.jwt;


import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Token 提供类
 */
@Component
public class TokenProvider {

    private final JwtProperties properties;
    private JwtParser jwtParser;

    public TokenProvider(JwtProperties properties) {
        this.properties = properties;
        this.jwtParser = Jwts.parser().setSigningKey(properties.getSecret());
    }

    public String tokenName() {
        return properties.getTokenName();
    }


    public String createToken(String subject, Date issueDate) {
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(issueDate)
                .setExpiration(new Date(issueDate.getTime() + properties.getExpirationTime()))
                .signWith(SignatureAlgorithm.HS512, properties.getSecret())
                .compact();
    }

    public JwtInfo parseToken(String token) {
        try {
            Claims claims = jwtParser.parseClaimsJws(token).getBody();
            if (claims != null) {
                JwtInfo ji = new JwtInfo();
                ji.setSubject(claims.getSubject());
                ji.setIssueDate(claims.getIssuedAt().getTime());
                return ji;
            }
        } catch (ExpiredJwtException e) {
            
        }

        return null;
    }

    public String getToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(properties.getHeader());
        if (StringUtils.isEmpty(bearerToken)) {
            final Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals(properties.getCookieName())) {
                        return cookie.getValue();
                    }
                }
            }
        }
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(properties.getTokenStartWith())) {
            return bearerToken.substring(properties.getTokenStartWith().length());
        }
        return null;
    }
}

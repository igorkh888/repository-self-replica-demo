package com.igorkhromov.service;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
public class SecurityService {

    public static final String AUTHORIZATION_HEADER = "Authorization";

    private static final String TOKEN = "token ";

    Optional<String> getAuthToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (null == cookies)
            return Optional.empty();

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(GithubService.GITHUB_AUTH_COOKIE)) {
                return Optional.ofNullable(cookie.getValue());
            }
        }

        return Optional.empty();
    }

    public boolean isAuthorized(HttpServletRequest request) {
        return getAuthToken(request).isPresent();
    }

    public String getRequestToken(String token) {
        return null != token ? TOKEN.concat(token) : TOKEN;
    }
}

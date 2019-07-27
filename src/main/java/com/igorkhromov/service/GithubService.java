package com.igorkhromov.service;

import com.igorkhromov.controller.AuthenticationController;
import com.igorkhromov.form.AuthTokenForm;
import com.igorkhromov.form.RepoForm;
import com.squareup.okhttp.*;
import okio.BufferedSink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import static com.igorkhromov.util.ObjectMapperUtil.*;


@Service
public class GithubService {

    private static final String GITHUB_BASE_URL = "https://github.com";

    private static final String GITHUB_API_BASE_URL = "https://api.github.com";

    private static final String GITHUB_AUTH_URL =
            GITHUB_BASE_URL + "/login/oauth/access_token";

    private static final String GITHUB_USER_REPOS_URL =
            GITHUB_API_BASE_URL +  "/user/repos";

    public static final String GITHUB_AUTH_COOKIE = "github-auth";

    private static final OkHttpClient OK_HTTP_CLIENT = new OkHttpClient();

    private static final String OAUTH_BASE_URL_TEMPLATE = GITHUB_BASE_URL
            + "/login/oauth/authorize?"
            + "client_id=%s"
            + "&scope=public_repo"
            + "&allow_signup=true"
            + "&redirect_uri=%s";

    private static final String GITHUB_CREATE_FORK_URL_TEMPLATE =
            GITHUB_API_BASE_URL + "/repos/:owner/:repo/forks";

    private static final String GITHUB_REPO_URL_TEMPLATE = GITHUB_BASE_URL
            + "/:user/:repo";

    @Value("${server.use.ssl}")
    private boolean sslEnabled;

    @Value("${server.host}")
    private String host;

    @Value("${server.port}")
    private String port;

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.replica.repository.owner}")
    private String replicaRepoOwner;

    @Value("${github.replica.repository.name}")
    private String replicaRepoName;

    private SecurityService securityService;

    @Autowired
    public GithubService(SecurityService securityService) {
        this.securityService = securityService;
    }

    public String getGithubOAuthUrl() {
        return String.format(OAUTH_BASE_URL_TEMPLATE, clientId, getServerBaseUrl());
    }

    private String getServerBaseUrl() {
        return (sslEnabled ? "https" : "http")
                .concat("://")
                .concat(host)
                .concat(":")
                .concat(port)
                .concat(AuthenticationController.GITHUB_OAUTH_REDIRECT_URI);
    }

    public String getGithubRepoUrl() {
        return GITHUB_REPO_URL_TEMPLATE
                .replace(":user", replicaRepoOwner)
                .replace(":repo", replicaRepoName);
    }

    public void authenticateUser(String code, HttpServletResponse response) {
        Optional<String> optAuthToken = getAuthToken(code);
        if(optAuthToken.isPresent()) {
            setAuthenticationCookie(optAuthToken.get(), response);
        } else {
            removeAuthenticationCookie(response);
        }
    }

    private Optional<String> getAuthToken(String code) {
        RequestBody formBody = new FormEncodingBuilder()
                .add("client_id", clientId)
                .add("client_secret", clientSecret)
                .add("code", code)
                .build();

        Request request = new Request.Builder()
                .url(GITHUB_AUTH_URL)
                .addHeader("Accept", "application/json")
                .post(formBody)
                .build();

        try {
            Response response = OK_HTTP_CLIENT.newCall(request).execute();
            if(200 == response.code()) {
                AuthTokenForm authTokenForm = json2Object(
                        response.body().string(),
                        AuthTokenForm.class
                );
                return Optional.ofNullable(authTokenForm.getAccessToken());
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private void setAuthenticationCookie(String authToken,
                                         HttpServletResponse response) {
        Cookie cookie = new Cookie(GITHUB_AUTH_COOKIE, authToken);
        cookie.setMaxAge(60*60*24*365);
        cookie.setSecure(false);
        cookie.setHttpOnly(false);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public void removeAuthenticationCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(GITHUB_AUTH_COOKIE, null);
        cookie.setMaxAge(0);
        cookie.setSecure(false);
        cookie.setHttpOnly(false);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public boolean isRepositoryExists(HttpServletRequest request) {
        Optional<String> optAuthToken = securityService.getAuthToken(request);
        if (optAuthToken.isPresent()) {
            Request httpRequest = new Request.Builder()
                    .url(GITHUB_USER_REPOS_URL)
                    .addHeader(
                            SecurityService.AUTHORIZATION_HEADER,
                            securityService.getRequestToken(optAuthToken.get())
                    )
                    .build();
            try {
                Response response = OK_HTTP_CLIENT.newCall(httpRequest).execute();
                if(200 == response.code()) {
                    RepoForm[] repositories = json2Object(
                            response.body().string(),
                            RepoForm[].class
                    );

                    if(null != repositories) {
                        for(RepoForm repo : repositories) {
                            if(repo.getName().equals(replicaRepoName))
                                return true;
                        }
                    }
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    public boolean forkRepository(HttpServletRequest request) {
        Optional<String> optAuthToken = securityService.getAuthToken(request);
        if (optAuthToken.isPresent()) {
            String requestURL = GITHUB_CREATE_FORK_URL_TEMPLATE
                    .replace(":owner", replicaRepoOwner)
                    .replace(":repo", replicaRepoName);

            Request httpRequest = new Request.Builder()
                    .url(requestURL)
                    .addHeader(
                            SecurityService.AUTHORIZATION_HEADER,
                            securityService.getRequestToken(optAuthToken.get())
                    )
                    .post(new FormEncodingBuilder().build())
                    .build();
            try {
                Response response = OK_HTTP_CLIENT.newCall(httpRequest).execute();
                if(202 == response.code()) {
                    RepoForm repository = json2Object(
                            response.body().string(),
                            RepoForm.class
                    );
                    return null != repository;
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}

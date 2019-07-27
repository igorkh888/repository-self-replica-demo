package com.igorkhromov.controller;

import com.igorkhromov.service.GithubService;
import com.igorkhromov.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class AuthenticationController extends BaseController {

    public static final String GITHUB_OAUTH_REDIRECT_URI =
            "/user/github/oauth_code";

    public static final String LOGOUT_URI = "/logout";

    private GithubService githubService;

    @Autowired
    public AuthenticationController(GithubService githubService) {
        this.githubService = githubService;
    }

    @GetMapping(GITHUB_OAUTH_REDIRECT_URI)
    public String loginWithGithub(
            @RequestParam("code") String code,
            HttpServletResponse response
    ) {
        githubService.authenticateUser(code, response);
        return redirect(IndexController.BASE_URI);
    }

    @GetMapping(LOGOUT_URI)
    public String logOut(HttpServletResponse response) {
        githubService.removeAuthenticationCookie(response);
        return redirect(IndexController.BASE_URI);
    }
}

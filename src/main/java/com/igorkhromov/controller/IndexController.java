package com.igorkhromov.controller;

import com.igorkhromov.attribute.Github;
import com.igorkhromov.attribute.Repository;
import com.igorkhromov.attribute.User;
import com.igorkhromov.service.GithubService;
import com.igorkhromov.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController extends BaseController {

    public static final String BASE_URI = "/";

    public static final String INDEX_PAGE = "index";

    public static final String GITHUB_ATTR = "github";

    public static final String USER_ATTR = "user";

    public static final String REPO_ATTR = "repo";

    private SecurityService securityService;

    private GithubService githubService;

    @Autowired
    public IndexController(
            SecurityService securityService,
            GithubService githubService
    ) {
        this.securityService = securityService;
        this.githubService = githubService;
    }

    @GetMapping(BASE_URI)
    public String getIndexPage(Model model, HttpServletRequest request)
    {
        Github github = new Github(githubService.getGithubOAuthUrl());
        model.addAttribute(GITHUB_ATTR, github);

        User user = new User(securityService.isAuthorized(request));
        model.addAttribute(USER_ATTR, user);

        Repository repository = new Repository();
        if(user.isAuthorized()) {
            repository.setExists(githubService.isRepositoryExists(request));
        }
        repository.setGithubURL(githubService.getGithubRepoUrl());
        repository.setForkURI(RepositoryController.FORK_REPOSITORY_URI);
        repository.setRemoveURI(RepositoryController.REMOVE_REPOSITORY_URI);
        model.addAttribute(REPO_ATTR, repository);

        return INDEX_PAGE;
    }
}

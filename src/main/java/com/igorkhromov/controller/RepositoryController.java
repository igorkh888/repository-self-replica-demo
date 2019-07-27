package com.igorkhromov.controller;

import com.igorkhromov.service.GithubService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import javax.servlet.http.HttpServletRequest;

@Controller
public class RepositoryController extends BaseController {

    public static final String FORK_REPOSITORY_URI = "/repo/fork";

    public static final String REMOVE_REPOSITORY_URI = "/repo";

    private GithubService githubService;

    public RepositoryController(GithubService githubService) {
        this.githubService = githubService;
    }

    @GetMapping(FORK_REPOSITORY_URI)
    public String forkRepository(HttpServletRequest request) {
        githubService.forkRepository(request);
        return redirect(IndexController.BASE_URI);
    }

    @GetMapping(REMOVE_REPOSITORY_URI)
    public String removeRepository(HttpServletRequest request) {

        return redirect(IndexController.BASE_URI);
    }
}

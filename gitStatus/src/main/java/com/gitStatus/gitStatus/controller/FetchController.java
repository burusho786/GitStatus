package com.gitStatus.gitStatus.controller;

import com.gitStatus.gitStatus.entity.RepositoryEntity;
import com.gitStatus.gitStatus.service.GithubFetchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/fetch")
@RequiredArgsConstructor
public class FetchController {

    private final GithubFetchService githubFetchService;

    @GetMapping("/all")
    public String fetchEverything() {

        return githubFetchService.fetchAllCategories();
    }

    @GetMapping("/{category}")
    public String fetchCategory(

            @PathVariable String category

    ) {

        List<RepositoryEntity> repos =
                githubFetchService.fetchCategory(
                        category
                );

        return "Fetched "
                + repos.size()
                + " repositories for "
                + category;
    }


    // the type of repos that are get from the github api, like ai, backend, frontend, security, devops
    @GetMapping("/categories")
    public Set<String> categories() {

        return githubFetchService
                .getSupportedCategories();
    }
}
package com.gitStatus.gitStatus.controller;

import com.gitStatus.gitStatus.entity.RepositoryEntity;
import com.gitStatus.gitStatus.service.RepoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/repos")
@RequiredArgsConstructor
public class FetchFromMongo {

    private final RepoService repoService;

    @GetMapping
    public List<RepositoryEntity> getAllRepositories() {

        return repoService.getAllRepositories();
    }

    @GetMapping("/category/{category}")
    public List<RepositoryEntity> getRepositoriesByCategory(

            @PathVariable String category

    ) {

        return repoService
                .getRepositoriesByCategory(category);
    }

    @GetMapping("/{githubId}")
    public RepositoryEntity getRepository(

            @PathVariable Long githubId

    ) {

        return repoService
                .getRepositoryByGithubId(githubId);
    }

    @GetMapping("/categories")
    public Set<String> getCategories() {

        return repoService.getAvailableCategories();
    }
}
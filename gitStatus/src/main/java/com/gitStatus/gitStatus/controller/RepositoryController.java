// src/main/java/com/gitStatus/gitStatus/controller/RepositoryController.java
package com.gitStatus.gitStatus.controller;

import com.gitStatus.gitStatus.dto.PageResponse;
import com.gitStatus.gitStatus.dto.RepoResponse;
import com.gitStatus.gitStatus.service.RepoService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * Read-only frontend surface for ranked repositories (ruleset §9).
 *
 * Contract:
 *   GET /api/repos                     -> paginated, ranked list
 *   GET /api/repos/category/{category} -> paginated, category-filtered
 *   GET /api/repos/categories          -> supported category names
 *   GET /api/repos/{githubId}          -> single repo
 *
 * Guarantees:
 *   - Never returns the persistence entity; always RepoResponse.
 *   - Pagination is enforced by Mongo (Pageable), never in memory.
 *   - size is capped at 100.
 *   - Errors are RFC 7807 ProblemDetail via GlobalExceptionHandler.
 *   - Never calls GitHub. Read path only touches MongoDB.
 */
@RestController
@RequestMapping("/api/repos")
@RequiredArgsConstructor
@Validated
public class RepositoryController {

    private static final int MAX_PAGE_SIZE = 100;

    private final RepoService repoService;

    @GetMapping
    public PageResponse<RepoResponse> getAllRepositories(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(MAX_PAGE_SIZE) int size
    ) {
        var pageable = PageRequest.of(page, size, trendingSort());
        return PageResponse.of(
                repoService.getAllRepositories(pageable),
                RepoResponse::from
        );
    }

    @GetMapping("/category/{category}")
    public PageResponse<RepoResponse> getRepositoriesByCategory(
            @PathVariable
            @Pattern(
                    regexp = "[A-Za-z0-9+#._-]{1,32}",
                    message = "category must match [A-Za-z0-9+#._-]{1,32}"
            )
            String category,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(MAX_PAGE_SIZE) int size
    ) {
        var pageable = PageRequest.of(page, size, trendingSort());
        return PageResponse.of(
                repoService.getRepositoriesByCategory(category, pageable),
                RepoResponse::from
        );
    }

    @GetMapping("/categories")
    public Set<String> getCategories() {
        return repoService.getAvailableCategories();
    }

    @GetMapping("/{githubId}")
    public RepoResponse getRepository(@PathVariable @Min(1) Long githubId) {
        return RepoResponse.from(
                repoService.getRepositoryByGithubId(githubId)
        );
    }

    /**
     * Trending order, highest first.
     * attentionScore is the ranked 0-100 value computed at ingestion time
     * by RankingService. The remaining keys are deterministic tiebreakers.
     */
    private Sort trendingSort() {
        return Sort.by(
                Sort.Order.desc("attentionScore"),
                Sort.Order.desc("rawScore"),
                Sort.Order.desc("stars"),
                Sort.Order.desc("updatedAt")
        );
    }
}
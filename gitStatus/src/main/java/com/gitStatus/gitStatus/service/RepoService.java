// src/main/java/com/gitStatus/gitStatus/service/RepoService.java
package com.gitStatus.gitStatus.service;

import com.gitStatus.gitStatus.entity.RepositoryEntity;
import com.gitStatus.gitStatus.exception.ResourceNotFoundException;
import com.gitStatus.gitStatus.repository.RepoMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RepoService {

    private final RepoMongoRepository repoMongoRepository;

    // ----- Existing API (kept for backward compatibility) -----

    public List<RepositoryEntity> getAllRepositories() {
        return repoMongoRepository.findAll();
    }

    public List<RepositoryEntity> getRepositoriesByCategory(String category) {
        return repoMongoRepository.findByCategoryOrderByStarsDesc(category);
    }

    public RepositoryEntity getRepositoryByGithubId(Long githubId) {
        return repoMongoRepository
                .findByGithubId(githubId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Repository not found: " + githubId));
    }

    public Set<String> getAvailableCategories() {
        return repoMongoRepository.findAll()
                .stream()
                .map(RepositoryEntity::getCategory)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    // ----- New paginated read API (ruleset §8/§9) -----

    public Page<RepositoryEntity> getAllRepositories(Pageable pageable) {
        return repoMongoRepository.findAll(pageable);
    }

    public Page<RepositoryEntity> getRepositoriesByCategory(String category, Pageable pageable) {
        return repoMongoRepository.findByCategory(category, pageable);
    }
}
package com.gitStatus.gitStatus.service;

import com.gitStatus.gitStatus.entity.RepositoryEntity;
import com.gitStatus.gitStatus.repository.RepoMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RepoService {

    private final RepoMongoRepository repoMongoRepository;

    public List<RepositoryEntity> getAllRepositories() {

        return repoMongoRepository.findAll();
    }

    public List<RepositoryEntity> getRepositoriesByCategory(
            String category
    ) {

        return repoMongoRepository
                .findByCategoryOrderByStarsDesc(category);
    }

    public RepositoryEntity getRepositoryByGithubId(
            Long githubId
    ) {

        return repoMongoRepository
                .findByGithubId(githubId)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Repository not found"
                        )
                );
    }

    public Set<String> getAvailableCategories() {

        return repoMongoRepository.findAll()
                .stream()
                .map(RepositoryEntity::getCategory)
                .collect(Collectors.toSet());
    }
}
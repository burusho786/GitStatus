package com.gitStatus.gitStatus.repository;

import com.gitStatus.gitStatus.entity.RepoAiAnalysis;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RepoAiAnalysisRepository
        extends MongoRepository<RepoAiAnalysis, String> {

    Optional<RepoAiAnalysis> findByGithubId(
            Long githubId
    );
}
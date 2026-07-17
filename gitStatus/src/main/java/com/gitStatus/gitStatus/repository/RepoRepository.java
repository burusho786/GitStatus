package com.gitStatus.gitStatus.repository;

import com.gitStatus.gitStatus.entity.RepositoryEntity;
import com.gitStatus.gitStatus.model.RepositoryDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepoRepository
        extends MongoRepository<RepositoryDocument, String> {

    List<RepositoryDocument> findByCategoryOrderByScoreDesc(
            String category
    );

    Optional<RepositoryDocument> findByGithubId(
            Long githubId
    );

}
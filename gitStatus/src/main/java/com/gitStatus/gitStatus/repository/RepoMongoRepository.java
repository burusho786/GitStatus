package com.gitStatus.gitStatus.repository;

import com.gitStatus.gitStatus.entity.RepositoryEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface RepoMongoRepository extends MongoRepository<RepositoryEntity,String> {

    Optional<RepositoryEntity> findByGithubId(Long githubId);

    List<RepositoryEntity> findByCategoryOrderByStarsDesc(String category);

}

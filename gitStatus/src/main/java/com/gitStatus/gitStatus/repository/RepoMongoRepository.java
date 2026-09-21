package com.gitStatus.gitStatus.repository;

import com.gitStatus.gitStatus.entity.RepositoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface RepoMongoRepository extends MongoRepository<RepositoryEntity,String> {

    Optional<RepositoryEntity> findByGithubId(Long githubId);

    List<RepositoryEntity> findByCategoryOrderByStarsDesc(String category);

    Page<RepositoryEntity> findByCategory(String category, Pageable pageable);


}

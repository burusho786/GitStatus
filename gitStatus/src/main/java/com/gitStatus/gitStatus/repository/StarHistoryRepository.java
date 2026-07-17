package com.gitStatus.gitStatus.repository;


import com.gitStatus.gitStatus.model.StarHistory;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface StarHistoryRepository
        extends MongoRepository<StarHistory, String> {

    List<StarHistory> findByGithubIdOrderByDate(
            Long githubId
    );
}
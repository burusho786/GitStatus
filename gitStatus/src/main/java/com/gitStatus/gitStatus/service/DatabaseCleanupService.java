package com.gitStatus.gitStatus.service;

import com.gitStatus.gitStatus.repository.RepoMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DatabaseCleanupService {

    private final RepoMongoRepository repoMongoRepository;

    public String cleanRepositories() {

        long count = repoMongoRepository.count();

        repoMongoRepository.deleteAll();

        return count + " repositories deleted successfully.";
    }
}
package com.gitStatus.gitStatus.service;


import com.gitStatus.gitStatus.model.RepositoryDocument;
import com.gitStatus.gitStatus.repository.RepoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CacheService {

    private final RedisTemplate<String, Object>
            redisTemplate;

    private final RepoRepository repoRepository;

    private static final Duration TTL =
            Duration.ofHours(25);

    public void rebuildCaches() {

        List<RepositoryDocument> allRepos =
                repoRepository.findAll();

        redisTemplate.opsForValue().set(
                "trending:all",
                allRepos,
                TTL
        );

        Set<String> categories =

                allRepos.stream()
                        .map(
                                RepositoryDocument::getCategory
                        )
                        .collect(Collectors.toSet());

        for (String category : categories) {

            List<RepositoryDocument> repos =

                    repoRepository
                            .findByCategoryOrderByScoreDesc(
                                    category
                            );

            redisTemplate.opsForValue().set(
                    "trending:" + category,
                    repos,
                    TTL
            );
        }
    }
}
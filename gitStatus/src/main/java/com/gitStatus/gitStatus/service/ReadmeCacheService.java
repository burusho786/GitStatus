package com.gitStatus.gitStatus.service;


import com.gitStatus.gitStatus.entity.RepositoryEntity;
import com.gitStatus.gitStatus.repository.RepoMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReadmeCacheService {

    private final RestClient restClient;

    private final RedisTemplate<String, Object> redisTemplate;

    private final RepoMongoRepository repoMongoRepository;

    @Value("${github.token}")
    private String githubToken;

    private static final Duration README_TTL =
            Duration.ofHours(12);

    /*
     * Fetch and cache the README of
     * a single repository.
     */
    public String getReadme(Long githubId) {

        RepositoryEntity repo =

                repoMongoRepository
                        .findByGithubId(githubId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Repository not found in MongoDB."
                                )
                        );

        String redisKey =
                "readme:" + githubId;

        String cachedReadme =

                (String) redisTemplate
                        .opsForValue()
                        .get(redisKey);

        if (cachedReadme != null) {

            System.out.println(
                    "README loaded from Redis."
            );

            return cachedReadme;
        }

        String githubUrl =

                "https://api.github.com/repos/"
                        + repo.getOwner()
                        + "/"
                        + repo.getName()
                        + "/readme";

        Map<String, Object> response =

                restClient
                        .get()
                        .uri(githubUrl)
                        .header(
                                "Authorization",
                                "Bearer " + githubToken
                        )
                        .header(
                                "Accept",
                                "application/vnd.github+json"
                        )
                        .retrieve()
                        .body(Map.class);

        if (response == null) {

            throw new RuntimeException(
                    "README not found on GitHub."
            );
        }

        String encodedContent =

                (String) response.get("content");

        if (encodedContent == null) {

            throw new RuntimeException(
                    "README content is empty."
            );
        }

        encodedContent =
                encodedContent.replace(
                        "\n",
                        ""
                );

        String decodedReadme =

                new String(

                        Base64
                                .getDecoder()
                                .decode(encodedContent),

                        StandardCharsets.UTF_8
                );

        redisTemplate
                .opsForValue()
                .set(

                        redisKey,

                        decodedReadme,

                        README_TTL
                );

        System.out.println(
                "README saved in Redis for repository: "
                        + repo.getFullName()
        );

        return decodedReadme;
    }

    /*
     * Fetch README files for every
     * repository stored in MongoDB.
     */
    public String cacheAllReadmes() {

        List<RepositoryEntity> repositories =
                repoMongoRepository.findAll();

        int successCount = 0;

        int failedCount = 0;

        for (RepositoryEntity repo : repositories) {

            try {

                getReadme(
                        repo.getGithubId()
                );

                successCount++;

            } catch (Exception e) {

                failedCount++;

                System.out.println(

                        "Failed to fetch README for: "

                                + repo.getFullName()

                                + " | "

                                + e.getMessage()
                );
            }
        }

        return "README cache completed.\n"
                + "Success: "
                + successCount
                + "\nFailed: "
                + failedCount;
    }

    public int cacheReadmeBatch(
            int batchSize
    ) {

        List<RepositoryEntity> repositories =

                repoMongoRepository
                        .findAll()
                        .stream()
                        .filter(repo ->

                                !Boolean.TRUE.equals(

                                        redisTemplate.hasKey(
                                                "readme:" +
                                                        repo.getGithubId()
                                        )
                                )
                        )
                        .limit(batchSize)
                        .toList();

        int count = 0;

        for (RepositoryEntity repo : repositories) {

            try {

                getReadme(
                        repo.getGithubId()
                );

                count++;

            } catch (Exception e) {

                System.out.println(

                        "Failed for repo: "

                                + repo.getGithubId()
                );
            }
        }

        return count;
    }
    /*
     * Remove one README from Redis.
     */
    public void deleteReadmeCache(

            Long githubId

    ) {

        redisTemplate.delete(

                "readme:" + githubId
        );
    }

    /*
     * Check whether a README exists
     * inside Redis.
     */
    public boolean readmeExistsInCache(

            Long githubId

    ) {

        return Boolean.TRUE.equals(

                redisTemplate.hasKey(

                        "readme:" + githubId
                )
        );
    }

    /*
     * Return all Redis README keys.
     */
    public List<String> getAllCachedReadmeKeys() {

        return redisTemplate
                .keys("readme:*")
                .stream()
                .toList();
    }

    /*
     * Delete all README caches.
     */
    public String clearAllReadmeCache() {

        List<String> keys =
                redisTemplate
                        .keys("readme:*")
                        .stream()
                        .toList();

        redisTemplate.delete(keys);

        return "Deleted "
                + keys.size()
                + " cached README files.";
    }
}
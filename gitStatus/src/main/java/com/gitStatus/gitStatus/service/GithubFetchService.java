package com.gitStatus.gitStatus.service;

import com.gitStatus.gitStatus.entity.RepositoryEntity;
import com.gitStatus.gitStatus.repository.RepoMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class GithubFetchService {

    private final RestClient restClient;

    private final RepoMongoRepository repoMongoRepository;

    private final RankingService rankingService;

    @Value("${github.token}")
    private String githubToken;

    private static final Map<String, List<String>> CATEGORY_TOPICS = Map.of(

            "ai",
            List.of(
                    "artificial-intelligence",
                    "machine-learning",
                    "deep-learning",
                    "llm",
                    "neural-network"
            ),

            "cybersecurity",
            List.of(
                    "cybersecurity",
                    "security",
                    "pentesting",
                    "malware"
            ),

            "backend",
            List.of(
                    "spring-boot",
                    "java",
                    "nodejs",
                    "api"
            ),

            "frontend",
            List.of(
                    "react",
                    "nextjs",
                    "vue",
                    "angular"
            ),

            "devops",
            List.of(
                    "docker",
                    "kubernetes",
                    "terraform",
                    "devops"
            )
    );

    public String fetchAllCategories() {

        repoMongoRepository.deleteAll();

        List<RepositoryEntity> allRepositories =
                new ArrayList<>();

        for (String category : CATEGORY_TOPICS.keySet()) {

            allRepositories.addAll(
                    fetchCategory(category)
            );
        }

        rankingService.rankRepositories(
                allRepositories
        );

        repoMongoRepository.saveAll(
                allRepositories
        );

        return "Fetched and ranked "
                + allRepositories.size()
                + " repositories.";
    }

    public List<RepositoryEntity> fetchCategory(
            String category
    ) {

        List<String> topics =
                CATEGORY_TOPICS.get(category);

        if (topics == null) {

            throw new RuntimeException(
                    "Category not found: "
                            + category
            );
        }

        List<RepositoryEntity> repositories =
                new ArrayList<>();

        for (String topic : topics) {

            String url =
                    "https://api.github.com/search/repositories"
                            + "?q=topic:" + topic
                            + "+stars:>100"
                            + "&sort=stars"
                            + "&order=desc"
                            + "&per_page=50";

            Map<String, Object> response =
                    restClient
                            .get()
                            .uri(url)
                            .header(
                                    "Authorization",
                                    "Bearer " + githubToken
                            )
                            .retrieve()
                            .body(Map.class);

            if (response == null) {
                continue;
            }

            List<Map<String, Object>> items =
                    (List<Map<String, Object>>)
                            response.get("items");

            if (items == null) {
                continue;
            }

            for (Map<String, Object> item : items) {

                Long githubId =
                        ((Number) item.get("id"))
                                .longValue();

                boolean exists = repositories
                        .stream()
                        .anyMatch(repo ->
                                repo.getGithubId()
                                        .equals(githubId)
                        );

                if (exists) {
                    continue;
                }

                RepositoryEntity repo =
                        mapRepository(
                                item,
                                category
                        );

                repositories.add(repo);
            }
        }

        return repositories;
    }

    private RepositoryEntity mapRepository(

            Map<String, Object> item,
            String category

    ) {

        Map<String, Object> owner =
                (Map<String, Object>)
                        item.get("owner");

        return RepositoryEntity.builder()

                .githubId(
                        ((Number) item.get("id"))
                                .longValue()
                )

                .name(
                        (String) item.get("name")
                )

                .fullName(
                        (String) item.get("full_name")
                )

                .owner(
                        (String) owner.get("login")
                )

                .description(
                        (String) item.get("description")
                )

                .language(
                        (String) item.get("language")
                )

                .stars(
                        ((Number) item.get("stargazers_count"))
                                .intValue()
                )

                .forks(
                        ((Number) item.get("forks_count"))
                                .intValue()
                )

                .watchers(
                        ((Number) item.get("watchers_count"))
                                .intValue()
                )

                .issues(
                        ((Number) item.get("open_issues_count"))
                                .intValue()
                )

                .htmlUrl(
                        (String) item.get("html_url")
                )

                .avatarUrl(
                        (String) owner.get("avatar_url")
                )

                .category(
                        category
                )

                .updatedAt(
                        LocalDateTime.parse(
                                ((String) item.get("updated_at"))
                                        .replace(
                                                "Z",
                                                ""
                                        )
                        )
                )

                .fetchedAt(
                        LocalDateTime.now()
                )

                .build();
    }

    public Set<String> getSupportedCategories() {

        return CATEGORY_TOPICS.keySet();
    }
}
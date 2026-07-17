package com.gitStatus.gitStatus.service;

import com.gitStatus.gitStatus.model.RepositoryDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GithubService {

    private final RestTemplate restTemplate;

    @Value("${github.token}")
    private String token;

    public List<RepositoryDocument> fetchRepositories() {

        List<RepositoryDocument> repos = new ArrayList<>();

        List<String> categories = List.of(
                "ai",
                "backend",
                "frontend",
                "security",
                "devops"
        );

        for (String category : categories) {

            String url =
                    "https://api.github.com/search/repositories" +
                            "?q=topic:" + category +
                            "&sort=stars&order=desc&per_page=100";

            HttpHeaders headers = new HttpHeaders();

            headers.setBearerAuth(token);

            HttpEntity<?> entity =
                    new HttpEntity<>(headers);

            ResponseEntity<JsonNode> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.GET,
                            entity,
                            JsonNode.class
                    );

            JsonNode items =
                    response.getBody().get("items");

            for (JsonNode node : items) {

                repos.add(mapRepository(node, category));
            }
        }

        return repos;
    }

    private RepositoryDocument mapRepository(
            JsonNode node,
            String category
    ) {

        return RepositoryDocument.builder()
                .githubId(node.get("id").asLong())
                .name(node.get("name").asText())
                .owner(node.get("owner").get("login").asText())
                .fullName(node.get("full_name").asText())
                .description(node.path("description").asText())
                .stars(node.get("stargazers_count").asInt())
                .forks(node.get("forks_count").asInt())
                .watchers(node.get("watchers_count").asInt())
                .issues(node.get("open_issues_count").asInt())
                .htmlUrl(node.get("html_url").asText())
                .avatarUrl(
                        node.get("owner")
                                .get("avatar_url")
                                .asText()
                )
                .category(category)
                .updatedAt(
                        LocalDateTime.parse(
                                node.get("updated_at").asText()
                                        .replace("Z", "")
                        )
                )
                .lastFetchedAt(LocalDateTime.now())
                .build();
    }
}
// src/main/java/com/gitStatus/gitStatus/dto/RepoResponse.java
package com.gitStatus.gitStatus.dto;

import com.gitStatus.gitStatus.entity.RepositoryEntity;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class RepoResponse {

    private Long githubId;
    private String name;
    private String fullName;
    private String owner;
    private String description;
    private String language;
    private List<String> topics;
    private String category;
    private Integer stars;
    private Integer forks;
    private Integer watchers;
    private Integer issues;
    private Double score;              // rawScore (weighted)
    private Integer attentionScore;    // 0-100, from RankingService
    private String htmlUrl;
    private String homepage;
    private String avatarUrl;
    private LocalDateTime updatedAt;

    public static RepoResponse from(RepositoryEntity e) {
        return RepoResponse.builder()
                .githubId(e.getGithubId())
                .name(e.getName())
                .fullName(e.getFullName())
                .owner(e.getOwner())
                .description(e.getDescription())
                .language(e.getLanguage())
                .topics(e.getTopics())
                .category(e.getCategory())
                .stars(e.getStars())
                .forks(e.getForks())
                .watchers(e.getWatchers())
                .issues(e.getIssues())
                .score(e.getRawScore())
                .attentionScore(e.getAttentionScore())
                .htmlUrl(e.getHtmlUrl())
                .homepage(null)
                .avatarUrl(e.getAvatarUrl())
                .updatedAt(e.getUpdatedAt())
                .build();
    }
}
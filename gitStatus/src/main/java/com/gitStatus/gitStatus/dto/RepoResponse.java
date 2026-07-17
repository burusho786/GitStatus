package com.gitStatus.gitStatus.dto;

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

    private Double score;

    private String htmlUrl;

    private String homepage;

    private String avatarUrl;

    private LocalDateTime updatedAt;
}
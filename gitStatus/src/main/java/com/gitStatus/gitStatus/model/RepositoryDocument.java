package com.gitStatus.gitStatus.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "repositories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RepositoryDocument {

    @Id
    private String id;

    private Long githubId;

    private String name;

    private String owner;

    private String fullName;

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

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime lastFetchedAt;
}
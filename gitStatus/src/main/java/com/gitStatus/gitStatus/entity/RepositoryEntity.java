package com.gitStatus.gitStatus.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "repositories")
public class RepositoryEntity {

    @Id
    private String id;

    private Long githubId;

    private String name;

    private String owner;

    private String description;

    private Integer stars;

    private Integer forks;

    private Integer watchers;

    private Integer issues;

    private String avatarUrl;
    private String category;

    private String language;

    private List<String> topics;

    private String htmlUrl;

    private LocalDateTime fetchedAt;

    private Double rawScore;

    private Integer attentionScore;

    private LocalDateTime updatedAt;

    private String fullName;
}
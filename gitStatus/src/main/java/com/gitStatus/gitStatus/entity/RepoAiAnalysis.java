package com.gitStatus.gitStatus.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "repo_ai_analysis")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepoAiAnalysis {

    @Id
    private String id;

    private Long githubId;

    private String repositoryExplainer;

    private String installationGuide;

    private String trendingReason;

    private List<String> learningPath;

    private LocalDateTime generatedAt;
}
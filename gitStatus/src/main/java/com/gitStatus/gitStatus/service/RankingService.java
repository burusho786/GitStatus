package com.gitStatus.gitStatus.service;

import com.gitStatus.gitStatus.entity.RepositoryEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class RankingService {

    public double calculateRawScore(
            RepositoryEntity repo
    ) {

        return

                (repo.getStars() * 0.50)

                        +

                        (repo.getForks() * 1.50)

                        +

                        (repo.getWatchers() * 0.30)

                        +

                        (repo.getIssues() * 0.10);
    }

    public int calculateAttentionScore(

            RepositoryEntity repo,

            double maxRawScore

    ) {

        double rawScore =
                calculateRawScore(repo);

        long daysSinceUpdate =
                ChronoUnit.DAYS.between(
                        repo.getUpdatedAt(),
                        LocalDateTime.now()
                );

        double recencyFactor =
                Math.max(
                        0.1,
                        1 - (daysSinceUpdate / 365.0)
                );

        double normalizedScore =

                (rawScore * recencyFactor)

                        / maxRawScore;

        return (int) Math.round(
                normalizedScore * 100
        );
    }

    public void rankRepositories(

            List<RepositoryEntity> repos

    ) {

        double maxRawScore =

                repos.stream()

                        .mapToDouble(
                                this::calculateRawScore
                        )

                        .max()

                        .orElse(1);

        repos.forEach(repo -> {

            repo.setRawScore(

                    calculateRawScore(repo)
            );

            repo.setAttentionScore(

                    calculateAttentionScore(
                            repo,
                            maxRawScore
                    )
            );
        });
    }
}
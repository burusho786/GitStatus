package com.gitStatus.gitStatus.ollamaservices;

import com.gitStatus.gitStatus.entity.RepoAiAnalysis;
import com.gitStatus.gitStatus.repository.RepoAiAnalysisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RepoAiService {

    private final RedisTemplate<String, Object> redisTemplate;

    private final RepoAiAnalysisRepository analysisRepository;

    private final OllamaService ollamaService;

    private final PromptService promptService;

    public RepoAiAnalysis explainRepository(
            Long githubId
    ) {

        // Already generated?

        RepoAiAnalysis existing =

                analysisRepository
                        .findByGithubId(
                                githubId
                        )
                        .orElse(null);

        if (existing != null) {

            return existing;
        }

        // Load README from Redis

        String readme =

                (String) redisTemplate
                        .opsForValue()
                        .get(
                                "readme:" + githubId
                        );

        if (readme == null) {

            throw new RuntimeException(
                    "README not found in Redis."
            );
        }

        System.out.println(
                "README length = " + readme.length()
        );

        before:

        // Limit huge READMEs

        if (readme.length() > 400) {

            readme = readme.substring(
                    0,
                    400


            );
            System.out.println(
                    "README length = " + readme.length()
            );

        }



        String prompt =

                promptService
                        .buildRepositoryPrompt(
                                readme
                        );


        System.out.println(
                "Sending prompt to Ollama...");
        String explanation =

                ollamaService
                        .askModel(
                                prompt
                        );
        System.out.println(
                "Ollama response:"
        );

        System.out.println(
                explanation
        );

        RepoAiAnalysis analysis =

                RepoAiAnalysis.builder()

                        .githubId(
                                githubId
                        )

                        .repositoryExplainer(
                                explanation
                        )

                        .generatedAt(
                                LocalDateTime.now()
                        )

                        .build();

        return analysisRepository.save(
                analysis
        );
    }


}
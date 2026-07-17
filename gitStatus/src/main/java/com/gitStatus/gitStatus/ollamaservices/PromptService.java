package com.gitStatus.gitStatus.ollamaservices;


import org.springframework.stereotype.Service;

@Service
public class PromptService {


    public String buildRepositoryPrompt(
            String readme
    ) {

        return """

                You are a senior software engineer.
                
                            Read the GitHub README below and answer:
                
                            • What does the project do?
                            • What problem does it solve?
                            • Who should use it?
                
                            Rules:
                
                            - Maximum 80 words.
                            - Use simple language.
                            - No technical jargon.
                            - Write one concise paragraph.
                
                            README:
                
                            %s

                """.formatted(readme);
    }




        public String buildTrendingPrompt(
                String repoName,
                Integer stars,
                Integer forks,
                Integer issues,
                Integer watchers,
                Double attentionScore,
                String description
        ) {

            return """
                You are an expert GitHub analyst.

                Repository: %s

                Description:
                %s

                Statistics:

                - Stars: %d
                - Forks: %d
                - Open Issues: %d
                - Watchers: %d
                - Attention Score: %.2f

                Explain in 3 short bullet points:

                1. Why developers are interested in this repository.
                2. Which repository metrics contribute most to its popularity.
                3. Whether it appears to be actively maintained.

                Keep the answer under 120 words.

                Output only the explanation.
                """
                    .formatted(
                            repoName,
                            description,
                            stars,
                            forks,
                            issues,
                            watchers,
                            attentionScore
                    );
        }

}

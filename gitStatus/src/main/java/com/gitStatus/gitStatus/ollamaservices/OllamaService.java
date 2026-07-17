package com.gitStatus.gitStatus.ollamaservices;



import com.gitStatus.gitStatus.dto.OllamaRequest;
import com.gitStatus.gitStatus.dto.OllamaResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class OllamaService {

    private final RestClient restClient;

    @Value("${ollama.model}")
    private String model;

    public OllamaService(
            @Qualifier("ollamaRestClient")
            RestClient restClient
    ) {
        this.restClient = restClient;
    }

    public String askModel(String prompt) {

        OllamaRequest request = OllamaRequest.builder()
                .model(model)
                .prompt(prompt)
                .stream(false)
                .build();
        System.out.println(restClient);
        System.out.println(model);
        OllamaResponse response = restClient
                .post()
                .uri("/api/generate")
                .body(request)
                .retrieve()
                .body(OllamaResponse.class);

        if (response == null) {
            throw new RuntimeException(
                    "No response from Ollama."
            );
        }

        return response.getResponse();
    }
}
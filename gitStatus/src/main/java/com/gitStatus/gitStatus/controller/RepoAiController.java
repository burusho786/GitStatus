package com.gitStatus.gitStatus.controller;

import com.gitStatus.gitStatus.entity.RepoAiAnalysis;
import com.gitStatus.gitStatus.ollamaservices.RepoAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class RepoAiController {

    private final RepoAiService repoAiService;

    @GetMapping("/explain/{githubId}")
    public RepoAiAnalysis explainRepository(

            @PathVariable
            Long githubId

    ) {

        return repoAiService
                .explainRepository(
                        githubId
                );
    }
}
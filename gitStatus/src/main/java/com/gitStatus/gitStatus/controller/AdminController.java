package com.gitStatus.gitStatus.controller;



import com.gitStatus.gitStatus.service.DatabaseCleanupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final DatabaseCleanupService cleanupService;

    @DeleteMapping("/repositories")
    public String deleteRepositories() {

        return cleanupService.cleanRepositories();
    }
}

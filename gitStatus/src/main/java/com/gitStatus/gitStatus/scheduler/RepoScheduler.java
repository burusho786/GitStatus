package com.gitStatus.gitStatus.scheduler;

import com.gitStatus.gitStatus.service.RepoService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RepoScheduler {

    private final RepoService repoService;

    @Scheduled(
            cron = "${scheduler.cron}",
            zone = "UTC"
    )
    public void refresh() {

       // repoService.refreshRepositories();
    }
}
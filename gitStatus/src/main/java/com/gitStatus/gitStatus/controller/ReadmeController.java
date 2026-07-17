package com.gitStatus.gitStatus.controller;


import com.gitStatus.gitStatus.service.ReadmeCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/readme")
@RequiredArgsConstructor
public class ReadmeController {

    private final ReadmeCacheService readmeCacheService;

    @GetMapping("/{githubId}")
    public String getSingleReadme(

            @PathVariable Long githubId
    ) {

        return readmeCacheService.getReadme(
                githubId
        );
    }

    @PostMapping("/cache-all")
    public String cacheAllReadmes() {

        return readmeCacheService.cacheAllReadmes();
    }

    @DeleteMapping("/{githubId}")
    public String deleteReadme(

            @PathVariable Long githubId
    ) {

        readmeCacheService.deleteReadmeCache(
                githubId
        );

        return "README cache deleted.";
    }

    @GetMapping("/exists/{githubId}")
    public boolean exists(

            @PathVariable Long githubId
    ) {

        return readmeCacheService
                .readmeExistsInCache(
                        githubId
                );
    }
    @PostMapping("/cache-batch")
    public String cacheBatch(

            @RequestParam(
                    defaultValue = "50"
            )
            int batchSize

    ) {

        int cachedCount = readmeCacheService
                .cacheReadmeBatch(
                        batchSize
                );

        return "Cached "
                + cachedCount
                + " README files.";
    }
}
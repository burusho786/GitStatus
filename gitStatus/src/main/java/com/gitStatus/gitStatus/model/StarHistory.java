package com.gitStatus.gitStatus.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "star_history")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StarHistory {

    @Id
    private String id;

    private Long githubId;

    private Integer stars;

    private LocalDate date;
}
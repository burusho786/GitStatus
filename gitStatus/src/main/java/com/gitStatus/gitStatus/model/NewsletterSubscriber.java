package com.gitStatus.gitStatus.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "newsletter_subscribers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsletterSubscriber {

    @Id
    private String id;

    private String email;

    private LocalDateTime subscribedAt;
}
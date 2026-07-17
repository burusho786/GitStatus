package com.gitStatus.gitStatus.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class NewsletterRequest {

    @Email
    private String email;
}
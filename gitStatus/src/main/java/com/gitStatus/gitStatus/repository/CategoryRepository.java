package com.gitStatus.gitStatus.repository;


import com.gitStatus.gitStatus.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CategoryRepository
        extends MongoRepository<Category, String> {

    Optional<Category> findBySlug(
            String slug
    );
}

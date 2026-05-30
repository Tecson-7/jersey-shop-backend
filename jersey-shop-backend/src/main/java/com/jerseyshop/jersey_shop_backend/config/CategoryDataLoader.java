package com.jerseyshop.jersey_shop_backend.config;

import com.jerseyshop.jersey_shop_backend.model.Category;
import com.jerseyshop.jersey_shop_backend.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryDataLoader implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    @Override
    public void run(String... args) {

        if (categoryRepository.count() == 0) {

            categoryRepository.save(
                    Category.builder()
                            .name("Club Jerseys")
                            .description("Football club jerseys")
                            .build()
            );

            categoryRepository.save(
                    Category.builder()
                            .name("National Teams")
                            .description("International team jerseys")
                            .build()
            );

            categoryRepository.save(
                    Category.builder()
                            .name("Premier League")
                            .description("Premier League jerseys")
                            .build()
            );

            categoryRepository.save(
                    Category.builder()
                            .name("La Liga")
                            .description("Spanish league jerseys")
                            .build()
            );

            categoryRepository.save(
                    Category.builder()
                            .name("Retro Jerseys")
                            .description("Classic football jerseys")
                            .build()
            );
        }
    }
}
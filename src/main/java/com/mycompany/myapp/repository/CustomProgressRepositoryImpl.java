package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Progress;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import reactor.core.publisher.Mono;

public class CustomProgressRepositoryImpl implements CustomProgressRepository {

    private final R2dbcEntityTemplate template;

    public CustomProgressRepositoryImpl(R2dbcEntityTemplate template) {
        this.template = template;
    }

    @Override
    public <S extends Progress> Mono<S> insertWithCustomId(S entity) {
        // Here you can use R2dbcEntityTemplate to perform an insert operation.
        // This is an example and the actual implementation can vary based on your needs and database type.
        return template.insert(entity);
    }
}

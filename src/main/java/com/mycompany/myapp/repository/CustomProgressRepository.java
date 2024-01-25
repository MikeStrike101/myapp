package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Progress;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

public interface CustomProgressRepository {
    <S extends Progress> Mono<S> insertWithCustomId(S entity);
}

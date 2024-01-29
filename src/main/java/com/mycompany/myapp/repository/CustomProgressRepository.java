package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Progress;
import reactor.core.publisher.Mono;

public interface CustomProgressRepository {
    <S extends Progress> Mono<S> insertWithCustomId(S entity);
}

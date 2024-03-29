package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Progress;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Progress entity.
 */
@SuppressWarnings("unused")
@Repository
@Primary
public interface ProgressRepository extends ReactiveCrudRepository<Progress, Long>, ProgressRepositoryInternal, CustomProgressRepository {
    @Override
    <S extends Progress> Mono<S> save(S entity);

    @Override
    Flux<Progress> findAll();

    @Override
    Mono<Progress> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);

    @Modifying
    @Query("UPDATE progress SET current_lesson = :currentLesson, xp = :xp WHERE id = :id")
    Mono<Integer> updateProgressCustom(Long id, Integer currentLesson, Integer xp);
}

interface ProgressRepositoryInternal {
    <S extends Progress> Mono<S> save(S entity);

    Flux<Progress> findAllBy(Pageable pageable);

    Flux<Progress> findAll();

    Mono<Progress> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Progress> findAllBy(Pageable pageable, Criteria criteria);

}

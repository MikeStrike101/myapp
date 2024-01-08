package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Problem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Problem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProblemRepository extends ReactiveCrudRepository<Problem, Long>, ProblemRepositoryInternal {
    Flux<Problem> findAllBy(Pageable pageable);

    @Override
    <S extends Problem> Mono<S> save(S entity);

    @Override
    Flux<Problem> findAll();

    @Override
    Mono<Problem> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface ProblemRepositoryInternal {
    <S extends Problem> Mono<S> save(S entity);

    Flux<Problem> findAllBy(Pageable pageable);

    Flux<Problem> findAll();

    Mono<Problem> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Problem> findAllBy(Pageable pageable, Criteria criteria);

}

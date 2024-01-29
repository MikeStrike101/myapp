package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.TestCase;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the TestCase entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TestCaseRepository extends ReactiveCrudRepository<TestCase, Long>, TestCaseRepositoryInternal {
    Flux<TestCase> findAllBy(Pageable pageable);

    @Query("SELECT * FROM test_case entity WHERE entity.problem_id = :id")
    Mono<TestCase> findByProblem(Integer questionNumber);

    @Query("SELECT * FROM test_case entity WHERE entity.problem_id IS NULL")
    Flux<TestCase> findAllWhereProblemIsNull();

    @Override
    <S extends TestCase> Mono<S> save(S entity);

    @Override
    Flux<TestCase> findAll();

    @Override
    Mono<TestCase> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface TestCaseRepositoryInternal {
    <S extends TestCase> Mono<S> save(S entity);

    Flux<TestCase> findAllBy(Pageable pageable);

    Flux<TestCase> findAll();

    Mono<TestCase> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<TestCase> findAllBy(Pageable pageable, Criteria criteria);

}

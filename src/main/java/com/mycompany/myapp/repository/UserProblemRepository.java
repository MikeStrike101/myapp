package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.UserProblem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the UserProblem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserProblemRepository extends ReactiveCrudRepository<UserProblem, Long>, UserProblemRepositoryInternal {
    Flux<UserProblem> findAllBy(Pageable pageable);

    @Query("SELECT * FROM user_problem entity WHERE entity.user_id = :id")
    Flux<UserProblem> findByUser(Long id);

    @Query("SELECT * FROM user_problem entity WHERE entity.user_id IS NULL")
    Flux<UserProblem> findAllWhereUserIsNull();

    @Query("SELECT * FROM user_problem entity WHERE entity.problem_id = :id")
    Flux<UserProblem> findByProblem(Long id);

    @Query("SELECT * FROM user_problem entity WHERE entity.problem_id IS NULL")
    Flux<UserProblem> findAllWhereProblemIsNull();

    @Override
    <S extends UserProblem> Mono<S> save(S entity);

    @Override
    Flux<UserProblem> findAll();

    @Override
    Mono<UserProblem> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface UserProblemRepositoryInternal {
    <S extends UserProblem> Mono<S> save(S entity);

    Flux<UserProblem> findAllBy(Pageable pageable);

    Flux<UserProblem> findAll();

    Mono<UserProblem> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<UserProblem> findAllBy(Pageable pageable, Criteria criteria);

}

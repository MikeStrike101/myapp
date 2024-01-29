package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ExecutionCode;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the ExecutionCode entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExecutionCodeRepository extends ReactiveCrudRepository<ExecutionCode, Long>, ExecutionCodeRepositoryInternal {
    Flux<ExecutionCode> findAllBy(Pageable pageable);

    @Override
    <S extends ExecutionCode> Mono<S> save(S entity);

    @Override
    Flux<ExecutionCode> findAll();

    @Override
    Mono<ExecutionCode> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);

    @Query("SELECT * FROM execution_code ec WHERE game_character = :gameCharacterId")
    Flux<ExecutionCode> findByGameCharacterId(@Param("gameCharacterId") Long gameCharacterId);

    Mono<ExecutionCode> findByGameCharacterAndQuestionNumber(Long gameCharacterId, Integer questionNumber);
}

interface ExecutionCodeRepositoryInternal {
    <S extends ExecutionCode> Mono<S> save(S entity);

    Flux<ExecutionCode> findAllBy(Pageable pageable);

    Flux<ExecutionCode> findAll();

    Mono<ExecutionCode> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<ExecutionCode> findAllBy(Pageable pageable, Criteria criteria);

}

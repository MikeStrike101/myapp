package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.GameCharacter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the GameCharacter entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GameCharacterRepository extends ReactiveCrudRepository<GameCharacter, Long>, GameCharacterRepositoryInternal {
    Flux<GameCharacter> findAllBy(Pageable pageable);

    @Override
    Mono<GameCharacter> findOneWithEagerRelationships(Long id);

    @Override
    Flux<GameCharacter> findAllWithEagerRelationships();

    @Override
    Flux<GameCharacter> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM game_character entity WHERE entity.progress_id = :id")
    Flux<GameCharacter> findByProgress(Long id);

    @Query("SELECT * FROM game_character entity WHERE entity.progress_id IS NULL")
    Flux<GameCharacter> findAllWhereProgressIsNull();

    @Query(
        "SELECT entity.* FROM game_character entity JOIN rel_game_character__problem joinTable ON entity.id = joinTable.problem_id WHERE joinTable.problem_id = :id"
    )
    Flux<GameCharacter> findByProblem(Long id);

    @Query("SELECT * FROM game_character entity WHERE entity.user_id = :id")
    Flux<GameCharacter> findByUser(Long id);

    @Query("SELECT * FROM game_character entity WHERE entity.user_id IS NULL")
    Flux<GameCharacter> findAllWhereUserIsNull();

    @Query("SELECT * FROM game_character entity WHERE entity.unique_link = :uniqueLink")
    Mono<GameCharacter> findByUniqueLink(String uniqueLink);

    @Override
    <S extends GameCharacter> Mono<S> save(S entity);

    @Override
    Flux<GameCharacter> findAll();

    @Override
    Mono<GameCharacter> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface GameCharacterRepositoryInternal {
    <S extends GameCharacter> Mono<S> save(S entity);

    Flux<GameCharacter> findAllBy(Pageable pageable);

    Flux<GameCharacter> findAll();

    Mono<GameCharacter> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<GameCharacter> findAllBy(Pageable pageable, Criteria criteria);

    Mono<GameCharacter> findOneWithEagerRelationships(Long id);

    Flux<GameCharacter> findAllWithEagerRelationships();

    Flux<GameCharacter> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}

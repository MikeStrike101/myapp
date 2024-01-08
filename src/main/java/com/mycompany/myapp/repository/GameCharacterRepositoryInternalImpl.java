package com.mycompany.myapp.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import com.mycompany.myapp.domain.GameCharacter;
import com.mycompany.myapp.repository.rowmapper.GameCharacterRowMapper;
import com.mycompany.myapp.repository.rowmapper.ProgressRowMapper;
import com.mycompany.myapp.repository.rowmapper.UserRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.BiFunction;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC custom repository implementation for the GameCharacter entity.
 */
@SuppressWarnings("unused")
class GameCharacterRepositoryInternalImpl extends SimpleR2dbcRepository<GameCharacter, Long> implements GameCharacterRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ProgressRowMapper progressMapper;
    private final UserRowMapper userMapper;
    private final GameCharacterRowMapper gamecharacterMapper;

    private static final Table entityTable = Table.aliased("game_character", EntityManager.ENTITY_ALIAS);
    private static final Table progressTable = Table.aliased("progress", "progress");
    private static final Table userTable = Table.aliased("jhi_user", "e_user");

    public GameCharacterRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ProgressRowMapper progressMapper,
        UserRowMapper userMapper,
        GameCharacterRowMapper gamecharacterMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(GameCharacter.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.progressMapper = progressMapper;
        this.userMapper = userMapper;
        this.gamecharacterMapper = gamecharacterMapper;
    }

    @Override
    public Flux<GameCharacter> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<GameCharacter> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = GameCharacterSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(ProgressSqlHelper.getColumns(progressTable, "progress"));
        columns.addAll(UserSqlHelper.getColumns(userTable, "user"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(progressTable)
            .on(Column.create("progress_id", entityTable))
            .equals(Column.create("id", progressTable))
            .leftOuterJoin(userTable)
            .on(Column.create("user_id", entityTable))
            .equals(Column.create("id", userTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, GameCharacter.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<GameCharacter> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<GameCharacter> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private GameCharacter process(Row row, RowMetadata metadata) {
        GameCharacter entity = gamecharacterMapper.apply(row, "e");
        entity.setProgress(progressMapper.apply(row, "progress"));
        entity.setUser(userMapper.apply(row, "user"));
        return entity;
    }

    @Override
    public <S extends GameCharacter> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Flux<GameCharacter> findAllWithEagerRelationships() {
        return findAllWithEagerRelationships(null);
    }

    @Override
    public Flux<GameCharacter> findAllWithEagerRelationships(Pageable page) {
        // Here you need to define the join logic to fetch related entities eagerly
        // Below is a simplified example
        String sql = page != null
            ? "SELECT * FROM game_character LEFT JOIN progress ON game_character.progress_id = progress.id LEFT JOIN jhi_user ON game_character.user_id = jhi_user.id LIMIT " +
            page.getPageSize() +
            " OFFSET " +
            page.getOffset()
            : "SELECT * FROM game_character LEFT JOIN progress ON game_character.progress_id = progress.id LEFT JOIN jhi_user ON game_character.user_id = jhi_user.id";
        return db.sql(sql).map(this::process).all();
    }

    @Override
    public Mono<GameCharacter> findOneWithEagerRelationships(Long id) {
        String sql =
            "SELECT * FROM game_character LEFT JOIN progress ON game_character.progress_id = progress.id LEFT JOIN jhi_user ON game_character.user_id = jhi_user.id WHERE game_character.id = " +
            id;
        return db.sql(sql).map(this::process).one();
    }
}

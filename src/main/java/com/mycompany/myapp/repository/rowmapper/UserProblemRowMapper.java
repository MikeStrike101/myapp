package com.mycompany.myapp.repository.rowmapper;

import com.mycompany.myapp.domain.UserProblem;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link UserProblem}, with proper type conversions.
 */
@Service
public class UserProblemRowMapper implements BiFunction<Row, String, UserProblem> {

    private final ColumnConverter converter;

    public UserProblemRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link UserProblem} stored in the database.
     */
    @Override
    public UserProblem apply(Row row, String prefix) {
        UserProblem entity = new UserProblem();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setSolvedAt(converter.fromRow(row, prefix + "_solved_at", Instant.class));
        entity.setPassedTestCases(converter.fromRow(row, prefix + "_passed_test_cases", Integer.class));
        entity.setXpAwarded(converter.fromRow(row, prefix + "_xp_awarded", Integer.class));
        entity.setUserId(converter.fromRow(row, prefix + "_user_id", String.class));
        entity.setProblemId(converter.fromRow(row, prefix + "_problem_id", Long.class));
        return entity;
    }
}

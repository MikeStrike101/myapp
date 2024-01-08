package com.mycompany.myapp.repository.rowmapper;

import com.mycompany.myapp.domain.Problem;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Problem}, with proper type conversions.
 */
@Service
public class ProblemRowMapper implements BiFunction<Row, String, Problem> {

    private final ColumnConverter converter;

    public ProblemRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Problem} stored in the database.
     */
    @Override
    public Problem apply(Row row, String prefix) {
        Problem entity = new Problem();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setTitle(converter.fromRow(row, prefix + "_title", String.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setLevel(converter.fromRow(row, prefix + "_level", Integer.class));
        entity.setXpReward(converter.fromRow(row, prefix + "_xp_reward", Integer.class));
        return entity;
    }
}

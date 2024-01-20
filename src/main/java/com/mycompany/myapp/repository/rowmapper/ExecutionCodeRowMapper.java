package com.mycompany.myapp.repository.rowmapper;

import com.mycompany.myapp.domain.ExecutionCode;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link ExecutionCode}, with proper type conversions.
 */
@Service
public class ExecutionCodeRowMapper implements BiFunction<Row, String, ExecutionCode> {

    private final ColumnConverter converter;

    public ExecutionCodeRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link ExecutionCode} stored in the database.
     */
    @Override
    public ExecutionCode apply(Row row, String prefix) {
        ExecutionCode entity = new ExecutionCode();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setQuestionNumber(converter.fromRow(row, prefix + "_question_number", Integer.class));
        entity.setCode(converter.fromRow(row, prefix + "_code", String.class));
        entity.setGameCharacter(converter.fromRow(row, prefix + "_game_character", Integer.class));
        return entity;
    }
}

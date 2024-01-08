package com.mycompany.myapp.repository.rowmapper;

import com.mycompany.myapp.domain.TestCase;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link TestCase}, with proper type conversions.
 */
@Service
public class TestCaseRowMapper implements BiFunction<Row, String, TestCase> {

    private final ColumnConverter converter;

    public TestCaseRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link TestCase} stored in the database.
     */
    @Override
    public TestCase apply(Row row, String prefix) {
        TestCase entity = new TestCase();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setInput(converter.fromRow(row, prefix + "_input", String.class));
        entity.setOutput(converter.fromRow(row, prefix + "_output", String.class));
        entity.setProblemId(converter.fromRow(row, prefix + "_problem_id", Long.class));
        return entity;
    }
}

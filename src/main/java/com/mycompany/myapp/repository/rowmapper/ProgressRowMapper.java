package com.mycompany.myapp.repository.rowmapper;

import com.mycompany.myapp.domain.Progress;
import com.mycompany.myapp.domain.enumeration.Status;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Progress}, with proper type conversions.
 */
@Service
public class ProgressRowMapper implements BiFunction<Row, String, Progress> {

    private final ColumnConverter converter;

    public ProgressRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Progress} stored in the database.
     */
    @Override
    public Progress apply(Row row, String prefix) {
        Progress entity = new Progress();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setStatus(converter.fromRow(row, prefix + "_status", Status.class));
        entity.setCurrentLesson(converter.fromRow(row, prefix + "_current_lesson", Integer.class));
        entity.setXp(converter.fromRow(row, prefix + "_xp", Integer.class));
        return entity;
    }
}

package com.mycompany.myapp.repository.rowmapper;

import com.mycompany.myapp.domain.GameCharacter;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link GameCharacter}, with proper type conversions.
 */
@Service
public class GameCharacterRowMapper implements BiFunction<Row, String, GameCharacter> {

    private final ColumnConverter converter;

    public GameCharacterRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link GameCharacter} stored in the database.
     */
    @Override
    public GameCharacter apply(Row row, String prefix) {
        GameCharacter entity = new GameCharacter();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setEmail(converter.fromRow(row, prefix + "_email", String.class));
        entity.setLevel(converter.fromRow(row, prefix + "_level", Integer.class));
        entity.setExperience(converter.fromRow(row, prefix + "_experience", Integer.class));
        entity.setShape(converter.fromRow(row, prefix + "_shape", String.class));
        entity.setColor(converter.fromRow(row, prefix + "_color", String.class));
        entity.setAccessory(converter.fromRow(row, prefix + "_accessory", String.class));
        entity.setProgrammingLanguage(converter.fromRow(row, prefix + "_programming_language", String.class));
        entity.setUniqueLink(converter.fromRow(row, prefix + "_unique_link", String.class));
        entity.setProgressId(converter.fromRow(row, prefix + "_progress_id", Long.class));
        entity.setUserId(converter.fromRow(row, prefix + "_user_id", String.class));
        return entity;
    }
}

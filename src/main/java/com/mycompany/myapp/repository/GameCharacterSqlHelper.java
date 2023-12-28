package com.mycompany.myapp.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class GameCharacterSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("name", table, columnPrefix + "_name"));
        columns.add(Column.aliased("email", table, columnPrefix + "_email"));
        columns.add(Column.aliased("level", table, columnPrefix + "_level"));
        columns.add(Column.aliased("experience", table, columnPrefix + "_experience"));
        columns.add(Column.aliased("shape", table, columnPrefix + "_shape"));
        columns.add(Column.aliased("color", table, columnPrefix + "_color"));
        columns.add(Column.aliased("accessory", table, columnPrefix + "_accessory"));
        columns.add(Column.aliased("programming_language", table, columnPrefix + "_programming_language"));
        columns.add(Column.aliased("unique_link", table, columnPrefix + "_unique_link"));

        columns.add(Column.aliased("progress_id", table, columnPrefix + "_progress_id"));
        columns.add(Column.aliased("user_id", table, columnPrefix + "_user_id"));
        return columns;
    }
}

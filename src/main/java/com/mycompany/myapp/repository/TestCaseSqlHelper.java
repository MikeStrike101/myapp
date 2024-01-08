package com.mycompany.myapp.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class TestCaseSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("input", table, columnPrefix + "_input"));
        columns.add(Column.aliased("output", table, columnPrefix + "_output"));

        columns.add(Column.aliased("problem_id", table, columnPrefix + "_problem_id"));
        return columns;
    }
}

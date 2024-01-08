package com.mycompany.myapp.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class UserProblemSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("solved_at", table, columnPrefix + "_solved_at"));
        columns.add(Column.aliased("passed_test_cases", table, columnPrefix + "_passed_test_cases"));
        columns.add(Column.aliased("xp_awarded", table, columnPrefix + "_xp_awarded"));

        columns.add(Column.aliased("user_id", table, columnPrefix + "_user_id"));
        columns.add(Column.aliased("problem_id", table, columnPrefix + "_problem_id"));
        return columns;
    }
}

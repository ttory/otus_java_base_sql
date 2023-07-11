/*
 * Copyright 2023.
 * Student's project.
 * Author: Polyanskaya Victory.
 */

package tables;

import db.DBConnector;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class AbsTable {
    private final String tableName;
    private final String primaryKey;
    private final Map<String, String> columns;

    public AbsTable(String tableName, Map<String, String> columns, String primaryKey) {
        this.tableName = tableName;
        this.columns = columns;
        this.primaryKey = primaryKey;
    }

    private String convertMapColumnsToString(Map<String, String> columns) {
        return columns.entrySet().stream()
                .map((Map.Entry entry) -> String.format("%s %s", entry.getKey(), entry.getValue()))
                .collect(Collectors.joining(", "));
    }

    public void create() {
        String sqlRequestCreate = String.format("create table if not exists `%s`(%s);",
                this.tableName,
                convertMapColumnsToString(columns) + ((this.primaryKey != null) ?
                        String.format(" ,primary key(%s)", primaryKey) : ""));
        String sqlRequestDrop = String.format("drop table if exists `%s`;", tableName);
        DBConnector.getInstance().executeRequest(sqlRequestDrop);
        DBConnector.getInstance().executeRequest(sqlRequestCreate);
    }

    public void print() {
        System.out.println(tableName);
        DBConnector.getInstance().printQuery(String.format("select * from `%s`", tableName));
    }

    public boolean fill(Map<String, Object> columns) {
        int index = 1;
        StringBuilder val = new StringBuilder();
        String param = columns.entrySet().stream()
                .map((Map.Entry entry) -> String.format("%s", entry.getKey()))
                .collect(Collectors.joining(","));
        val.append("?");
        for (int i = 0; i < columns.size() - 1; i++) {
            val.append(",?");
        }
        String sqlFill = String.format("insert into `%s`(%s) values (%s);", this.tableName, param, val);
        PreparedStatement preparedStatement = DBConnector.getInstance().prepareStatement(sqlFill);
        try {
            for (Object value : columns.values()) {
                preparedStatement.setObject(index, value);
                index++;
            }
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }

    }

}

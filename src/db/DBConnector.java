/*
 * Copyright 2023.
 * Student's project.
 * Author: Polyanskaya Victory.
 */

package db;

import settings.PropertiesReader;
import java.sql.*;
import java.util.Map;

public class DBConnector {
    private Connection connection = null;
    private final Map<String, String> settings = new PropertiesReader().read();
    private static DBConnector instance;
    public static synchronized DBConnector getInstance() {
        if (instance == null) {
            instance = new DBConnector();
        }
        return instance;
    }

    public void connect() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(settings.get("url") + "/" + settings.get("db_name"),
                        settings.get("username"), settings.get("password"));
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

    }

    public Statement getStatement() {
        if (connection != null) {
            try {
                return connection.createStatement();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Statement getStatementUpdatable() {
        if (connection != null) {
            try {
                return connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void printQuery(String request) {
        Statement statement = getInstance().getStatement();
        if (statement != null) {

            try {
                ResultSet set = statement.executeQuery(request);
                ResultSetMetaData rsmd = set.getMetaData();
                int columns = rsmd.getColumnCount();
                for (int i = 1; i <= columns; i++) {
                    if (i > 1) System.out.print(" | ");
                    System.out.printf("%1$-15s", rsmd.getColumnLabel(i));
                }
                System.out.println();
                while (set.next()) {

                    for (int i = 1; i <= columns; i++) {
                        if (i > 1) System.out.print(" | ");
                        System.out.printf("%1$-15s", set.getString(i));
                    }
                    System.out.println();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void executeRequest(String request) {
        Statement statement = getInstance().getStatement();
        if (statement != null) {
            try {
                statement.execute(request);
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public PreparedStatement prepareStatement(String request) {
        if (connection != null) {
            try {
                return connection.prepareStatement(request);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    public void close() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}

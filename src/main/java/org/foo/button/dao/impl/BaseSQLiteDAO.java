package org.foo.button.dao.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by phil on 2/15/16.
 */
public class BaseSQLiteDAO {
    private Connection conn = null;

    public BaseSQLiteDAO() throws SQLException {
        this(DriverManager.getConnection(String.format("jdbc:sqlite:%s", ".dash.db")));
    }

    public BaseSQLiteDAO(Connection dbConnection) throws SQLException {
        conn = dbConnection;
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
}

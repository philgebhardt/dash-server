package org.foo.util;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by phil on 2/15/16.
 */
public class SQLiteDBTest {

    private Connection conn;

    public void setupDB() throws IOException, SQLException {
        conn = DriverManager.getConnection(String.format("jdbc:sqlite:%s", getDBFile().getAbsolutePath()));
        Statement stmt = conn.createStatement();
        stmt.executeUpdate(FileUtils.readFileToString(new File("src/main/resources/sql/sqlite3/button/button_ddl.sql")));
    }

    public void teardownDB() throws IOException, SQLException {
        conn.close();
        getDBFile().delete();
    }

    public File getDBFile() {
        return new File("target",this.getClass().getName().toString());
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
}
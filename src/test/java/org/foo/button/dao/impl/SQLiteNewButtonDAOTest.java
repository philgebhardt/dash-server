package org.foo.button.dao.impl;

import org.apache.commons.io.FileUtils;
import org.foo.button.dao.ButtonDAO;
import org.foo.button.dao.ButtonEventDAO;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by phil on 3/6/16.
 */
public class SQLiteNewButtonDAOTest {
    private static final String ID_TEST000 = "TEST000";
    ButtonDAO dao;
    @BeforeMethod
    public void setup() throws SQLException, IOException {
        Connection conn = DriverManager.getConnection(String.format("jdbc:sqlite:%s", "target/SQLiteNewButtonDAOTest.db"));
        Statement stmt = conn.createStatement();
        stmt.executeUpdate(FileUtils.readFileToString(new File("src/main/resources/sql/sqlite3/button/button_ddl.sql")));
        dao = new SQLiteButtonDAO(conn);
        ((SQLiteButtonEventDAO)dao).setTableName("new_button");
    }

    @AfterMethod
    public void teardown() {
        new File("target/SQLiteNewButtonDAOTest.db").delete();
    }

}

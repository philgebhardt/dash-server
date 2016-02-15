package org.foo.button.dao.impl;

import org.apache.commons.io.FileUtils;
import org.foo.button.model.Button;
import org.foo.button.dao.ButtonDAO;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by phil on 2/14/16.
 */
public class SQLiteButtonDAOTest {

    private static final String ID_TEST000 = "TEST000";
    private static final String NAME_TEST000 = "Test Zero";
    ButtonDAO dao;
    @BeforeMethod
    public void setup() throws SQLException, IOException {
        Connection conn = DriverManager.getConnection(String.format("jdbc:sqlite:%s", "target/SQLiteButtonDAOTest.db"));
        Statement stmt = conn.createStatement();
        stmt.executeUpdate(FileUtils.readFileToString(new File("src/main/resources/sql/sqlite3/button/button_ddl.sql")));
        dao = new SQLiteButtonDAO(conn);
    }

    @AfterMethod
    public void teardown() {
        new File("target/SQLiteButtonDAOTest.db").delete();
    }

    @Test
    public void testSave() throws Exception {
        int count = dao.findAll().size();
        _saveNow();
        assertThat(dao.findAll().size()).isEqualTo(count+1);
    }

    private void _saveNow() throws Exception {
        _save(ID_TEST000, NAME_TEST000, new Date());
    }

    private void _saveFromNow(Long diff) throws Exception {
        _save(ID_TEST000, NAME_TEST000, TimeUtils.dateFromNow(diff));
    }
    
    private void _save(String id, String name, Date dateCreated) throws Exception {
        Button button = new Button();
        button.setId(id);
        button.setName(name);
//        button.setDateCreated(dateCreated);
        dao.save(button);
    }

//    @Test
//    public void findRange() throws Exception {
//        _saveNow();
//        _saveNow();
//        _saveFromNow(TimeUtils.A_MINUTE_AGO);
//        _saveFromNow(TimeUtils.A_MINUTE_AGO);
//        _saveFromNow(TimeUtils.A_WEEK_AGO);
//
//        assertThat(dao.findAll(TimeUtils.minutesAgo(2), TimeUtils.aMinuteAgo()))
//                .hasSize(2);
//
//        assertThat(dao.findAll(null, TimeUtils.aMinuteAgo()))
//                .hasSize(3);
//
//        assertThat(dao.findAll(null, TimeUtils.now()))
//                .hasSize(5);
//
//        assertThat(dao.findAll(TimeUtils.minutesAgo(5), null))
//                .hasSize(4);
//
//        assertThat(dao.findAll(null, null))
//                .isNotNull();
//
//    }
}

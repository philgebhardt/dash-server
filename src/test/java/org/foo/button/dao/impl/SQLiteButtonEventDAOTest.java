package org.foo.button.dao.impl;

import static org.assertj.core.api.Assertions.*;

import org.apache.commons.io.FileUtils;
import org.foo.button.dao.ButtonEventDAO;
import org.foo.button.model.ButtonEvent;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.Date;

/**
 * Created by phil on 2/14/16.
 */
public class SQLiteButtonEventDAOTest {

    private static final String ID_TEST000 = "TEST000";
    ButtonEventDAO dao;
    @BeforeMethod
    public void setup() throws SQLException, IOException {
        Connection conn = DriverManager.getConnection(String.format("jdbc:sqlite:%s", "target/SQLiteButtonEventDAOTests.db"));
        Statement stmt = conn.createStatement();
        stmt.executeUpdate(FileUtils.readFileToString(new File("src/main/resources/sql/sqlite3/button/button_ddl.sql")));
        dao = new SQLiteButtonEventDAO(conn);
    }

    @AfterMethod
    public void teardown() {
        new File("target/SQLiteButtonEventDAOTests.db").delete();
    }

    @Test
    public void testSave() throws Exception {
        int count = dao.findAll().size();
        _saveNow();
        assertThat(dao.findAll().size()).isEqualTo(count+1);
    }

    private void _saveNow() throws Exception {
        _save(ID_TEST000, new Date());
    }

    private void _saveFromNow(Long diff) throws Exception {
        _save(ID_TEST000, TimeUtils.dateFromNow(diff));
    }
    private void _save(String id, Date date) throws Exception {
        ButtonEvent event = new ButtonEvent();
        event.setId(id);
        event.setDtmOccured(date);
        dao.save(event);
    }

    @Test
    public void testFindAllById() throws Exception {
        _saveNow();
        _saveNow();
        _saveNow();

        assertThat(dao.findById(ID_TEST000).size()).isEqualTo(3);
    }

    @Test
    public void findRange() throws Exception {
        _saveNow();
        _saveNow();
        _saveFromNow(TimeUtils.A_MINUTE_AGO);
        _saveFromNow(TimeUtils.A_MINUTE_AGO);
        _saveFromNow(TimeUtils.A_WEEK_AGO);

        assertThat(dao.findAll(TimeUtils.minutesAgo(2), TimeUtils.aMinuteAgo()))
                .hasSize(2);

        assertThat(dao.findAll(null, TimeUtils.aMinuteAgo()))
                .hasSize(3);

        assertThat(dao.findAll(null, TimeUtils.now()))
                .hasSize(5);

        assertThat(dao.findAll(TimeUtils.minutesAgo(5), null))
                .hasSize(4);

        assertThat(dao.findAll(null, null))
                .isNotNull();

    }

    @Test
    public void testIdRange() throws Exception {
        _saveNow();
        _saveNow();
        _saveFromNow(TimeUtils.A_MINUTE_AGO);
        _saveFromNow(TimeUtils.A_MINUTE_AGO);
        _saveFromNow(TimeUtils.A_WEEK_AGO);

        assertThat(dao.findById(ID_TEST000, TimeUtils.minutesAgo(2), TimeUtils.aMinuteAgo()))
                .hasSize(2);

        assertThat(dao.findById(ID_TEST000, null, TimeUtils.aMinuteAgo()))
                .hasSize(3);

        assertThat(dao.findById(ID_TEST000, null, TimeUtils.now()))
                .hasSize(5);

        assertThat(dao.findById(ID_TEST000, TimeUtils.minutesAgo(5), null))
                .hasSize(4);

        assertThat(dao.findById(ID_TEST000, null, null))
                .isNotNull();

    }
}

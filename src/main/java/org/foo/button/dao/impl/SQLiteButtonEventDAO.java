package org.foo.button.dao.impl;

import org.foo.button.dao.ButtonEventDAO;
import org.foo.button.model.ButtonEvent;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * Created by phil on 2/14/16.
 */
public class SQLiteButtonEventDAO implements ButtonEventDAO {

    private Connection conn = null;

    public SQLiteButtonEventDAO() throws SQLException {
        this(DriverManager.getConnection(String.format("jdbc:sqlite:%s", ".events.db")));
    }

    public SQLiteButtonEventDAO(Connection dbConnection) throws SQLException {
        conn = dbConnection;
    }

    public void save(ButtonEvent event) throws Exception {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO button_event (id,dttm_occurred) VALUES(?,?)");
        stmt.setString(1, event.getId());
        stmt.setDate(2, new java.sql.Date(event.getDtmOccured().getTime()));
        stmt.executeUpdate();
    }

    public Collection<ButtonEvent> findDateRange(Date from, Date till) throws Exception {
        Collection<ButtonEvent> events = new ArrayList<>();
        if(from==null && till==null) return events;

        boolean fromOnly=till==null;
        boolean tillOnly=from==null;
        PreparedStatement stmt;
        if(fromOnly) {
            stmt = conn.prepareStatement("SELECT * FROM button_event WHERE dttm_occurred >= ?");
            stmt.setDate(1, new java.sql.Date(from.getTime()));
        } else if (tillOnly) {
            stmt = conn.prepareStatement("SELECT * FROM button_event WHERE dttm_occurred <= ?");
            stmt.setDate(1, new java.sql.Date(till.getTime()));
        } else {
            stmt = conn.prepareStatement("SELECT * FROM button_event WHERE dttm_occurred BETWEEN ? AND ?");
            stmt.setDate(1, new java.sql.Date(from.getTime()));
            stmt.setDate(2, new java.sql.Date(till.getTime()));
        }
        ResultSet resultSet = stmt.executeQuery();
        return resultToCollection(resultSet);
    }

    private Collection<ButtonEvent> resultToCollection(ResultSet resultSet) throws SQLException {
        Collection<ButtonEvent> events = new ArrayList<ButtonEvent>();
        while (resultSet.next()) {
            ButtonEvent event = new ButtonEvent();
            event.setId(resultSet.getString("id"));
            event.setDtmOccured(resultSet.getDate("dttm_occurred"));
            events.add(event);
        }
        return events;
    }

    public Collection<ButtonEvent> findAllById(String id) throws Exception {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM button_event WHERE id = ?");
        Collection<ButtonEvent> events = new ArrayList<ButtonEvent>();
        stmt.setString(1, id);
        ResultSet resultSet = stmt.executeQuery();
        return resultToCollection(resultSet);
    }

    public Collection<ButtonEvent> findAll() throws Exception {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM button_event");
        Collection<ButtonEvent> events = new ArrayList<ButtonEvent>();
        ResultSet resultSet = stmt.executeQuery();
        while (resultSet.next()) {
            ButtonEvent event = new ButtonEvent();
            event.setId(resultSet.getString("id"));
            event.setDtmOccured(resultSet.getDate("dttm_occurred"));
            events.add(event);
        }
        return events;
    }
}

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
public class SQLiteButtonEventDAO extends BaseSQLiteDAO implements ButtonEventDAO {

    public SQLiteButtonEventDAO() throws SQLException {
        super();
    }

    public SQLiteButtonEventDAO(Connection conn) throws SQLException {
        super(conn);
    }

    public void save(ButtonEvent event) throws Exception {
        PreparedStatement stmt = getConn().prepareStatement("INSERT INTO "+getTableName()+" (id,dttm_occurred) VALUES(?,?)");
        stmt.setString(1, event.getId());
        stmt.setDate(2, new java.sql.Date(event.getDtmOccured().getTime()));
        stmt.executeUpdate();
    }

    public Collection<ButtonEvent> findAll(Date from, Date till) throws Exception {
        PreparedStatement stmt;

        if(from==null && till==null) {
            stmt = getConn().prepareStatement("SELECT * FROM "+getTableName()+"");
        } else if(till==null) {
            stmt = getConn().prepareStatement("SELECT * FROM "+getTableName()+" WHERE dttm_occurred >= ?");
            stmt.setDate(1, new java.sql.Date(from.getTime()));
        } else if (from==null) {
            stmt = getConn().prepareStatement("SELECT * FROM "+getTableName()+" WHERE dttm_occurred <= ?");
            stmt.setDate(1, new java.sql.Date(till.getTime()));
        } else {
            stmt = getConn().prepareStatement("SELECT * FROM "+getTableName()+" WHERE dttm_occurred BETWEEN ? AND ?");
            stmt.setDate(1, new java.sql.Date(from.getTime()));
            stmt.setDate(2, new java.sql.Date(till.getTime()));
        }
        ResultSet resultSet = stmt.executeQuery();
        return resultToCollection(resultSet);
    }

    public Collection<ButtonEvent> findById(String id) throws Exception {
        return findById(id, null, null);
    }

    @Override
    public Collection<ButtonEvent> findById(String id, Date from, Date till) throws Exception {
        PreparedStatement stmt;

        if (from == null && till == null) {
            stmt = getConn().prepareStatement("SELECT * FROM " + getTableName() + " WHERE id = ?");
            stmt.setString(1, id);
        } else if (till == null) {
            stmt = getConn().prepareStatement("SELECT * FROM " + getTableName() + " WHERE id = ? AND dttm_occurred >= ?");
            stmt.setString(1, id);
            stmt.setDate(2, new java.sql.Date(from.getTime()));
        } else if (from == null) {
            stmt = getConn().prepareStatement("SELECT * FROM " + getTableName() + " WHERE id = ? AND dttm_occurred <= ?");
            stmt.setString(1, id);
            stmt.setDate(2, new java.sql.Date(till.getTime()));
        } else {
            stmt = getConn().prepareStatement("SELECT * FROM " + getTableName() + " WHERE id = ? AND dttm_occurred BETWEEN ? AND ?");
            stmt.setString(1, id);
            stmt.setDate(2, new java.sql.Date(from.getTime()));
            stmt.setDate(3, new java.sql.Date(till.getTime()));
        }
        ResultSet resultSet = stmt.executeQuery();
        return resultToCollection(resultSet);
    }

    @Override
    public Collection<ButtonEvent> findAll() throws Exception {
        return findAll(null, null);
    }

    private Collection<ButtonEvent> resultToCollection(ResultSet resultSet) throws SQLException {
        Collection<ButtonEvent> events = new ArrayList<>();
        while (resultSet.next()) {
            ButtonEvent event = new ButtonEvent();
            event.setId(resultSet.getString("id"));
            event.setDtmOccured(resultSet.getDate("dttm_occurred"));
            events.add(event);
        }
        return events;
    }
}

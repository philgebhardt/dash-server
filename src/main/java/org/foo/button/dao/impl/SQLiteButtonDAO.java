package org.foo.button.dao.impl;

import org.foo.button.model.Button;
import org.foo.button.dao.ButtonDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Created by phil on 2/15/16.
 */
public class SQLiteButtonDAO extends BaseSQLiteDAO implements ButtonDAO {

    public SQLiteButtonDAO() throws SQLException {
        super();
    }

    public SQLiteButtonDAO(Connection conn) throws SQLException {
        super(conn);
    }

    @Override
    public Button findById(String id) throws SQLException {
        PreparedStatement stmt = getConn().prepareStatement("SELECT * FROM "+getTableName()+" WHERE id = ?");
        stmt.setString(1, id);
        ResultSet resultSet = stmt.executeQuery();
        Button button;
        try {
            button = new Button();
            button.setId(resultSet.getString("id"));
            button.setName(resultSet.getString("name"));
        } catch(SQLException e) {
            button=null;
        }
        return button;
    }

    @Override
    public Collection<Button> findByName(String name) throws SQLException {
        Collection<Button> buttons = new ArrayList<>();
        PreparedStatement stmt = getConn().prepareStatement("SELECT * FROM "+getTableName()+" WHERE name = ?");
        stmt.setString(2, name);
        ResultSet resultSet = stmt.executeQuery();
        while(resultSet.next()) {
            Button button = new Button();
            button.setId(resultSet.getString("id"));
            button.setName(resultSet.getString("name"));
            buttons.add(button);
        }
        return buttons;
    }

    @Override
    public void save(Button button) throws SQLException {
        boolean buttonExists = findById(button.getId())!=null;
        if(buttonExists) {
            PreparedStatement stmt = getConn().prepareStatement("UPDATE "+getTableName()+" SET id = ?, name = ? WHERE id = ?");
            stmt.setString(1, button.getId());
            stmt.setString(2, button.getName());
            stmt.setString(3, button.getId());
            stmt.executeUpdate();
        } else {
            PreparedStatement stmt = getConn().prepareStatement("INSERT INTO "+getTableName()+" (id,name) VALUES(?,?)");
            stmt.setString(1, button.getId());
            stmt.setString(2, button.getName());
            stmt.executeUpdate();
        }
    }

    @Override
    public Collection<Button> findAll() throws SQLException {
        Collection<Button> buttons = new ArrayList<>();
        PreparedStatement stmt = getConn().prepareStatement("SELECT * FROM "+getTableName());
        ResultSet resultSet = stmt.executeQuery();
        while(resultSet.next()) {
            Button button = new Button();
            button.setId(resultSet.getString("id"));
            button.setName(resultSet.getString("name"));
            buttons.add(button);
        }
        return buttons;
    }

    @Override
    public void saveAll(Collection<Button> buttons) {
        buttons.forEach((button) -> {
            try {
                save(button);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void delete(Button button) throws SQLException {
        PreparedStatement stmt = getConn().prepareStatement("DELETE FROM "+getTableName()+" WHERE id =?");
        stmt.setString(1, button.getId());
        stmt.executeUpdate();
    }

    @Override
    public void deleteAll(Collection<Button> buttons) throws SQLException {
        PreparedStatement stmt = getConn().prepareStatement("DELETE FROM "+getTableName()+" WHERE "+ _idIn(buttons));
        Iterator<Button> iter = buttons.iterator();
        int i = 1;
        while(iter.hasNext()) {
            stmt.setString(i++, iter.next().getId());
        }
        stmt.executeUpdate();
    }

    private String _idIn(Collection<Button> buttons) {
        StringBuilder placeholders = new StringBuilder();
        for (int i = 0; i < buttons.size(); i++) {
            if (i != 0)
                placeholders.append(", ");
            placeholders.append("?");
        }
        return "id IN (" + placeholders.toString() + ")";
    }
}

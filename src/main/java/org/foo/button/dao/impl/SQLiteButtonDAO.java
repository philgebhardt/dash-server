package org.foo.button.dao.impl;

import org.foo.button.model.Button;
import org.foo.button.dao.ButtonDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

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
        PreparedStatement stmt = getConn().prepareStatement("SELECT * FROM button WHERE id = ?");
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
        PreparedStatement stmt = getConn().prepareStatement("SELECT * FROM button WHERE name = ?");
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
            PreparedStatement stmt = getConn().prepareStatement("UPDATE button SET id = ?, name = ? WHERE id = ?");
            stmt.setString(1, button.getId());
            stmt.setString(2, button.getName());
            stmt.executeUpdate();
        } else {
            PreparedStatement stmt = getConn().prepareStatement("INSERT INTO button (id,name) VALUES(?,?)");
            stmt.setString(1, button.getId());
            stmt.setString(2, button.getName());
            stmt.executeUpdate();
        }
    }

    @Override
    public Collection<Button> findAll() throws SQLException {
        Collection<Button> buttons = new ArrayList<>();
        PreparedStatement stmt = getConn().prepareStatement("SELECT * FROM button");
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
}

package org.foo.button.dao;

import org.foo.button.model.Button;

import java.sql.SQLException;
import java.util.Collection;

/**
 * Created by phil on 2/15/16.
 */
public interface ButtonDAO {
    public Button findById(String id) throws SQLException;
    public Collection<Button> findByName(String id) throws SQLException;
    public void save(Button button) throws SQLException;
    public Collection<Button> findAll() throws SQLException;
    public void saveAll(Collection<Button> buttons);

    public void delete(Button button) throws SQLException;
    public void deleteAll(Collection<Button> buttons) throws SQLException;
}

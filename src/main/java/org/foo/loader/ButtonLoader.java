package org.foo.loader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.foo.button.model.Button;
import org.foo.button.dao.ButtonDAO;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

/**
 * Created by phil on 2/15/16.
 */
public class ButtonLoader {

    private final ObjectMapper mapper;
    private final ButtonDAO dao;

    public ButtonLoader(ButtonDAO dao) {
        this.dao = dao;
        this.mapper = new ObjectMapper();
    }

    public void load(Collection<Button> buttons) throws SQLException {
        dao.saveAll(buttons);
    }

    public void load(File file) throws IOException {
        dao.saveAll(mapper.readValue(file, new TypeReference<Collection<Button>>(){}));
    }
}

package org.foo.button.dao.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.foo.button.dao.ButtonEventDAO;
import org.foo.button.model.ButtonEvent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * Created by phil on 2/13/16.
 */
public class JsonButtonEventDAO implements ButtonEventDAO {

    private File dbFile;
    private Collection<ButtonEvent> events;
    private ObjectMapper mapper;

    public JsonButtonEventDAO() {
        this(new File(".events.json"));
    }

    public JsonButtonEventDAO(File dbFile) {
        setDbFile(dbFile);
        setMapper(new ObjectMapper());
        _load();
    }

    private void _load() {
        try {
            events = getMapper().readValue(dbFile,
                    new TypeReference<Collection<ButtonEvent>>() {/*ignore*/});
        } catch (IOException e) {
            events = new ArrayList<ButtonEvent>();
        }
    }

    public void save(ButtonEvent event) throws Exception {
        events.add(event);
        _save();
    }

    private void _save() throws IOException {
        getMapper().writeValue(dbFile, events);
    }

    public Collection<ButtonEvent> findById(String id) throws Exception {
        return null;
    }

    public Collection<ButtonEvent> findDateRange(Date from, Date till) throws Exception {
        return null;
    }

    public Collection<ButtonEvent> findAll() throws Exception {
        return null;
    }

    public Collection<ButtonEvent> findAllById(String id) throws Exception {
        return null;
    }

    public File getDbFile() {
        return dbFile;
    }

    public void setDbFile(File dbFile) {
        this.dbFile = dbFile;
    }

    public ObjectMapper getMapper() {
        return mapper;
    }

    public void setMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public void setEvents(Collection<ButtonEvent> events) {
        this.events = events;
    }
}

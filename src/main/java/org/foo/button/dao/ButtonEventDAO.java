package org.foo.button.dao;

import org.foo.button.model.ButtonEvent;

import java.util.Collection;
import java.util.Date;

/**
 * Created by phil on 2/13/16.
 */
public interface ButtonEventDAO {
    public void save(ButtonEvent event) throws Exception;
    public Collection<ButtonEvent> findById(String id) throws Exception;
    public Collection<ButtonEvent> findDateRange(Date from, Date till) throws Exception;
    public Collection<ButtonEvent> findAll() throws Exception;
}

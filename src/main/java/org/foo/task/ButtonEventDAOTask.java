package org.foo.task;

import org.foo.button.dao.ButtonEventDAO;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by phil on 2/14/16.
 */
public abstract class ButtonEventDAOTask implements Runnable {

    private ButtonEventDAO buttonEventDAO;

    public ButtonEventDAOTask(ButtonEventDAO dao) {
        this.buttonEventDAO = dao;
    }

    public ButtonEventDAO getButtonEventDAO() {
        return buttonEventDAO;
    }

    public void setButtonEventDAO(ButtonEventDAO buttonEventDAO) {
        this.buttonEventDAO = buttonEventDAO;
    }
}

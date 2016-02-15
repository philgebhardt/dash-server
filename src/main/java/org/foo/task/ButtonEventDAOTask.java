package org.foo.task;

import org.foo.button.dao.ButtonEventDAO;
import org.foo.button.dao.ButtonDAO;

/**
 * Created by phil on 2/14/16.
 */
public abstract class ButtonEventDAOTask implements Runnable {

    private ButtonEventDAO buttonEventDAO;

    private ButtonDAO buttonDAO;

    public ButtonEventDAOTask(ButtonEventDAO buttonEventDAO, ButtonDAO buttonDAO) {
        this.buttonEventDAO = buttonEventDAO;
        this.buttonDAO = buttonDAO;
    }

    public ButtonEventDAO getButtonEventDAO() {
        return buttonEventDAO;
    }

    public void setButtonDAO(ButtonDAO buttonDAO) {
        this.buttonDAO = buttonDAO;
    }

    public ButtonDAO getButtonDAO() {
        return buttonDAO;
    }

    public void setButtonEventDAO(ButtonEventDAO buttonEventDAO) {
        this.buttonEventDAO = buttonEventDAO;
    }
}

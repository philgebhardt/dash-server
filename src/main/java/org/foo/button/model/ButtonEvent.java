package org.foo.button.model;

import java.util.Date;

/**
 * Created by phil on 2/13/16.
 */
public class ButtonEvent {
    private String id;
    private Date dtmOccured;

    public Date getDtmOccured() {
        return dtmOccured;
    }

    public void setDtmOccured(Date dtm_occured) {
        this.dtmOccured = dtm_occured;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

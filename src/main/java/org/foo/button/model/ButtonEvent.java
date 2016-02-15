package org.foo.button.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.foo.util.JsonDateSerializer;

import java.util.Date;

/**
 * Created by phil on 2/13/16.
 */
public class ButtonEvent {
    private String id;

    @JsonSerialize(using=JsonDateSerializer.class)
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

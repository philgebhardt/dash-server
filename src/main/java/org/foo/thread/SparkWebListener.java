package org.foo.thread;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.foo.button.dao.ButtonEventDAO;
import org.foo.button.model.ButtonEvent;
import org.foo.task.ButtonEventDAOTask;
import org.foo.util.JsonDateDeserializer;
import spark.Request;
import spark.Response;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import static org.foo.util.JsonDateDeserializer.*;
import static spark.Spark.get;

/**
 * Created by phil on 2/14/16.
 */
public class SparkWebListener extends ButtonEventDAOTask {

    private ObjectMapper mapper;

    public SparkWebListener(ButtonEventDAO dao) {
        super(dao);
        mapper = new ObjectMapper();
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm a z"));
    }

    @Override
    public void run() {

        get("/events", (req, res) -> {
            DateParams dateParams = _getDateParams(req);
            Collection<ButtonEvent> events = getButtonEventDAO().findAll(dateParams.FROM, dateParams.TILL);
            _prepareResponse(req, res);
            return mapper.writeValueAsString(events);
        });

        get("/events/", (req, res) -> {
            DateParams dateParams = _getDateParams(req);
            Collection<ButtonEvent> events = getButtonEventDAO().findAll(dateParams.FROM, dateParams.TILL);
            _prepareResponse(req, res);
            return mapper.writeValueAsString(events);
        });

        get("/events/:id", (req, res) -> {
            String id = req.params("id");
            DateParams dateParams = _getDateParams(req);
            Collection<ButtonEvent> events = getButtonEventDAO().findById(id, dateParams.FROM, dateParams.TILL);
            _prepareResponse(req, res);
            return mapper.writeValueAsString(events);
        });
    }

    private DateParams _getDateParams(Request req) throws ParseException {
        String fromStr = req.queryParams("from");
        String tillStr = req.queryParams("till");
        Date from = (fromStr!=null) ? stringToDate(fromStr) : null;
        Date till = (tillStr!=null) ? stringToDate(tillStr) : null;
        return new DateParams(from,till);
    }

    private void _prepareResponse(Request req, Response res) {
        res.header("Content-Type", "application/json");

    }

    private class DateParams {
        public final Date FROM;
        public final Date TILL;

        public DateParams(Date from, Date till) {
            FROM=from;
            TILL=till;
        }
    }
}

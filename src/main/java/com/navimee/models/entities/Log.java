package com.navimee.models.entities;

import com.navimee.firestore.Paths;
import com.navimee.logger.LogEnum;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

public class Log implements Comparable<Log>, Entity {

    private LogEnum type;
    private Date time;
    private String reference;
    private Integer count;
    private String id;

    public Log() {
    }

    public Log(LogEnum type, String reference, Integer count) {
        DateTimeZone zone = DateTimeZone.forID("Europe/Warsaw");
        LocalDateTime warsaw = LocalDateTime.now(zone);

        this.type = type;
        this.reference = Paths.get(reference);
        this.count = count;
        this.time = warsaw.toDate();
    }

    public Log(LogEnum type, Exception exception) {
        StringWriter sw = new StringWriter();
        exception.printStackTrace(new PrintWriter(sw));

        DateTimeZone zone = DateTimeZone.forID("Europe/Warsaw");
        LocalDateTime warsaw = LocalDateTime.now(zone);

        exception.printStackTrace();

        this.type = type;
        this.reference = sw.toString();
        this.time = warsaw.toDate();
        this.count = null;
    }

    public Log(LogEnum type, String reference) {
        this(type, reference, null);
    }

    public LogEnum getType() {
        return type;
    }

    public void setType(LogEnum type) {
        this.type = type;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    @Override
    public int compareTo(Log o) {
        return time.compareTo(o.time);
    }
}

package com.navimee.models.entities;

import com.navimee.firestore.FirebasePaths;
import com.navimee.logger.LogTypes;
import com.navimee.models.entities.contracts.Entity;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

public class Log implements Comparable<Log>, Entity {

    private LogTypes type;
    private Date time;
    private String reference;
    private Integer count;
    private String id;

    public Log() {
    }

    public Log(LogTypes type, String reference, Integer count) {
        DateTime warsaw = DateTime.now(DateTimeZone.UTC);

        this.type = type;
        this.reference = FirebasePaths.get(reference);
        this.count = count;
        this.time = warsaw.toDate();
    }

    public Log(LogTypes type, Exception exception) {
        StringWriter sw = new StringWriter();
        exception.printStackTrace(new PrintWriter(sw));

        DateTime warsaw = DateTime.now(DateTimeZone.UTC);

        exception.printStackTrace();

        this.type = type;
        this.reference = sw.toString();
        this.time = warsaw.toDate();
        this.count = null;
    }

    public Log(LogTypes type, String reference) {
        this(type, reference, null);
    }

    public LogTypes getType() {
        return type;
    }

    public void setType(LogTypes type) {
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

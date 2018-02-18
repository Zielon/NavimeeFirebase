package com.navimee.models.entities;

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
    private String id;
    private String country;

    public Log() {
    }

    public Log(LogTypes type, String format, Object... args) {
        DateTime warsaw = DateTime.now(DateTimeZone.UTC);

        this.type = type;
        this.reference = String.format(format, args).toUpperCase();
        this.time = warsaw.toDate();
        this.country = System.getenv().get("COUNTRY");
    }

    public Log(LogTypes type, Exception exception) {
        StringWriter sw = new StringWriter();
        exception.printStackTrace(new PrintWriter(sw));

        DateTime warsaw = DateTime.now(DateTimeZone.UTC);

        exception.printStackTrace();

        this.type = type;
        this.reference = sw.toString();
        this.time = warsaw.toDate();
        this.country = System.getenv().get("COUNTRY");
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}

package com.navimee.models.entities;

import com.navimee.logger.LogEnum;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;

import java.util.Date;

public class Log implements Comparable<Log>, Entity {

    public Log() {
    }

    public Log(LogEnum type, String collection, int count) {
        this.type = type;
        this.collection = collection;
        this.count = count;

        DateTimeZone zone = DateTimeZone.forID("Europe/Warsaw");
        LocalDateTime warsaw = LocalDateTime.now(zone);

        this.time = warsaw.toDate();
    }

    private LogEnum type;
    private Date time;
    private String collection;
    private int count;
    private String id;

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

    public String getCollection() {
        if (collection.contains("DOCUMENTS"))
            return collection.split("DOCUMENTS")[1];
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection.toUpperCase();
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getId() {
        return id;
    }

    @Override
    public String getReference() {
        return null;
    }

    @Override
    public void setReference(String reference) {
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int compareTo(Log o) {
        return time.compareTo(o.time);
    }
}

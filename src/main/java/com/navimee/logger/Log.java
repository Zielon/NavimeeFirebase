package com.navimee.logger;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

public class Log {

    public Log(){}

    public Log(LogEnum type, String collection, int count){
        this.type = type;
        this.collection = collection;
        this.count = count;

        DateTimeZone zone = DateTimeZone.forID("Europe/Warsaw");
        LocalDateTime warsaw = LocalDateTime.now(zone);
        DateTimeFormatter dtf = ISODateTimeFormat.dateTime();

        this.time = dtf.print(warsaw);
    }

    private LogEnum type;
    private String time;
    private String collection;
    private int count;
    private String id;

    public LogEnum getType() {
        return type;
    }

    public void setType(LogEnum type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public String getCollection() {
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

    public void setId(String id) {
        this.id = id;
    }
}

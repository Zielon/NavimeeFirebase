package com.navimee.tasks;

public class TasksFixedTimes {

    private static final int MINUTE = 60 * 1000;
    private static final int HOUR = 60 * MINUTE;

    public static final int EVENTS = HOUR * 24 + 10 * MINUTE;
    public static final int HOTSPOT = MINUTE * 15;
    public static final int NOTIFICATIONS = MINUTE;
    public static final int REMOVAL = MINUTE * 15;
}

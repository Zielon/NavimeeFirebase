package com.navimee.tasks;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class HotPlacesTask {

    @Scheduled(cron = "0 0 0/1 * * ?")
    public void addHotPlacesTask(){

    }
}

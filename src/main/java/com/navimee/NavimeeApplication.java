package com.navimee;

import com.navimee.contracts.services.NotificationsService;
import com.navimee.logger.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
@EnableScheduling
public class NavimeeApplication extends SpringBootServletInitializer {

    @Autowired
    NotificationsService notificationsService;

    @PostConstruct
    void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        notificationsService.listenForFeedback();
        Logger.isRunning = false;
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(NavimeeApplication.class);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(NavimeeApplication.class, args);
    }
}

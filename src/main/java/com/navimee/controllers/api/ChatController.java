package com.navimee.controllers.api;

import com.navimee.contracts.repositories.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Future;

@RestController
@RequestMapping(value = "api/chat")
public class ChatController {

    @Autowired
    ChatRepository chatRepository;

    @RequestMapping(value = "default", method = RequestMethod.POST)
    public Future<Void> chatDefaults() {
        return chatRepository.setDefaultRooms();
    }
}

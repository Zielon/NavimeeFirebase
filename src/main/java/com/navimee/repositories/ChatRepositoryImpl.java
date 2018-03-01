package com.navimee.repositories;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.SetOptions;
import com.navimee.contracts.repositories.ChatRepository;
import com.navimee.firestore.PathBuilder;
import com.navimee.models.entities.chat.Room;
import com.navimee.staticData.NavimeeData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import static com.navimee.firestore.FirebasePaths.GROUP;
import static com.navimee.firestore.FirebasePaths.ROOM_DETAILS;

@Repository
public class ChatRepositoryImpl implements ChatRepository {

    @Autowired
    Firestore database;

    @Autowired
    ExecutorService executorService;

    @Override
    public CompletableFuture<Void> setDefaultRooms() {

        return CompletableFuture.runAsync(() -> {
            NavimeeData data = new NavimeeData();
            List<Room> chats = data.getChatDefault();

            chats.forEach(chat -> {
                database.document(new PathBuilder().add(GROUP).addCountry().add(chat.getId()).add(ROOM_DETAILS).build())
                        .set(chat, SetOptions.merge());

                if (chat.isAdvertisement()) {
                    chat.setId(chat.getId() + "_OGLOSZENIA");
                    chat.setName(chat.getName() + " ogłoszenia");
                    database.document(new PathBuilder().add(GROUP).addCountry().add(chat.getId()).add(ROOM_DETAILS).build())
                            .set(chat, SetOptions.merge());
                }
            });
        }, executorService);
    }
}
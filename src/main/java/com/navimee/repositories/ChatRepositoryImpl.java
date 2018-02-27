package com.navimee.repositories;

import com.google.cloud.firestore.Firestore;
import com.navimee.contracts.repositories.ChatRepository;
import com.navimee.contracts.repositories.places.CoordinatesRepository;
import com.navimee.firestore.PathBuilder;
import com.navimee.models.entities.chat.Room;
import com.navimee.models.entities.coordinates.City;
import com.navimee.staticData.NavimeeData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.navimee.firestore.FirebasePaths.GROUP;
import static com.navimee.firestore.FirebasePaths.ROOM_DETAILS;

@Repository
public class ChatRepositoryImpl implements ChatRepository {

    @Autowired
    Firestore database;

    @Autowired
    CoordinatesRepository coordinatesRepository;

    @Override
    public CompletableFuture<Void> setDefaultRooms() {
        return coordinatesRepository.getAvailableCities().thenAcceptAsync(cities -> {
            NavimeeData data = new NavimeeData();
            List<String> chats = data.getChatDefault();
            chats.addAll(cities.stream().map(City::getName).collect(Collectors.toList()));
            chats.forEach(chat -> {
                String name = chat.toLowerCase();
                name = name.substring(0, 1).toUpperCase() + name.substring(1);

                Room room = new Room();

                room.setAdmin("ADMIN_DRIVELY");
                room.setName(name);
                room.setEditable(false);

                database.document(new PathBuilder().add(GROUP).addCountry().add(chat.toUpperCase()).add(ROOM_DETAILS).build()).set(room);
            });
        });
    }
}

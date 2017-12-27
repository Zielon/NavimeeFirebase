package com.navimee.services;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.navimee.contracts.repositories.NotificationsRepository;
import com.navimee.contracts.repositories.UsersRepository;
import com.navimee.contracts.services.FcmService;
import com.navimee.contracts.services.NotificationsService;
import com.navimee.firestore.Paths;
import com.navimee.models.entities.Feedback;
import com.navimee.models.entities.Notification;
import com.navimee.models.entities.User;
import com.navimee.models.entities.contracts.FcmSendable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class NotificationsServiceImpl implements NotificationsService {

    @Autowired
    FirebaseDatabase firebaseDatabase;

    @Autowired
    NotificationsRepository notificationsRepository;

    @Autowired
    FcmService fcmService;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    @Qualifier("scheduledExecutor")
    ScheduledExecutorService scheduledExecutorService;

    @Override
    public Future sendDaySchedule() {
        List<Notification> notifications = notificationsRepository.getAvailable();
        return fcmService.send(notifications, notification -> {
            Map<String, Object> data = new HashMap<>();
            data.put("title", notification.getTitle());
            data.put("endTime", notification.getEndTime());
            data.put("lat", notification.getGeoPoint().getLatitude());
            data.put("lng", notification.getGeoPoint().getLongitude());
            return data;
        });
    }

    @Override
    public void listenForFeedback() {
        firebaseDatabase.getReference(Paths.FEEDBACK_COLLECTION).addChildEventListener(getChildEventListener());
    }

    private ChildEventListener getChildEventListener() {
        return new ChildEventListener() {
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Feedback feedback = dataSnapshot.getValue(Feedback.class);
                User user = usersRepository.getUser(feedback.getUserId());

                feedback.setUserId(user.getId());
                feedback.setToken(user.getToken());

                scheduledExecutorService.schedule(() -> {
                    List<FcmSendable> sendables = new ArrayList<>();
                    sendables.add(feedback);
                    fcmService.send(sendables, fcmSendable -> {
                        Map<String, Object> data = new HashMap<>();
                        data.put("address", feedback.getLocationName());
                        data.put("name", user.getName());
                        data.put("surname", user.getSurname());
                        return data;
                    });
                }, feedback.getDurationInSec(), TimeUnit.SECONDS);
            }

            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            public void onCancelled(DatabaseError databaseError) {
            }
        };
    }
}

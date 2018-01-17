package com.navimee.services;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.navimee.contracts.repositories.NotificationsRepository;
import com.navimee.contracts.repositories.UsersRepository;
import com.navimee.contracts.services.FcmService;
import com.navimee.contracts.services.NotificationsService;
import com.navimee.enums.NotificationType;
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
import java.util.function.Function;

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

    private Function<Notification, Map<String, Object>> getDataCreator(NotificationType type) {
        return notification -> {
            Map<String, Object> data = new HashMap<>();
            data.put("title", notification.getTitle());
            data.put("endTime", notification.getEndTime());
            data.put("lat", notification.getGeoPoint().getLatitude());
            data.put("lng", notification.getGeoPoint().getLongitude());
            data.put("rank", notification.getRank());
            data.put("type", type);
            return data;
        };
    }

    @Override
    public Future sendDaySchedule() {
        List<Notification> notifications = notificationsRepository.getAvailableNotifications();
        return fcmService.send(notifications, getDataCreator(NotificationType.SCHEDULED_EVENT));
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

                if(feedback.isSent()) return;

                int waitForFeedbackSend = 60 * 15; // in seconds

                feedback.setId(dataSnapshot.getKey());
                feedback.setUserId(user.getId());
                feedback.setToken(user.getToken());

                scheduledExecutorService.schedule(() -> {
                    List<FcmSendable> sendables = new ArrayList<>();
                    sendables.add(feedback);
                    fcmService.send(sendables, fcmSendable -> {
                        Map<String, Object> data = new HashMap<>();
                        data.put("id", feedback.getId());
                        data.put("locationName", feedback.getLocationName());
                        data.put("locationAddress", feedback.getLocationAddress());
                        data.put("name", user.getName() != null ? user.getName().split(" ")[0] : "No-name");
                        data.put("type", NotificationType.FEEDBACK);
                        return data;
                    });
                }, feedback.getDurationInSec() + waitForFeedbackSend, TimeUnit.SECONDS);
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
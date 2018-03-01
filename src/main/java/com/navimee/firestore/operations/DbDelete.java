package com.navimee.firestore.operations;

import com.google.cloud.firestore.*;
import com.navimee.asyncCollectors.Completable;
import com.navimee.logger.LogTypes;
import com.navimee.logger.Logger;
import com.navimee.models.entities.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@Component
public class DbDelete {

    @Autowired
    ExecutorService executorService;

    public void collection(CollectionReference collection, int deletedAll) {
        int batchSize = 3000;
        try {
            int deleted = 0;

            QuerySnapshot future = collection.limit(batchSize).get().get();
            List<QueryDocumentSnapshot> documents = future.getDocuments();
            List<Future<WriteResult>> tasks = new ArrayList<>();

            for (DocumentSnapshot document : documents) {
                tasks.add(document.getReference().delete());
                ++deleted;
            }

            // Wait for all tasks to finish.
            Completable.wait(executorService, tasks);

            if (deleted >= batchSize) {
                deletedAll += deleted;
                collection(collection, deletedAll);
            }

        } catch (Exception e) {
            Logger.LOG(new Log(LogTypes.EXCEPTION, e));
        }

        // Logger.LOG(new Log(LogTypes.DELETION, collection.getPath(), deletedAll));
    }

    public void document(DocumentReference document) {
        try {
            document.getCollections().forEach(collectionReference -> {
                collection(collectionReference, 1);
            });
            document.delete().get();
        } catch (Exception e) {
            Logger.LOG(new Log(LogTypes.EXCEPTION, e));
        }
    }

    public void document(Query query) {
        try {
            for (DocumentSnapshot documentSnapshot : query.get().get().getDocuments())
                document(documentSnapshot.getReference());
        } catch (Exception e) {
            Logger.LOG(new Log(LogTypes.EXCEPTION, e));
        }
    }
}

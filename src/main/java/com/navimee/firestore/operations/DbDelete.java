package com.navimee.firestore.operations;

import com.google.cloud.firestore.*;
import com.navimee.logger.LogEnum;
import com.navimee.logger.Logger;
import com.navimee.models.entities.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static com.navimee.asyncCollectors.CompletionCollector.waitForSingleFuture;

@Component
public class DbDelete {

    @Autowired
    ExecutorService executorService;

    public void collection(CollectionReference collection, int deletedAll) {
        int batchSize = 1000;
        try {
            int deleted = 0;

            QuerySnapshot future = collection.limit(batchSize).get().get();
            List<DocumentSnapshot> documents = future.getDocuments();
            List<Future<WriteResult>> tasks = new ArrayList<>();

            for (DocumentSnapshot document : documents) {
                tasks.add(document.getReference().delete());
                ++deleted;
            }

            // Wait for all tasks to finish.
            waitForSingleFuture(executorService, tasks);

            if (deleted >= batchSize) {
                deletedAll += deleted;
                collection(collection, deletedAll);
            }
        } catch (Exception e) {
            Logger.LOG(new Log(LogEnum.EXCEPTION, e));
        }

        // Logger.LOG(new Log(LogEnum.DELETION, collection.getPath(), deletedAll));
    }

    public void document(DocumentReference document) {
        try {
            for (CollectionReference collection : document.getCollections().get())
                collection(collection, 1);
            document.delete().get();
        } catch (Exception e) {
            Logger.LOG(new Log(LogEnum.EXCEPTION, e));
        }
    }

    public void document(Query query) {
        try {
            for (DocumentSnapshot documentSnapshot : query.get().get().getDocuments())
                document(documentSnapshot.getReference());
        } catch (Exception e) {
            Logger.LOG(new Log(LogEnum.EXCEPTION, e));
        }
    }
}

package com.navimee.firestore.operations;

import com.google.cloud.firestore.*;
import com.navimee.logger.Log;
import com.navimee.logger.LogEnum;
import com.navimee.logger.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import static com.navimee.asyncCollectors.CompletionCollector.waitForSingleFuture;

public class Delete extends Base {

    public static void collection(CollectionReference collection, int deletedAll) {
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
            System.err.println("Error deleting collection : " + e.getMessage());
        }

        Logger.LOG(new Log(LogEnum.DELETION, collection.getPath(), deletedAll));
    }

    public static void document(DocumentReference document) {
        try {
            for (CollectionReference collection : document.getCollections().get())
                collection(collection, 0);
            document.delete().get();
        } catch (Exception e) {
            System.err.println("Error deleting document : " + e.getMessage());
        }
    }

}

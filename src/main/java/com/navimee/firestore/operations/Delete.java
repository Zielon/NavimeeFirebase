package com.navimee.firestore.operations;

import com.google.cloud.firestore.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import static com.navimee.asyncCollectors.CompletionCollector.waitForSingle;

public class Delete extends BaseOperation {

    public static void collection(CollectionReference collection) {
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
            waitForSingle(tasks);

            if (deleted >= batchSize) {
                collection(collection);
            }
        } catch (Exception e) {
            System.err.println("Error deleting collection : " + e.getMessage());
        }
    }

    public static void document(DocumentReference document) {
        try {
            for (CollectionReference collection : document.getCollections().get())
                collection(collection);
            document.delete().get();
        } catch (Exception e) {
            System.err.println("Error deleting document : " + e.getMessage());
        }
    }

}

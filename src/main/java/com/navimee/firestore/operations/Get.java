package com.navimee.firestore.operations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Get extends BaseOperation {

    public static <T> List<T> fromDocument(DocumentReference documentReference, Class<T> type) {
        List<T> output = new ArrayList<>();
        try {
            for (CollectionReference collectionReference : documentReference.getCollections().get())
                output.addAll(getFromCollection(collectionReference, type, false));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return output;
    }

    public static <T> List<T> fromCollection(CollectionReference collectionReference, Class<T> type) {
        return getFromCollection(collectionReference, type, true);
    }

    public static <T> List<T> getFromCollection(CollectionReference collectionReference, Class<T> type, boolean logging) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JodaModule());
        List<T> output = new ArrayList<>();
        try {
            for (DocumentSnapshot document : collectionReference.get().get().getDocuments())
                output.add(mapper.convertValue(document.getData(), type));

            String LOG = String.format("ENTITIES %d RETRIEVED FROM -> %s | %s", output.size(), collectionReference.getPath().toUpperCase(), new Date());
            if (logging) System.out.println(LOG);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return output;
    }
}

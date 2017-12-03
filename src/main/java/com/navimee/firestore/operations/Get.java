package com.navimee.firestore.operations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.navimee.logger.Log;
import com.navimee.logger.LogEnum;
import com.navimee.logger.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Get extends Base {

    public static <T> List<T> fromDocument(DocumentReference documentReference, Class<T> type) {
        List<T> output = new ArrayList<>();
        try {
            for (CollectionReference collectionReference : documentReference.getCollections().get())
                output.addAll(fromCollection(collectionReference, type, true));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return output;
    }

    public static <T> List<T> fromCollection(CollectionReference collectionReference, Class<T> type) {
        return fromCollection(collectionReference, type, true);
    }

    public static <T> List<T> fromCollection(CollectionReference collectionReference, Class<T> type, boolean logging) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JodaModule());
        List<T> output = new ArrayList<>();
        try {
            for (DocumentSnapshot document : collectionReference.get().get().getDocuments())
                output.add(mapper.convertValue(document.getData(), type));

            if (logging)
                Logger.LOG(new Log(LogEnum.RETRIEVAL, collectionReference.getPath(), output.size()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return output;
    }
}

package com.navimee.firestore.operations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.navimee.firestore.Paths;
import com.navimee.logger.LogEnum;
import com.navimee.logger.Logger;
import com.navimee.models.entities.Entity;
import com.navimee.models.entities.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

@Component
public class Get {

    @Autowired
    ExecutorService executorService;

    public <T extends Entity> List<T> fromDocument(DocumentReference documentReference, Class<T> type) {
        List<T> output = new ArrayList<>();
        try {
            for (CollectionReference collectionReference : documentReference.getCollections().get())
                output.addAll(fromCollection(collectionReference, type, false));

        } catch (Exception e) {
            Logger.LOG(new Log(LogEnum.EXCEPTION, e));
        }

        return output;
    }

    public <T extends Entity> List<T> fromCollection(CollectionReference collectionReference, Class<T> type) {
        return fromCollection(collectionReference, type, true);
    }

    public <T extends Entity> List<T> fromCollection(CollectionReference collectionReference, Class<T> type, boolean logging) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JodaModule());
        List<T> output = new ArrayList<>();
        try {
            for (DocumentSnapshot document : collectionReference.get().get().getDocuments()) {
                T entity = mapper.convertValue(document.getData(), type);
                entity.setReference(Paths.get(collectionReference));
                output.add(entity);
            }
            if (logging)
                Logger.LOG(new Log(LogEnum.RETRIEVAL, collectionReference.getPath(), output.size()));
        } catch (Exception e) {
            Logger.LOG(new Log(LogEnum.EXCEPTION, e));
        }

        return output;
    }
}

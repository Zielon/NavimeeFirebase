package com.navimee.firestore.operations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Query;
import com.navimee.logger.LogTypes;
import com.navimee.logger.Logger;
import com.navimee.models.entities.Log;
import com.navimee.models.entities.contracts.Entity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DbGet {

    public <T extends Entity> List<T> fromDocument(DocumentReference documentReference, Class<T> type) {
        List<T> output = new ArrayList<>();
        try {
            for (CollectionReference collectionReference : documentReference.getCollections().get())
                output.addAll(fromCollection(collectionReference, type, false));

        } catch (Exception e) {
            Logger.LOG(new Log(LogTypes.EXCEPTION, e));
        }

        return output;
    }

    public <T extends Entity> T fromSingleDocument(DocumentReference documentReference, Class<T> type) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JodaModule());
        T entity = null;
        try {
            entity = mapper.convertValue(documentReference.get().get().getData(), type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entity;
    }

    public <T extends Entity> List<T> fromCollection(CollectionReference collectionReference, Class<T> type) {
        return fromCollection(collectionReference, type, true);
    }

    public <T extends Entity> List<T> fromCollection(Query query, Class<T> type) {
        List<T> output = new ArrayList<>();
        try {
            output = fromCollection(query.get().get().getDocuments(), type);
        } catch (Exception e) {
            Logger.LOG(new Log(LogTypes.EXCEPTION, e));
        }

        return output;
    }

    private <T extends Entity> List<T> fromCollection(CollectionReference collectionReference, Class<T> type, boolean logging) {
        List<T> output = new ArrayList<>();
        try {
            output = fromCollection(collectionReference.get().get().getDocuments(), type);
        } catch (Exception e) {
            Logger.LOG(new Log(LogTypes.EXCEPTION, e));
        }
        if (logging)
            Logger.LOG(new Log(LogTypes.RETRIEVAL, collectionReference.getPath(), output.size()));
        return output;
    }

    private <T extends Entity> List<T> fromCollection(List<DocumentSnapshot> documentSnapshots, Class<T> type) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JodaModule());
        List<T> output = new ArrayList<>();
        try {
            for (DocumentSnapshot document : documentSnapshots) {
                T entity = mapper.convertValue(document.getData(), type);
                output.add(entity);
            }
        } catch (Exception e) {
            Logger.LOG(new Log(LogTypes.EXCEPTION, e));
        }

        return output;
    }
}

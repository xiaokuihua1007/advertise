package org.classmatechen.sample.mongo;

import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.classmatechen.sample.config.AppInit;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.client.model.UpdateOneModel;

public class MongoStore {

    /**
     * @see AppInit#init
     */
    private static MongoTemplate template;

    public static void store(String collection, UpdateOneModel<Document> document) {
        store(collection, Arrays.asList(document));
    }

    public static void store(String collection, List<UpdateOneModel<Document>> documents) {
        template.getCollection(collection).bulkWrite(documents);
    }

    // public static <T> List<T> list(Class<T> cls) {
    //     return template.findAll(cls);
    // }

    public static <T> List<T> list(Class<T> cls, String collectionName) {
        return template.findAll(cls, collectionName);
    }
}

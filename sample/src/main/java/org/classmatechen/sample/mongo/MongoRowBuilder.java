package org.classmatechen.sample.mongo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.bson.Document;
import org.classmatechen.sample.config.AppInit;
import org.classmatechen.sample.mongo.remove.BlankRemove;
import org.classmatechen.sample.mongo.remove.EmptyRemove;
import org.classmatechen.sample.mongo.remove.NullRemove;
import org.classmatechen.sample.mongo.remove.ZoreRemove;
import org.springframework.data.mongodb.core.convert.MongoConverter;

import com.mongodb.BasicDBObject;
import com.mongodb.client.model.UpdateOneModel;
import com.mongodb.client.model.UpdateOptions;

public class MongoRowBuilder<T> {

    private static final String _id = "_id";
    private static final String _class = "_class";
    /**
     * @see AppInit#init
     */
    private static MongoConverter converter;
    private static final Remove REMOVE_NULL = new NullRemove();
    private static final int REMOVE_NULL_V = 1;
    private static final Remove REMOVE_ZORE = new ZoreRemove();
    private static final int REMOVE_ZORE_V = 2;
    private static final Remove REMOVE_BLANK = new BlankRemove();
    private static final int REMOVE_BLANK_V = 4;
    private static final Remove REMOVE_EMPTY = new EmptyRemove();
    private static final int REMOVE_EMPTY_V = 8;

    private final T data;
    private final Map<String, Object> append;
    private final Set<String> remove;
    private int action;

    public MongoRowBuilder() {

        this(null);
    }

    public MongoRowBuilder(T data) {

        this.data = data;
        this.append = new HashMap<>();
        this.action = 0;
        this.remove = new HashSet<>();
        removeClass();
    }

    public MongoRowBuilder<T> removeNull() {
        this.action = this.action | REMOVE_NULL_V;
        return this;
    }

    public MongoRowBuilder<T> removeZore() {
        this.action = this.action | REMOVE_ZORE_V;
        return this;
    }

    public MongoRowBuilder<T> removeBlank() {
        this.action = this.action | REMOVE_BLANK_V;
        return this;
    }

    public MongoRowBuilder<T> removeEmpty() {
        this.action = this.action | REMOVE_EMPTY_V;
        return this;
    }

    public MongoRowBuilder<T> removeClass() {
        return remove(_class);
    }

    public MongoRowBuilder<T> remove(String key) {
        this.remove.add(key);
        return this;
    }

    public MongoRowBuilder<T> id(Object value) {
        return append(_id, value);
    }

    public MongoRowBuilder<T> append(String key, Object value) {
        this.append.put(key, value);
        return this;
    }

    private void check() {

        // if (Objects.isNull(this.data)) {
        //     throw new RuntimeException("data can not be null");
        // }
        if (Objects.isNull(this.append.get(_id))) {
            throw new RuntimeException("id can not be null");
        }
    }

    public UpdateOneModel<Document> build() {

        Document appendDocument = new Document();
        Document unsetDocument = new Document();

        check();

        // init
        if (Objects.nonNull(data)) {

            BasicDBObject object = new BasicDBObject();
            converter.write(this.data, object);

            // remove
            List<Remove> actions = new ArrayList<>();
            if ((this.action & REMOVE_NULL_V) > 0) {
                actions.add(REMOVE_NULL);
            }
            if ((this.action & REMOVE_ZORE_V) > 0) {
                actions.add(REMOVE_ZORE);
            }
            if ((this.action & REMOVE_BLANK_V) > 0) {
                actions.add(REMOVE_BLANK);
            }
            if ((this.action & REMOVE_EMPTY_V) > 0) {
                actions.add(REMOVE_EMPTY);
            }

            // remove
            for (String key : object.keySet()) {

                if (this.remove.contains(key)) {
                    unsetDocument.append(key, 1);
                    continue;
                }
                Object value = object.get(key);
                boolean isRemoved = false;
                for (Remove action : actions) {
                    if (action.process(key, value)) {
                        unsetDocument.append(key, 1);
                        isRemoved = true;
                        break;
                    }
                }
                if (!isRemoved) {
                    appendDocument.append(key, value);
                }
            }
        }

        // append
        for (String key : this.append.keySet()) {
            appendDocument.append(key, this.append.get(key));
        }
        Object id = this.append.get(_id);

        Document operate = new Document();
        if (!appendDocument.isEmpty()) {
            operate.append("$set", appendDocument);
        }
        if (!unsetDocument.isEmpty()) {
            operate.append("$unset", unsetDocument);
        }
        return new UpdateOneModel<>(
            new BasicDBObject(_id, id),
            operate,
            new UpdateOptions().upsert(true)
        );
    }
}

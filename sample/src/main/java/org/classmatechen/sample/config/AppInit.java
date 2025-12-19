package org.classmatechen.sample.config;

import javax.annotation.PostConstruct;

import org.classmatechen.basic.util.ReflectUtil;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.stereotype.Component;

@Component
public class AppInit {

    private MongoConverter converter;
    private MongoTemplate template;

    public AppInit(
        MongoConverter converter,
        MongoTemplate template
    ) {
        this.converter = converter;
        this.template = template;
    }

    @PostConstruct
    private void init() {

        ReflectUtil.setField("org.classmatechen.sample.mongo.MongoRowBuilder.converter", converter);
        ReflectUtil.setField("org.classmatechen.sample.mongo.MongoStore.template", template);
    }
}

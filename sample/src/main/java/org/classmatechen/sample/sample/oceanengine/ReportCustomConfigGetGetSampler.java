package org.classmatechen.sample.sample.oceanengine;

import java.util.List;
import org.classmatechen.basic.group.Consumer;
import org.classmatechen.basic.group.Param;
import org.classmatechen.basic.group.impl.ListGroup;
import org.classmatechen.oceanengine.request.ReportCustomConfigGetGet;
import org.classmatechen.sample.sample.Sampler;
import org.classmatechen.sample.sample.SamplerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.bytedance.ads.model.ReportCustomConfigGetV30ResponseDataListInner;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ReportCustomConfigGetGetSampler implements Sampler<ReportCustomConfigGetGet.Param> {

    public static final String collection = "Dy_ReportCustomConfigGetGet";

    @Autowired
    private MongoTemplate template;

    @Override
    public void sample(List<Param<ReportCustomConfigGetGet.Param>> params) {
        
        this.template.dropCollection(collection);
        Consumer<ReportCustomConfigGetGet.Param, List<ReportCustomConfigGetV30ResponseDataListInner>> consumer = (context, _param, list) -> {
            this.template.insert(list, collection);
        };

        new ListGroup<>(SamplerUtil.create(ReportCustomConfigGetGet.class), params.get(0), consumer).execute();
    }
}

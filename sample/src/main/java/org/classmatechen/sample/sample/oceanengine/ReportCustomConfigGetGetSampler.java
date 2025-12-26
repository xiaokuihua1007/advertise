package org.classmatechen.sample.sample.oceanengine;

import java.util.List;
import org.classmatechen.basic.group.Consumer;
import org.classmatechen.basic.group.GroupFail;
import org.classmatechen.basic.group.Param;
import org.classmatechen.basic.group.impl.ListGroup;
import org.classmatechen.oceanengine.request.ReportCustomConfigGetGet;
import org.classmatechen.sample.sample.AbstarctSampler;
import org.classmatechen.sample.sample.SamplerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.bytedance.ads.model.ReportCustomConfigGetV30ResponseDataListInner;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ReportCustomConfigGetGetSampler extends AbstarctSampler<ReportCustomConfigGetGet.Param> {

    public static final String collection = "Dy_ReportCustomConfigGetGet";

    @Autowired
    private MongoTemplate template;

    @Override
    public List<GroupFail<ReportCustomConfigGetGet.Param>> doSample(List<Param<ReportCustomConfigGetGet.Param>> params) {
        
        this.template.dropCollection(collection);
        Consumer<ReportCustomConfigGetGet.Param, List<ReportCustomConfigGetV30ResponseDataListInner>> consumer = (context, _param, list) -> {
            this.template.insert(list, collection);
        };

        return new ListGroup<>(SamplerUtil.create(ReportCustomConfigGetGet.class), params.get(0), consumer).execute();
    }
}

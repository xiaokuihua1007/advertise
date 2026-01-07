package org.classmatechen.sample.sample.oceanengine;

import java.util.List;

import org.classmatechen.basic.group.Consumer;
import org.classmatechen.basic.group.GroupFail;
import org.classmatechen.basic.group.Param;
import org.classmatechen.basic.group.impl.ListGroup;
import org.classmatechen.oceanengine.request.QwReportCustomConfigGetGet;
import org.classmatechen.sample.sample.AbstarctSampler;
import org.classmatechen.sample.sample.SamplerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.bytedance.ads.model.QianchuanReportCustomConfigGetV10ResponseDataCustomConfigDatasInner;

@Deprecated
@Service
public class QwReportCustomConfigGetGetSampler extends AbstarctSampler<QwReportCustomConfigGetGet.Param> {

    public static final String collection = "Dy_QwReportCustomConfigGetGet";

    @Autowired
    private MongoTemplate template;

    @Override
    public List<GroupFail<QwReportCustomConfigGetGet.Param>> doSample(List<Param<QwReportCustomConfigGetGet.Param>> params) {
        
        this.template.dropCollection(collection);
        Consumer<QwReportCustomConfigGetGet.Param, List<QianchuanReportCustomConfigGetV10ResponseDataCustomConfigDatasInner>> consumer = (context, _param, list) -> {
            this.template.insert(list, collection);
        };

        return new ListGroup<>(SamplerUtil.create(QwReportCustomConfigGetGet.class), params.get(0), consumer).execute();
    }
}

package org.classmatechen.sample.sample.oceanengine;

import java.util.List;
import java.util.stream.Collectors;

import org.classmatechen.basic.group.Consumer;
import org.classmatechen.basic.group.GroupFail;
import org.classmatechen.basic.group.Param;
import org.classmatechen.basic.group.impl.PageGroup;
import org.classmatechen.oceanengine.request.LogSearchGet;
import org.classmatechen.sample.sample.AbstarctSampler;
import org.classmatechen.sample.sample.SamplerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.bytedance.ads.model.ToolsLogSearchV2ResponseDataLogsInner;

import lombok.Data;

@Service
public class LogSearchGetSampler extends AbstarctSampler<LogSearchGetSampler.Inner> {

    public static final String collection = "Dy_LogSearchGet";

    @Autowired
    private MongoTemplate template;

    @Override
    public List<GroupFail<Inner>> doSample(List<Param<Inner>> params) {

        if (null == params || params.isEmpty()) {
            return null;
        }
        String day = params.get(0).getParam().getDay();
        String startTime = day + " 00:00:00", endTime = day + " 23:59:59";

        List<Param<LogSearchGet.Param>> ps = params.stream().map(param -> {
            LogSearchGet.Param p = new LogSearchGet.Param();
            p.setStartTime(startTime);
            p.setEndTime(endTime);
            p.setPageSize(20L);
            p.setAdvertiserId(param.getParam().getAdvertiserId());
            return new Param<>(param.getContext(), p);
        }).collect(Collectors.toList());

        Query query = new Query(Criteria.where("createTime").gte(startTime).lt(endTime));
        /** long count = */ this.template.remove(query, collection).getDeletedCount();
        Consumer<LogSearchGet.Param, List<ToolsLogSearchV2ResponseDataLogsInner>> consumer = (context, param, list) -> {
            this.template.insert(list, collection);
        };

        List<GroupFail<LogSearchGet.Param>> fail = new PageGroup<>(SamplerUtil.page(LogSearchGet.class), ps, consumer).execute();
        if (null != fail) {
            return fail.stream().map(item -> {
                LogSearchGet.Param param = item.getParam();
                Inner inner = new Inner();
                inner.setAdvertiserId(param.getAdvertiserId());
                inner.setDay(day);
                return new GroupFail<>(new Param<>(item.getContext(), inner), item.getError());
            }).collect(Collectors.toList());
        } {
            return null;
        }
    }

    @Data
    public static class Inner {
        private Long advertiserId;
        private String day;
    }
}

package org.classmatechen.sample.sample.tencent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.Document;
import org.classmatechen.basic.group.Consumer;
import org.classmatechen.basic.group.Param;
import org.classmatechen.basic.group.impl.PageGroup;
import org.classmatechen.sample.mongo.MongoRowBuilder;
import org.classmatechen.sample.mongo.MongoStore;
import org.classmatechen.sample.sample.Sampler;
import org.classmatechen.sample.sample.SamplerUtil;
import org.classmatechen.tencent.request.HourlyReportsGet;
import org.springframework.stereotype.Service;

import com.mongodb.client.model.UpdateOneModel;
import com.tencent.ads.model.v3.HourlyReportApiListStruct;

@Service
public class HourlyReportsGetSampler implements Sampler<HourlyReportsGet.Param> {

    public static final String collection = "Tx_HourlyReportsGet";

    @Override
    public void sample(List<Param<HourlyReportsGet.Param>> params) {
        
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Consumer<HourlyReportsGet.Param, List<HourlyReportApiListStruct>> consumer = (context, param, list) -> {
            List<UpdateOneModel<Document>> documents = list
                .stream()
                .map(data -> {
                        String yesterday = param.getDateRange().getStartDate();
                        String key = yesterday.replace("-", "") + (data.getHour() < 10 ? "0" : "") + Long.toString(data.getHour());
                        String time = yesterday + (data.getHour() < 10 ? " 0" : " ") + Long.toString(data.getHour()) + ":00:00";
                        Date date;
                        try {
                            date = format.parse(time);
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                        return new MongoRowBuilder<>(data)
                                                .removeNull()
                                                .append("day", yesterday)
                                                .append("hour", data.getHour())
                                                .append("time", date.getTime() / 1000)
                                                .id(Long.toString(data.getDynamicCreativeId()) + key)
                                                .build();
                    }
                )
                .collect(Collectors.toList());
            MongoStore.store(collection, documents);
        };

        new PageGroup<>(SamplerUtil.page(HourlyReportsGet.class), params, consumer).execute();
    }
}

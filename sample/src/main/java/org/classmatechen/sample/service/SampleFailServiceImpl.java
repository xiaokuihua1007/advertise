package org.classmatechen.sample.service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import org.classmatechen.basic.Context;
import org.classmatechen.basic.group.Param;
import org.classmatechen.sample.mapper.SampleFailMapper;
import org.classmatechen.sample.po.SampleFail;
import org.classmatechen.sample.query.SampleFailQuery;
import org.classmatechen.sample.sample.Sampler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Service
public class SampleFailServiceImpl implements SampleFailService {

    @Autowired
    private SampleFailMapper mapper;

    // @Transactional(rollbackFor = Exception.class)
    public void batchInsert(List<SampleFail> fails) {

        List<List<SampleFail>> partions = Lists.partition(fails, 20);
        for (List<SampleFail> partion : partions) {
            mapper.batchInsert(partion);
        }
    }

    public void retry() {

        List<SampleFail> smaplers = mapper.groupBySampler();
        for (SampleFail fail : smaplers) {
            String smapler = fail.getSampler();
            doRetry(smapler);
        }
        
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void doRetry(String smapler) {

        Sampler sampler;
        try {
            sampler = (Sampler) ((Class<?>) Class.forName(smapler)).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        Long page = 1L;
        Long limit = 100L;
        SampleFailQuery query = new SampleFailQuery();
        query.setPage(page);
        query.setLimit(limit);
        query.setSampler(smapler);
        List<SampleFail> fails = mapper.list(query);

        List<Param> params = new ArrayList<>();
        List<Long> ids = new ArrayList<>();
        while (true) {
            
            for (SampleFail fail : fails) {

                Class<?> clazz = null;
                Type type;
                Object param = null;
                Context context;
                try {
                    if (null != fail.getParam()) {
                        clazz = (Class<?>) Class.forName(fail.getParamClass());
                        type = TypeToken.getParameterized(clazz).getType();
                        param = new Gson().fromJson(fail.getParam(), type);
                    }
                    clazz = (Class<?>) Class.forName(fail.getContextClass());
                    type = TypeToken.getParameterized(clazz).getType();
                    context = new Gson().fromJson(fail.getContext(), type);
                } catch (ClassNotFoundException e) {
                    continue;
                }
                params.add(new Param(context, param));
                ids.add(fail.getId());
            }
            if (fails.size() < limit) {
                break;
            }
            query.next();
            fails = this.mapper.list(query);
        }
        sampler.sample(params);
        mapper.deleteById(ids);
    }
}

package org.classmatechen.sample.sample.tencent;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.Document;
import org.classmatechen.basic.group.Consumer;
import org.classmatechen.basic.group.Param;
import org.classmatechen.basic.group.impl.PageGroup;
import org.classmatechen.sample.mapper.TencentMapper;
import org.classmatechen.sample.mongo.MongoRowBuilder;
import org.classmatechen.sample.mongo.MongoStore;
import org.classmatechen.sample.sample.Sampler;
import org.classmatechen.sample.sample.SamplerUtil;
import org.classmatechen.tencent.TxContext;
import org.classmatechen.tencent.TxContextImpl;
import org.classmatechen.tencent.request.OrganizationAccountRelationGet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mongodb.client.model.UpdateOneModel;
import com.tencent.ads.model.v3.OrganizationAccountRelationGetListStruct;

@Service
public class OrganizationAccountRelationGetSampler implements Sampler<Void> {

    public static final String collection = "Tx_OrganizationAccountRelationGet";

    @Autowired
    private TencentMapper tencentMapper;

    @Override
    public void sample(List<Param<Void>> placeholder) {

        List<Param<OrganizationAccountRelationGet.Param>> params = this.tencentMapper
            .list()
            .stream()
            .map(tencent -> {
                OrganizationAccountRelationGet.Param param = new OrganizationAccountRelationGet.Param();
                // param.setFields 这个设置似乎不起作用, 每次都返回所有字段
                param.setPageSize(50L);
                param.setFields(Arrays.asList(
                    "account_id",
                    "corporation_name",
                    "is_adx",
                    "is_mp",
                    "comment_list"
                ));
                return new Param<>(new TxContextImpl(tencent.getClientId()), param);
            })
            .collect(Collectors.toList());

        Consumer<OrganizationAccountRelationGet.Param, List<OrganizationAccountRelationGetListStruct>> consumer = (context, param, list) -> {
            List<UpdateOneModel<Document>> documents = list
                    .stream()
                    .map(data -> new MongoRowBuilder<>(data)
                                                .id(data.getAccountId())
                                                .append("clientId", ((TxContext) context).getClientId())
                                                .build()
                    )
                    .collect(Collectors.toList());
                MongoStore.store(collection, documents);
        };

        new PageGroup<>(SamplerUtil.page(OrganizationAccountRelationGet.class), params, consumer).execute();
    }
}

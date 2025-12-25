package org.classmatechen.sample.service;

import java.util.List;

import org.classmatechen.sample.mapper.AdvertiseMetricsMapper;
import org.classmatechen.sample.po.AdvertiseMetrics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdvertiseMetricsService {

    @Autowired
    private AdvertiseMetricsMapper mapper;

    public List<AdvertiseMetrics> list() {
        return mapper.list();
    }

    public void insert(List<AdvertiseMetrics> metrics) {
        mapper.insert(metrics);
    }
}

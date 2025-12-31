package org.classmatechen.sample.job;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import com.xxl.job.core.context.XxlJobHelper;

public class XxljobParam {

    private static final String startDate = "startDate";
    private static final String endDate = "endDate";
    private static final String date = "date";

    private final Map<String, String> keyV;

    public XxljobParam() {
        keyV = parse();
    }

    private Map<String, String> parse() {

        String param = XxlJobHelper.getJobParam();
        if (null == param || "" == param) {
            return null;
        }
        String[] rows = param.split("\\");
        Map<String, String> keyV = new HashMap<>();
        for (String row : rows) {
            String[] ky = row.split("=");
            keyV.put(ky[0], ky[1]);
        }
        return keyV;
    }

    public String getDateOrYesterday() {
    
        String date = keyV.get(XxljobParam.date);
        if (null == date || date.length() == 0) {
            return yesterday();
        } else {
            return date;
        }
    }

    public String getDateTimeOrYesterday() {
    
        String date = keyV.get(XxljobParam.date);
        if (null == date || date.length() == 0) {
            return yesterdayStartTime();
        } else {
            return date + " 00:00:00";
        }
    }

    public String getStartDateOrYesterday() {
    
        String startDate = keyV.get(XxljobParam.startDate);
        if (null == startDate ||startDate.length() == 0) {
            return yesterday();
        } else {
            return startDate;
        }
    }

    public String getEndDateOrYesterday() {
    
        String end = keyV.get(endDate);
        if (null == end || end.length() == 0) {
            return yesterday();
        } else {
            return end;
        }
    }

    public String getStartDateTimeOrYesterday() {
    
        String start = keyV.get(startDate);
        if (null == start || start.length() == 0) {
            return yesterdayStartTime();
        } else {
            return start + " 00:00:00";
        }
    }

    public String getEndDateTimeOrYesterday() {
    
        String end = keyV.get(endDate);
        if (null == end || end.length() == 0) {
            return yesterdayEndTime();
        } else {
            return end + " 23:59:59";
        }
    }

    private String yesterday() {

        return new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis() - 1000 * 60 * 60 * 24);
    }

    private String yesterdayStartTime() {

        return yesterday() + " 00:00:00";
    }

    private String yesterdayEndTime() {

        return yesterday() + " 23:59:59";
    }
}

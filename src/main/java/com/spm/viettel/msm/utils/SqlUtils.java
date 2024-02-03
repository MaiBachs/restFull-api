//package com.spm.viettel.msm.utils;
//
//import com.viettel.msm.biz.StaffBiz;
//import freemarker.template.Configuration;
//import freemarker.template.Template;
//import freemarker.template.TemplateException;
//import org.apache.commons.lang3.StringUtils;
//import org.hibernate.SQLQuery;
//import org.hibernate.Session;
//
//import java.io.IOException;
//import java.io.StringWriter;
//import java.util.Collection;
//import java.util.Collections;
//import java.util.Map;
//
//public class SqlUtils {
//
//    public static SQLQuery getSQLQueryFromTemplate(Map<String, Object> params, String templateName) {
//        String sql = getSqlFromTemplate(params, templateName);
//        return getSqlQuery(params, sql);
//    }
//
//    public static SQLQuery getSqlQuery(Session session, Map<String, Object> params, String sql) {
//        SQLQuery query = session.createSQLQuery(sql);
//        for (Map.Entry<String, Object> entry : params.entrySet()) {
//            Object value = entry.getValue();
//            if (value != null && sql.contains(":" + entry.getKey())) {
//                if (value instanceof Collection<?>) {
//                    query.setParameterList(entry.getKey(), Collections.singleton(value));
//                } else if (value instanceof String) {
//                    if (StringUtils.isNotEmpty(value.toString())) {
//                        query.setParameter(entry.getKey(), value);
//                    }
//                } else {
//                    query.setParameter(entry.getKey(), value);
//                }
//            }
//        }
//        return query;
//    }
//
//    public static SQLQuery getSQLQueryCountFromTemplate(Session session, Map<String, Object> params, String templateName) {
//        String sql = getSqlFromTemplate(params, templateName);
//        String countSql = "SELECT count(*) FROM (" + sql + " )";
//        return getSqlQuery(session, params, countSql);
//    }
//}

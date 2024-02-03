package com.spm.viettel.msm.service;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.persistence.Query;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

@Service
public class BaseService {

    @Autowired
    @Qualifier("smartphoneSessionFactory")
    private SessionFactory smartPhoneSessionFactory;

    @Autowired
    @Qualifier("SMSessionFactory")
    private SessionFactory smSessionFactory;
    public <T> Query getSessionFactory(Class<T> instance, String fileName, Map<String, Object> model,Session smartphoneSession) throws IOException, TemplateException {
        Query query = null;
        try {
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
            cfg.setObjectWrapper(new DefaultObjectWrapper(Configuration.VERSION_2_3_31));
            cfg.setClassForTemplateLoading(instance, "/sqls");
            Template t = cfg.getTemplate(fileName);
//        Template t = cfg.getTemplate("searchEvaluationConfig.sftl");
            Writer out = new StringWriter();
            t.process(model, out);
            String sql = out.toString();
            smartphoneSession = smartPhoneSessionFactory.openSession();
            query = smartphoneSession.createQuery(sql);
        }catch (Exception e){
            e.printStackTrace();
        }
        return query;
    }

    public <T> NativeQuery getSMSessionFactory(Class<T> instance, String fileName, Map<String, Object> model,Session smSession) throws IOException, TemplateException {
        NativeQuery query = null;
        try {
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_32);
            cfg.setObjectWrapper(new DefaultObjectWrapper(Configuration.VERSION_2_3_32));
            cfg.setClassForTemplateLoading(instance, "/sqls");
            Template t = cfg.getTemplate(fileName);
//        Template t = cfg.getTemplate("searchEvaluationConfig.sftl");
            Writer out = new StringWriter();
            t.process(model, out);
            String sql = out.toString();
            smSession = smSessionFactory.openSession();
            query = smSession.createSQLQuery(sql);
        }catch (Exception e){
            e.printStackTrace();
        }
        return query;
    }
}

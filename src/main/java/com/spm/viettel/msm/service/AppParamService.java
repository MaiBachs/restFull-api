package com.spm.viettel.msm.service;

import com.spm.viettel.msm.dto.AppParamsDTO;
import com.spm.viettel.msm.repository.sm.AppParamsSMRepository;
import com.spm.viettel.msm.repository.sm.entity.AppParamsSM;
import com.spm.viettel.msm.repository.smartphone.AppParamsRepository;
import com.spm.viettel.msm.repository.smartphone.entity.AppParamsSmartPhone;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.persistence.Query;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AppParamService extends BaseService{
    private final Logger loggerFactory = LoggerFactory.getLogger(AppParamService.class);
    @Autowired
    private AppParamsRepository appParamsSmartPhoneRepository;
    @Autowired
    private AppParamsSMRepository appParamsSmRepository;
    @Autowired
    @Qualifier("smartphoneSessionFactory")
    private SessionFactory smartPhoneSessionFactory;

    public AppParamsSmartPhone getParamsByTypeAndCode(String type, String posCode) {
        return appParamsSmartPhoneRepository.getAppParamsByTypeAndCode(type,posCode);
    }

    public List<AppParamsSM> getParamsSMByTypeAndCode(String type, String posCode) {
        return appParamsSmRepository.findAppParamsSMByTypeAndCode(type,posCode);
    }
    public  List<AppParamsSmartPhone> findAppParamsByTypeAndStatus(String type) {
        Session smartphoneSession = null;
        List<AppParamsSmartPhone> appParamSmartPhones = new ArrayList<>();
        try {
            Map<String, Object> model = new HashMap<>();
            model.put("type", type);
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
            cfg.setObjectWrapper(new DefaultObjectWrapper(Configuration.VERSION_2_3_31));
            cfg.setClassForTemplateLoading(getClass(), "/sqls"); // Thay thế "/templates" bằng đường dẫn thực tế
            Template t = cfg.getTemplate("findAppParamsByTypeAndStatus.sftl");
            Writer out = new StringWriter();
            t.process(model, out);
            String sql = out.toString();
            smartphoneSession = smartPhoneSessionFactory.openSession();
            Query query = smartphoneSession.createNativeQuery(sql)
                    .addScalar("type", StringType.INSTANCE)
                    .addScalar("code", StringType.INSTANCE)
                    .addScalar("name", StringType.INSTANCE)
                    .addScalar("value", StringType.INSTANCE)
                    .addScalar("status", StringType.INSTANCE)
                    .addScalar("telecomServiceId", LongType.INSTANCE)
                    .setResultTransformer(Transformers.aliasToBean(AppParamsSmartPhone.class));
            if(type !=  null){
                query.setParameter("type", type);
            }
        appParamSmartPhones = query.getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (smartphoneSession != null && smartphoneSession.isOpen()) {
                smartphoneSession.close();
            }
        }
        return appParamSmartPhones;
    }

    public List<String> getListChannelAllowPlanVisit() {
        List<String> lst = appParamsSmRepository.getListChannelAllowPlanVisit("CHANNEL_TYPE_QLKD_MAP");
        return lst;
    }
}
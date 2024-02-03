package com.spm.viettel.msm.service;

import com.spm.viettel.msm.dto.CoordinatesDto;
import freemarker.template.TemplateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DoubleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.persistence.Query;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TimeKeepingService extends BaseService{


    public List<CoordinatesDto> searchCoordinates(Session session, String staffCode, Integer objectType, String fromDate, String toDate) throws TemplateException, IOException {
        Map<String, Object> params = new HashMap<>();
        NativeQuery query = getSMSessionFactory(getClass(),"searchCoordinates.sftl",params,session);
        query.setParameter("staffCode", staffCode);
        query.setParameter("objectType", objectType);
        query.setParameter("fromDate", fromDate);
        query.setParameter("toDate", toDate);
        query.setResultTransformer(Transformers.aliasToBean(CoordinatesDto.class));
        query.addScalar("x", DoubleType.INSTANCE);
        query.addScalar("y", DoubleType.INSTANCE);
        return query.list();
    }

}

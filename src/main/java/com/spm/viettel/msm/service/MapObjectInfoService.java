package com.spm.viettel.msm.service;

import com.spm.viettel.msm.dto.MapObjectAttributeDto;
import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MapObjectInfoService {
    public List<MapObjectAttributeDto> getAttributesByObjectId(Session sessionSM, Long mapObjectId) {
        try {
            StringBuffer sql = new StringBuffer("SELECT mat.ATTRIBUTE_NAME_VN nameVN, mat.ATTRIBUTE_NAME_EN nameEN, mat.ATTRIBUTE_NAME_ES nameES,att.ATTRIBUTE_VALUE value ");
            sql.append(" FROM MAP_OBJECT_INFO_ATTRIBUTE att INNER JOIN MAP_ATTRIBUTES mat ON (att.ATTRIBUTE_ID= mat.ATTRIBUTE_ID) ");
            sql.append(" WHERE att.object_id = ").append(mapObjectId);
            sql.append(" AND att.STATUS = 1 ");
            sql.append(" ORDER BY mat.ORDERS ASC ");
            SQLQuery query = sessionSM.createSQLQuery(sql.toString());
            query.setResultTransformer(Transformers.aliasToBean(MapObjectAttributeDto.class));
            query.addScalar("nameVN", StringType.INSTANCE);
            query.addScalar("nameEN", StringType.INSTANCE);
            query.addScalar("nameES", StringType.INSTANCE);
            query.addScalar("value", StringType.INSTANCE);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getTotalRegisterOfBTS(Session sessionSM, Long btsId) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT SUM(rpsub.NUM_REG) FROM bonus.syn_BTS_register rpsub, sm.BTS_MANAGEMENT bts  ");
        sb.append(" WHERE rpsub.bts_site=SUBSTR(bts.BTS_CODE, 1, 7)  ");
        sb.append("  AND bts.BTS_ID = ").append(btsId);
        sb.append(" AND rpsub.report_date = TRUNC (SYSDATE - 1)");
        SQLQuery query = sessionSM.createSQLQuery(sb.toString());
        List<Object> results = query.list();
        if (CollectionUtils.isNotEmpty(results)) {
            for (Object o : results) {
                if (o!= null) {
                    return o.toString();
                }
            }
        }
        return "0";
    }

    public static String get3G4GOfBTS(Session session, Long btsId) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT rpsub.BTS_CODE, rpsub.NUM_REG FROM bonus.syn_BTS_register rpsub, sm.BTS_MANAGEMENT bts  ");
        sb.append(" WHERE rpsub.bts_site=SUBSTR(bts.BTS_CODE, 1, 7)  ");
        sb.append("  AND bts.BTS_ID = ").append(btsId);
        sb.append(" AND rpsub.report_date = TRUNC (SYSDATE - 1)");
        SQLQuery query = session.createSQLQuery(sb.toString());
        List<Object[]> results = query.list();
        StringBuilder sbResult = new StringBuilder();
        if (CollectionUtils.isNotEmpty(results)) {
            for (Object[] o : results) {
                if (o[0] != null) {
                    if (sbResult.length() > 0) {
                        sbResult.append(";");
                    }
                    sbResult.append(o[0].toString()).append(": ").append(o[1]);
                }
            }
        }
        return sbResult.toString();
    }

    public static String getPopulationOfBTS(Session sessionSM, Long btsId) {
        String sql = "SELECT POPULATION FROM sm.BTS_MANAGEMENT WHERE BTS_ID=" + btsId;
        SQLQuery query = sessionSM.createSQLQuery(sql);
        List<Object> results = query.list();
        if (CollectionUtils.isNotEmpty(results)) {
            for (Object o : results) {
                if (o != null) {
                    return o.toString();
                }
            }
        }
        return "0";
    }
}

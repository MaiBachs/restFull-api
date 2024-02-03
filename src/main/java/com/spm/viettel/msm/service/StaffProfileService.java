package com.spm.viettel.msm.service;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class StaffProfileService {

    private final Logger loggerFactory = LoggerFactory.getLogger(StaffProfileService.class);


    public List<String> getStaffImages(Session session, Long staffId) {
        List<String> list = new ArrayList<String>();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT image ");
        sb.append("FROM STAFF_PROFILE ");
        sb.append("WHERE  STAFF_ID=").append(staffId);
        sb.append(" ORDER BY CREATE_DATE ASC ");
        SQLQuery query = session.createSQLQuery(sb.toString());
        List<Clob> result = query.list();
        if (CollectionUtils.isNotEmpty(result)) {
            for (Clob o : result) {
                String imageBase64 = clobToString(o);
                if (StringUtils.isNotEmpty(imageBase64) && StringUtils.isNotBlank(imageBase64)) {
                    list.add(imageBase64);
                }
            }
        }
        return list;
    }

    /*********************************************************************************************
     * From CLOB to String
     * @return string representation of clob
     *********************************************************************************************/
    public String clobToString(java.sql.Clob data) {
        final StringBuilder sb = new StringBuilder();
        if (data != null) {
            try {
                final Reader reader = data.getCharacterStream();
                final BufferedReader br = new BufferedReader(reader);

                int b;
                while (-1 != (b = br.read())) {
                    sb.append((char) b);
                }
                br.close();
            } catch (SQLException e) {
                loggerFactory.error("SQL. Could not convert CLOB to string", e);
            } catch (IOException e) {
                loggerFactory.error("IO. Could not convert CLOB to string", e);
            } catch (Exception e) {
                loggerFactory.error("EX. Could not convert CLOB to string", e);
            }
        }
        return sb.toString();
    }

}

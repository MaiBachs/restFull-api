package com.spm.viettel.msm.service;

import com.spm.viettel.msm.Constant;
import com.spm.viettel.msm.dto.UserTokenDto;
import com.spm.viettel.msm.repository.sm.ShopRepository;
import com.spm.viettel.msm.repository.sm.StaffRepository;
import com.spm.viettel.msm.repository.sm.entity.Shop;
import com.spm.viettel.msm.repository.sm.entity.Staff;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import viettel.passport.client.ObjectToken;
import viettel.passport.client.UserToken;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author edward
 */
@Service
public class AuthenticateService {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticateService.class);

    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    @Qualifier("vsaSessionFactory")
    private SessionFactory vsaSessionFactory;

    @Value("STAFF_MANAGER_LIST")
    private String staffManagerList;

    @Value("${vsa.domainCode}")
    private String appCode;

    public boolean checkAuthority(String authority, UserToken vsaUserToken) {
        try {
            if (StringUtils.isEmpty(authority)) {
                return false;
            }
            if (vsaUserToken != null) {
                // Check exist role in component role of user
                ArrayList<ObjectToken> roleList = vsaUserToken.getComponentList();
                for (Iterator<ObjectToken> it = roleList.iterator(); it.hasNext(); ) {
                    ObjectToken objToken = it.next();
                    if (null != objToken && authority.equals(objToken.getObjectName())) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return false;
    }

    public UserTokenDto getUserCurrent(UserToken vsaUserToken) {
        UserTokenDto userToken = new UserTokenDto();
        String userName = "thanhnv3_vtp";
        if (vsaUserToken != null) {
            userName = vsaUserToken.getUserName();
            userToken.setUserID(vsaUserToken.getUserID());
            userToken.setLoginName(userName);
            userToken.setFullName(vsaUserToken.getFullName());
            userToken.setBelongToManyGroup(false);
        }
        Staff staff = staffRepository.findFirstByStaffCodeIgnoreCase(userName);
        if (staff != null) {
            userToken.setPosCode(staff.getPosCode());
            userToken.setStaffCode(staff.getStaffCode());
            userToken.setUserID(staff.getStaffId());
            userToken.setStaffName(staff.getName());
            userToken.setShopId(staff.getShopId());
            userToken.setChannelTypeId(staff.getChannelTypeId());
            if (staff.getShopId() != null) {
                Shop shop = shopRepository.findFirstByShopIdAndStatusNot(staff.getShopId(), Constant.STATUS_NOT_ACTIVE);
                if (shop != null) {
                    userToken.setShopName(shop.getName());
                    userToken.setShopCode(shop.getShopCode());
                    userToken.setShopType(shop.getShopType());
                }
            }
        }
        return userToken;
    }

    public UserTokenDto getUserInformation(UserToken vsaUserToken) {
        UserTokenDto userToken = getUserCurrent(vsaUserToken);
        List<String> roles = getRolesOfUser(userToken.getStaffCode());
        userToken.setRoles(roles);
        return userToken;
    }

    public List<String> getRolesOfUser(String staffCode) {
        List<String> roles = new ArrayList<>();
        String sql =
                "SELECT o.OBJECT_CODE \n" +
                        "FROM OBJECTS o LEFT JOIN ROLE_OBJECT ro ON (o.OBJECT_ID = ro.OBJECT_ID)\n" +
                        "LEFT JOIN ROLES r ON (ro.ROLE_ID = r.ROLE_ID)\n" +
                        "LEFT JOIN ROLE_USER ru ON (r.ROLE_ID = ru.ROLE_ID)\n" +
                        "LEFT JOIN USERS u ON (ru.USER_ID = u.USER_ID)\n" +
                        "LEFT JOIN APPLICATIONS a2 ON (o.APP_ID = a2.APP_ID)\n" +
                        "WHERE o.STATUS =1\n" +
                        "AND ro.IS_ACTIVE =1\n" +
                        "AND r.STATUS =1\n" +
                        "AND ru.IS_ACTIVE =1\n" +
                        "AND a2.APP_CODE = '" + appCode + "' " +
                        "AND UPPER(u.USER_NAME) ='" + staffCode +"' ";

        Session vsaSession = null;
        try {
            vsaSession = vsaSessionFactory.openSession();
            Query queryRole = vsaSession.createNativeQuery(sql);
            roles = queryRole.getResultList();
//            roles.add("QL_REPORT_CHANNEL");
//            roles.add("QLHS_QLHS_PHYSICAL_BR");
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (vsaSession != null && vsaSession.isOpen()) {
                vsaSession.close();
            }
        }
        return roles;
    }

    public boolean isManager(String posCode){
        if(StringUtils.isNotEmpty(posCode)){
            String[] managerList = staffManagerList.split(";");
            for (String s : managerList) {
                if(s.equalsIgnoreCase(posCode)) return true;
            }
        }
        return false;
    }
}

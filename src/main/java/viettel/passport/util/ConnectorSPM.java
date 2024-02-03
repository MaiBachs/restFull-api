package viettel.passport.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import viettel.passport.client.ServiceTicketValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

@Data
@AllArgsConstructor
@Component
public class ConnectorSPM {
    private HttpServletRequest request;
    private HttpServletResponse response;
    private String ticket;
    private String passportLoginURL;
    private String serviceURL;
    private String domainCode;
    private String passportValidateURL;
    private String errorUrl;
    private String[] allowedUrls;
    private boolean modifyHeader = false;
    private String returnUrl;
    private static Logger LOG = LoggerFactory.getLogger(ConnectorSPM.class);

    public ConnectorSPM(HttpServletRequest req, HttpServletResponse res, String passportLoginURL, String serviceURL, String domainCode, String passportValidateURL, String errorUrl, String[] allowedUrls, boolean modifyHeader) {
        this.request = req;
        this.response = res;
        this.passportLoginURL = passportLoginURL;
        this.serviceURL = serviceURL;
        this.domainCode = domainCode;
        this.passportValidateURL = passportValidateURL;
        this.errorUrl = errorUrl;
        this.allowedUrls = allowedUrls;
        this.modifyHeader = modifyHeader;
    }

    public Boolean isAuthenticate() {
        return this.request != null && this.request.getSession() != null && this.request.getSession().getAttribute("vsaUserToken") != null;
    }

    public Boolean hadTicket() {
        String st = this.request.getParameter("ticket");
        return st != null && st.trim().length() > 0;
    }

    public Boolean getAuthenticate() throws IOException {
        try {
            String tmpTicket = this.request.getParameter("ticket");
            String ip = this.request.getHeader("VTS-IP");
            String ipwan = this.request.getRemoteAddr();
            String mac = this.request.getHeader("VTS-MAC");

            try {
                if (ip != null && ip.length() > 0) {
                    ip = ModifyHeaderUtils.parseIP(ip);
                } else {
                    ip = null;
                }

                if (mac != null && mac.length() > 0) {
                    mac = ModifyHeaderUtils.parseMAC(mac);
                } else {
                    mac = null;
                }
            } catch (Exception var7) {
                ip = null;
                mac = null;
                LOG.error("Giai ma modify header that bai " + var7.getMessage(), var7);
            }

            if (tmpTicket != null && tmpTicket.trim().length() != 0) {
                ServiceTicketValidator stValidator = new ServiceTicketValidator();
                stValidator.setCasValidateUrl(passportValidateURL);
                stValidator.setServiceTicket(tmpTicket);
                if (this.returnUrl != null && this.returnUrl.trim().length() > 0) {
                    stValidator.setService(serviceURL + "?return=" + this.returnUrl);
                } else {
                    stValidator.setService(serviceURL);
                }

                stValidator.setDomainCode(domainCode);
                stValidator.validate();
                HttpSession session = this.request.getSession();
                session.invalidate();
                session = this.request.getSession(true);
                this.response.setHeader("SET-COOKIE", "JSESSIONID=" + session.getId() + ";Path=" + this.request.getContextPath() + ";HttpOnly");
                if (!stValidator.isAuthenticationSuccesful()) {
                    session.setAttribute("vsaUserToken", (Object) null);
                    session.setAttribute("netID", (Object) null);
                    return false;
                } else {
                    session.setAttribute("vsaUserToken", stValidator.getUserToken());
                    session.setAttribute("netID", stValidator.getUser());
                    session.setAttribute("VTS-IP", ip);
                    session.setAttribute("VTS-MAC", mac);
                    if (ipwan == null) {
                        LOG.error("IP WAN get from request is NULL!!!");
                        System.out.println("IP WAN get from request is NULL!!!");
                    } else {
                        LOG.info("IP WAN is: " + ipwan);
                        System.out.println("IP WAN is: " + ipwan);
                    }

                    session.setAttribute("VTS-IPWAN", ipwan);
                    if (this.returnUrl != null && this.returnUrl.trim().length() > 0) {
                        session.setAttribute("return_url", this.returnUrl);
                    }

                    if (!modifyHeader && ip == null && mac == null) {
                        LOG.info(String.format("User %s logined at ipwan %s without modifyHeader", stValidator.getUser(), ipwan));
                    } else {
                        LOG.info(String.format("User %s logined at ip %s and mac %s ipwan %s session %s - %s modifyHeader", stValidator.getUser(), ip, mac, ipwan, session.getId(), modifyHeader ? "with" : "without"));
                    }

                    return true;
                }
            } else {
                return false;
            }
        } catch (ParserConfigurationException var8) {
            LOG.error(var8.getMessage(), var8);
            return false;
        }
    }


//    public static void setPassportValidateURL(String passportValidateURL) {
//        Connector.passportValidateURL = passportValidateURL;
//    }

//    public static void setAllowedUrls(String[] strs) {
//        allowedUrls = new String[strs.length];
//        System.arraycopy(strs, 0, allowedUrls, 0, strs.length);
//    }
//
//    public static String[] getAllowedUrls() {
//        String[] tmps = new String[allowedUrls.length];
//        System.arraycopy(allowedUrls, 0, tmps, 0, allowedUrls.length);
//        return tmps;
//    }

//    static {
//        try {
//            rb = ResourceBundle.getBundle("cas");
//            passportLoginURL = rb.getString("loginUrl");
//            serviceURL = rb.getString("service");
//            domainCode = rb.getString("domainCode");
//            passportValidateURL = rb.getString("validateUrl");
//            errorUrl = rb.getString("errorUrl");
//            modifyHeader = rb.getString("useModifyHeader").equalsIgnoreCase("true");
//            allowedUrls = rb.getString("AllowUrl").split(",");
//        } catch (MissingResourceException var1) {
//            LOG.error(var1.getMessage(), var1);
//        }
//
//    }
}

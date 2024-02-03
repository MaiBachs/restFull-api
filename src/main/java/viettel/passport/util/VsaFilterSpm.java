package viettel.passport.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;
import viettel.passport.client.ObjectToken;
import viettel.passport.client.UserToken;
import viettel.passport.client.VSAValidate;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

/**
 * @author edward
 */
public class VsaFilterSpm implements Filter {
    private FilterConfig filterConfig;
    private static Logger logger = LoggerFactory.getLogger(VsaFilter.class);
    private static HashSet<String> casAllowedURL = new HashSet();
    private static HashSet<String> allMenuURL = new HashSet();
    private boolean timeoutToErrorPage = false;
    private boolean ignoreAjaxRequest = false;

    private String passportLogoutURL;
    private String passportLoginURL;
    private String serviceURL;
    private String domainCode;
    private String passportValidateURL;
    private String passportServiceUrl;
    private String errorUrl;
    private String[] allowedUrls;
    private boolean modifyHeader = false;

    public VsaFilterSpm(String passportLoginURL, String passportLogoutURL, String serviceURL, String domainCode, String passportValidateURL, String passportServiceUrl, String errorUrl, String[] allowedUrls, boolean modifyHeader) {
        this.passportLoginURL = passportLoginURL;
        this.passportLogoutURL = passportLogoutURL;
        this.serviceURL = serviceURL;
        this.domainCode = domainCode;
        this.passportValidateURL = passportValidateURL;
        this.passportServiceUrl = passportServiceUrl;
        this.errorUrl = errorUrl;
        this.allowedUrls = allowedUrls;
        this.modifyHeader = modifyHeader;
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
        try {
            logger.debug("Lay danh sach AllowUrl tu file config 'cas_en_US.properties'");
            if (this.allowedUrls != null) {
                getCasAllowedURL().addAll(Arrays.asList(this.allowedUrls));
            }
        } catch (Exception var8) {
            logger.error("Loi lay danh sach AllowUrl tu file config:'cas_en_US.properties'", var8);
            throw new ExceptionInInitializerError(var8);
        }

        try {
            this.setFilterConfig(config);
            logger.info("File Config :" + config);
            String sTimeoutToErrorPage = this.filterConfig.getInitParameter("timeoutToErrorPage");
            if ("true".equalsIgnoreCase(sTimeoutToErrorPage)) {
                this.timeoutToErrorPage = true;
            } else {
                this.timeoutToErrorPage = false;
            }

            String sIgnoreAjaxRequest = this.filterConfig.getInitParameter("ignoreAjaxRequest");
            if ("true".equalsIgnoreCase(sIgnoreAjaxRequest)) {
                this.ignoreAjaxRequest = true;
            } else {
                this.ignoreAjaxRequest = false;
            }

            SpmVSAValidate vsaValidate = new SpmVSAValidate(passportServiceUrl, domainCode);
            ArrayList<ObjectToken> objs = vsaValidate.getAllMenu();

            for (int i = 0; i < objs.size(); ++i) {
                allMenuURL.add(((ObjectToken) objs.get(i)).getObjectUrl().split("\\?")[0]);
            }

            if (logger.isDebugEnabled()) {
                logger.debug("All menu URL: " + allMenuURL);
            }
        } catch (Exception var7) {
            logger.error("Loi khi lay danh sach tat ca module URL", var7);
            allMenuURL.clear();
        }

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        logger.debug("VSAFilter process request");
        HttpServletRequest req = null;
        HttpServletResponse res = null;
        if (request instanceof HttpServletRequest) {
            req = (HttpServletRequest) request;
        }

        String returnURL = req.getParameter("return");
        if (response instanceof HttpServletResponse) {
            res = (HttpServletResponse) response;
        }

        ConnectorSPM cnn = new ConnectorSPM(req, res, passportLoginURL, serviceURL, domainCode, passportValidateURL, errorUrl, allowedUrls, modifyHeader);
        if (returnURL != null && returnURL.trim().length() > 0) {
            cnn.setReturnUrl(returnURL);
        }
        req.setAttribute("VSA-IsPassedVSAFilter", "True");
        logger.info(String.valueOf(req.getRequestURL()));
        String linkFull = req.getServletPath();
        if (!this.alowURL(req.getRequestURI(), this.allowedUrls) && !this.alowURL(linkFull, this.allowedUrls)) {
            logger.error("request not in allow urls. Check if session authen?");
            //if is logout
            String macSavedInSession;
            Map<String, String[]> map = request.getParameterMap();
//            if (linkFull.contains("logout")){ ==> For use only this filter in project
            // Only for use spring boot security with this filter in same project
//            if (linkFull.contains("login") && map.containsKey("logout")) {
            if (linkFull.contains("logout")) {
                HttpSession session = req.getSession();
                if (session != null) {
                    session.invalidate();
                    String logoutUrl = this.passportLogoutURL + "?service=" + URLEncoder.encode(this.serviceURL, "UTF-8");
                    res.sendRedirect(logoutUrl);
                }
            } else if (!cnn.isAuthenticate()) {
                logger.error("session is not authen. Check if have ticket in request?");
                String redirectUrl;
                if (cnn.hadTicket()) {
                    logger.error("have ticket from passport. Validate this ticket");
                    logger.error("cnn: " + cnn);
                    if (!cnn.getAuthenticate()) {
                        logger.warn("Redirect to error page by authenticate failure.");
                        redirectUrl = this.errorUrl + "?errorCode=" + "AuthenticateFailure";
                        req.setAttribute("VSA-IsPassedVSAFilter", "False");
                        res.setHeader("VSA-Flag", "InPageRedirect");
                        res.setHeader("VSA-Location", redirectUrl);
                        if (this.ignoreAjaxRequest && req.getHeader("X-Requested-With") != null && req.getHeader("X-Requested-With").length() > 0) {
                            logger.debug("ajax request! Fw request to next filter");
                            chain.doFilter(request, response);
                        } else {
                            logger.debug("redirect browser to error page: " + redirectUrl);
                            res.sendRedirect(redirectUrl);
                        }
                    } else {
                        logger.debug("validat successfull. Fw request to next filter");
                        chain.doFilter(request, response);
                    }
                } else {
                    redirectToLogin(request, response, chain, req, res, returnURL, cnn);
                }
            } else {
                logger.debug("session already authen. Check session hijacking");
                HttpSession session = req.getSession();
                logger.debug("get ip,mac stored in session");
                macSavedInSession = (String) session.getAttribute("VTS-MAC");
                logger.debug("mac store: " + macSavedInSession);
                String ipSavedInSession = (String) session.getAttribute("VTS-IP");
                logger.debug("ip lan sotre: " + ipSavedInSession);
                String ipWanSavedInSession = (String) session.getAttribute("VTS-IPWAN");
                logger.debug("ipwan stored: " + ipWanSavedInSession);
                logger.debug("get ip,mac in request");
                String mac = req.getHeader("VTS-MAC");
                String ip = req.getHeader("VTS-IP");
                String ipWan = req.getRemoteAddr();

                try {
                    logger.info("decode ip");
                    if (ip != null && ip.length() > 0) {
                        ip = ModifyHeaderUtils.parseIP(ip);
                    } else {
                        ip = null;
                    }

                    logger.debug("decode mac");
                    if (mac != null && mac.length() > 0) {
                        mac = ModifyHeaderUtils.parseMAC(mac);
                    } else {
                        mac = null;
                    }
                } catch (Exception var19) {
                    ip = null;
                    mac = null;
                    logger.error("Giai ma modify header that bai " + var19.getMessage(), var19);
                }

                logger.debug("ip in request: " + ip);
                logger.debug("mac in request: " + mac);
                String fakeSessionRedirectUrl = cnn.getPassportLoginURL() + "?appCode=" + cnn.getDomainCode() + "&service=" + URLEncoder.encode(cnn.getServiceURL(), "UTF-8");
                boolean fakeSession = false;
                logger.debug("compare ip wan");
                if (!ipWanSavedInSession.equalsIgnoreCase(ipWan)) {
                    logger.error(String.format("User: %s dang nhap tu 2 ipwan!!! ipwan dau tien %s, ipwan sau %s", (String) session.getAttribute("netID"), ipWanSavedInSession, ipWan));
                    res.sendRedirect(fakeSessionRedirectUrl);
                    session.invalidate();
                    fakeSession = true;
                }

                logger.debug("compate ip lan");
                if (!fakeSession && ipSavedInSession != null && !ipSavedInSession.equalsIgnoreCase(ip)) {
                    logger.error(String.format("User: %s dang nhap tu 2 ip!!! ip dau tien %s, ip sau %s", (String) session.getAttribute("netID"), ipSavedInSession, ip));
                    res.sendRedirect(fakeSessionRedirectUrl);
                    session.invalidate();
                    fakeSession = true;
                }

                logger.debug("compare mac");
                if (!fakeSession && macSavedInSession != null && !macSavedInSession.equalsIgnoreCase(mac)) {
                    logger.error(String.format("User: %s dang nhap tu 2 mac!!! mac dau tien %s, mac sau %s", (String) session.getAttribute("netID"), macSavedInSession, mac));
                    res.sendRedirect(fakeSessionRedirectUrl);
                    session.invalidate();
                    fakeSession = true;
                }

                if (!fakeSession) {
                    if (!allMenuURL.isEmpty() && allMenuURL.contains(req.getServletPath())) {
                        logger.info("url already declare in VSA. Check rights excute url?");
                        if (this.getVsaAllowedServletPath(req).contains(req.getServletPath())) {
                            logger.info("url have rights to excute. Fw to next filter");
                            chain.doFilter(request, response);
                        } else {
                            logger.error("Khai bao chua phan quyen: " + req.getServletPath() + "\n");
                            String redirectUrl = this.errorUrl + "?errorCode=" + "NotPermissionAction";
                            req.setAttribute("VSA-IsPassedVSAFilter", "False");
                            res.setHeader("VSA-Flag", "NewPageRedirect");
                            res.setHeader("VSA-Location", redirectUrl);
                            if (this.ignoreAjaxRequest && req.getHeader("X-Requested-With") != null && req.getHeader("X-Requested-With").length() > 0) {
                                logger.info("bypass ajax request. next to filter");
                                chain.doFilter(request, response);
                            } else {
                                logger.info("redirect to error page: " + redirectUrl);
                                res.sendRedirect(redirectUrl);
                            }
                        }
                    } else {
                        logger.error("Chua khai bao: " + req.getServletPath() + "\n");
                        logger.debug("fw request to next filter");
                        chain.doFilter(request, response);
                    }
                }
            }
        } else {
            logger.debug("request in allow urls");
            chain.doFilter(req, res);
        }

    }

    public void redirectToLogin(ServletRequest request, ServletResponse response, FilterChain chain, HttpServletRequest req, HttpServletResponse res, String returnURL, ConnectorSPM cnn) throws IOException, ServletException {
        String redirectUrl;
        String macSavedInSession;
        logger.info("Request have no ticket. Redirect to passport");
        redirectUrl = cnn.getServiceURL();
        if (returnURL != null && returnURL.trim().length() > 0) {
            redirectUrl = redirectUrl + "?return=" + returnURL;
        }

        macSavedInSession = cnn.getPassportLoginURL() + "?appCode=" + cnn.getDomainCode() + "&service=" + URLEncoder.encode(redirectUrl, "UTF-8");
        req.setAttribute("VSA-IsPassedVSAFilter", "False");
        res.setHeader("VSA-Flag", "InPageRedirect");
        res.setHeader("VSA-Location", macSavedInSession);
        if (this.ignoreAjaxRequest && req.getHeader("X-Requested-With") != null && req.getHeader("X-Requested-With").length() > 0) {
            logger.info("ajax request! Fw request to next filter");
            chain.doFilter(request, response);
        } else {
            if (this.timeoutToErrorPage) {
                macSavedInSession = this.errorUrl + "?errorCode=" + "SessionTimeout";
            }

            logger.info("redirect to passport login: " + macSavedInSession);
            res.sendRedirect(macSavedInSession);
        }
    }

    @Override
    public void destroy() {
    }

    private Boolean alowURL(String url, String[] listAlowUrl) {
        AntPathMatcher matcher = new AntPathMatcher();
        for (String includePattern :listAlowUrl) {
            if (matcher.match(includePattern, url)) {
                return true;
            }
        }
        return false;
    }

    public static HashSet<String> getAllMenuURL() {
        return allMenuURL;
    }

    public static void setAllMenuURL(HashSet<String> allMenuURL) {
        VsaFilterSpm.allMenuURL = allMenuURL;
    }

    public static HashSet<String> getCasAllowedURL() {
        return casAllowedURL;
    }

    public static void setCasAllowedURL(HashSet<String> casAllowedURL) {
        VsaFilterSpm.casAllowedURL = casAllowedURL;
    }

    private HashSet<String> getVsaAllowedServletPath(HttpServletRequest request) {
        UserToken vsaUserToken = (UserToken) request.getSession().getAttribute("vsaUserToken");
        HashSet<String> vsaAllowedURL = new HashSet();
        Iterator i$ = vsaUserToken.getObjectTokens().iterator();

        while (i$.hasNext()) {
            ObjectToken ot = (ObjectToken) i$.next();
            String servletPath = ot.getObjectUrl();
            if (!"#".equals(servletPath)) {
                vsaAllowedURL.add(servletPath.split("\\?")[0]);
            }
        }

        return vsaAllowedURL;
    }

    public FilterConfig getFilterConfig() {
        return this.filterConfig;
    }

    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }
}

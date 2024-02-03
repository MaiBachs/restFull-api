package com.spm.viettel.msm.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import viettel.passport.util.VsaFilterSpm;

import javax.servlet.Filter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

//@Configuration
public class FilterConfig {
    private final Logger loggerFactory = LoggerFactory.getLogger(FilterConfig.class);

    @Value("${vsa.loginUrl}")
    private  String passportLoginURL;
    @Value("${vsa.logoutUrl}")
    private  String passportLogoutURL;
    @Value("${vsa.service}")
    private  String serviceURL;
    @Value("${vsa.service_protocol}")
    private  String serviceProtocol;
    @Value("${server.port}")
    private int port;
    @Value("${vsa.domainCode}")
    private  String domainCode;
    @Value("${vsa.validateUrl}")
    private  String passportValidateURL;
    @Value("${vsa.passportServiceUrl}")
    private  String passportServiceUrl;
    @Value("${vsa.errorUrl}")
    private  String errorUrl;
    @Value("${vsa.AllowUrl}")
    private  String allowedUrl;
    @Value("${vsa.useModifyHeader:false}")
    private Boolean modifyHeader = false;
    private  String serviceURLGetFromServer;
    private  String errorUrlGetFromServer;

    @Bean
    public FilterRegistrationBean vsaFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(vsaFilter());
        registration.addUrlPatterns("/*");
        registration.setName("VSA Filter");
        registration.setOrder(1);
        return registration;
    }

    public Filter vsaFilter() {
        try {
            Enumeration<NetworkInterface> nics = NetworkInterface.getNetworkInterfaces();
            while (nics.hasMoreElements()) {
                NetworkInterface nic = nics.nextElement();
                if (!nic.isLoopback()) {
                    Enumeration<InetAddress> addrs = nic.getInetAddresses();
                    while (addrs.hasMoreElements()) {
                        InetAddress addr = addrs.nextElement();
                        if (addr instanceof Inet4Address) {
                            StringBuilder sb = new StringBuilder(serviceProtocol);
                            sb.append(addr.getHostAddress()).append(":").append(port).append("/");
                            serviceURLGetFromServer = sb.toString();
                            errorUrlGetFromServer = sb.toString();
                        }
                    }
                }
            }
        } catch (SocketException e) {
            loggerFactory.error("======Get IP of Server error", e);
        }
        String[] allowedUrls = allowedUrl.split(",");
        return new VsaFilterSpm(passportLoginURL, passportLogoutURL, serviceURLGetFromServer, domainCode,
                passportValidateURL, passportServiceUrl, errorUrlGetFromServer, allowedUrls, modifyHeader);
    }

}

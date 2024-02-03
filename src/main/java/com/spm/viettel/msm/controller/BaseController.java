package com.spm.viettel.msm.controller;

import com.poiji.bind.Poiji;
import com.poiji.option.PoijiOptions;
import com.spm.viettel.msm.dto.PlanChannelSale;
import com.spm.viettel.msm.dto.request.BaseRequest;
import com.spm.viettel.msm.utils.PaginationHelper;
import com.spm.viettel.msm.utils.Config;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import viettel.passport.client.ObjectToken;
import viettel.passport.client.UserToken;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.io.*;
import java.util.*;

public abstract class BaseController{

    private final Logger loggerFactory = LoggerFactory.getLogger(ChannelwithgroupController.class);
    protected Locale locale = Locale.US;


    @Value("${TEMPLATE_PATH}")
    private String templateFolder;

    @Value("${FILE_TEMP_UPLOAD_PATH}")
    private String fileNameFullFolder;

    public PageRequest buildPageRequest(BaseRequest baseRequestDTO) {
        if (baseRequestDTO == null) {
            return PaginationHelper.generatePageRequest(PaginationHelper.DEFAULT_CURRENT_PAGE , PaginationHelper.DEFAULT_RECORD_PER_PAGE);
        }

        return PaginationHelper.generatePageRequest(baseRequestDTO.getPage() - 1, baseRequestDTO.getOffset() == 0 ? PaginationHelper.DEFAULT_RECORD_PER_PAGE: baseRequestDTO.getOffset());
    }

    public UserToken getUserLogined(){
        HttpServletRequest req = getRequest();
        HttpSession session = req.getSession();
        return  (UserToken) session.getAttribute("vsaUserToken");
    }

    public String getIpAddress() {
        HttpServletRequest req = getRequest();
        return req.getRemoteAddr();
        /*
         * Su dung cho vsa 3.0 HttpServletRequest req = getRequest();
         * HttpSession sess = req.getSession(); String ip = (String)
         * sess.getAttribute("VTS-IP"); String mac = (String)
         * sess.getAttribute("VTS-MAC "); return ip;
         */

    }

    public boolean checkAuthority(String authority) {
        HttpServletRequest req = getRequest();
        try {
            HttpSession session = req.getSession();
            viettel.passport.client.UserToken vsaUserToken = (viettel.passport.client.UserToken) session.getAttribute("vsaUserToken");

            if (vsaUserToken != null) {
                // Check exist role in component role of user
                ArrayList<ObjectToken> roleList = (ArrayList<ObjectToken>) (vsaUserToken.getComponentList());
                for (Iterator<ObjectToken> it = roleList.iterator(); it.hasNext(); ) {
                    ObjectToken objToken = it.next();
                    if (null != objToken && authority.equals(objToken.getObjectName()) && objToken.getStatus() == 1) {
                        return true;
                    }
                }
                //Collection menuList = vsaUserToken.getParentMenu();
            }
        } catch (Exception e) {
            loggerFactory.error(e.getMessage());
        }

        return true;
    }

    public<T> List<T> convertToExcel(File excelFile, Class<T> bean) throws IOException {
        List<T> result = new ArrayList<>();
        PoijiOptions options = PoijiOptions.PoijiOptionsBuilder.settings().headerStart(1).addListDelimiter(";").build();
        List<T> rows =  Poiji.fromExcel(excelFile, bean,options);
        result.addAll(rows);
        return result;
    }

    public<T> String buildImportResultFile(List<T> data,String fileTemplate,String fileName,String formatType) {
        String templateFolder = this.templateFolder;
        String fileNameNew = fileName + Calendar.getInstance().getTimeInMillis() + "." + formatType;
        String fileNameFull = this.fileNameFullFolder + File.separator + fileNameNew;
        File templateFile = new File(templateFolder, fileTemplate);
        try {
            InputStream is = new FileInputStream(templateFile);
            File fileResult = new File(fileNameFull);
            OutputStream os = new FileOutputStream(fileResult);
            Context context = new Context();
            context.putVar("data", data);
            JxlsHelper.getInstance().processTemplate(is, os, context);
            return fileNameNew;
        } catch (Exception e) {
            e.printStackTrace();
            loggerFactory.error(e.getMessage());
        }
        return null;
    }

    public abstract HttpServletRequest getRequest();
}

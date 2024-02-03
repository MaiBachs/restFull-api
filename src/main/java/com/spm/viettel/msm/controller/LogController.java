package com.spm.viettel.msm.controller;

import com.spm.viettel.msm.dto.response.GeneralResponse;
import com.spm.viettel.msm.dto.response.LogResponse;
import com.spm.viettel.msm.factory.ResponseFactory;
import com.spm.viettel.msm.utils.Constants;
import com.viettel.im.common.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/log")
public class LogController extends BaseController{
    @Autowired
    private HttpServletRequest request;
    @Override
    public HttpServletRequest getRequest() {
        return request;
    }
    @Autowired
    private ResponseFactory responseFactory;

    @Value("${logging.file.path}")
    private String logForderDir;

    @GetMapping(path = "/view-log-channel-checklist")
    public ResponseEntity<GeneralResponse<LogResponse>> viewLogChannelCheckList(@RequestParam(value = "currentPage", required = true) int currentPage){
        File folder = new File(logForderDir + File.separator);
        List<File> listOfFiles = Arrays.asList(folder.listFiles());
        List<File> isFiles = listOfFiles.stream().filter(fw->fw.isFile()).collect(Collectors.toList());
        Collections.reverse(isFiles);
        LogResponse logResponse = new LogResponse();
        if(isFiles.size() > currentPage*Constants.PAGE_SIZE){
            for (int i = (currentPage-1)*Constants.PAGE_SIZE; i < currentPage*Constants.PAGE_SIZE; i++) {
                logResponse.getFileNames().add(isFiles.get(i).getName());
            }
        } else if(isFiles.size() > (currentPage-1)*Constants.PAGE_SIZE && isFiles.size() < currentPage*Constants.PAGE_SIZE){
            for (int i = (currentPage-1)*Constants.PAGE_SIZE; i < listOfFiles.size(); i++) {
                logResponse.getFileNames().add(isFiles.get(i).getName());
            }
        }

        logResponse.setTotalRecord(isFiles.size());
        logResponse.setTotalPage((long) Math.ceil((float)logResponse.getTotalRecord()/(float) Constants.PAGE_SIZE));
        logResponse.setCurrentPage(currentPage);
        return responseFactory.success(logResponse);
    }

    @GetMapping(path = "/download-log-channel-checklist")
    public ResponseEntity<?> getLogChannelCheckList(@RequestParam(name = "fileNameDownload", required = true) String fileNameDownload){
        try {
            File fileToDownload = new File(logForderDir + File.separator + fileNameDownload);
            if (fileToDownload != null) {
                InputStream inputStream = new FileInputStream(fileToDownload);
                String fileName = fileNameDownload;
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.TEXT_PLAIN);
                headers.setContentDispositionFormData("attachment", fileName);
                InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
                return ResponseEntity.ok()
                        .headers(headers)
                        .body(inputStreamResource);
            }
            return responseFactory.success();
        } catch (FileNotFoundException e) {
            return responseFactory.success();
        }
    }
}

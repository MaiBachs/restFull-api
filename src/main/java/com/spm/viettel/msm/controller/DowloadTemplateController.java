package com.spm.viettel.msm.controller;

import com.spm.viettel.msm.factory.ResponseFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.io.InputStream;

@RestController
@RequestMapping("/api/download-template")
public class DowloadTemplateController extends BaseController{

    private final Logger loggerFactory = LoggerFactory.getLogger(DowloadTemplateController.class);
    private final HttpServletRequest request;
    private final ResponseFactory responseFactory;

    @Value("${TEMPLATE_PATH}")
    private String templateFolder;

    @Value("${FILE_TEMP_UPLOAD_PATH}")
    private String fileNameFullFolder;

    @Autowired
    public DowloadTemplateController(HttpServletRequest request, ResponseFactory responseFactory) {
        this.request = request;
        this.responseFactory = responseFactory;
    }

    @Override
    public HttpServletRequest getRequest() {
        return request;
    }

    @Operation(summary = "download template import")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully Download file"),
            @ApiResponse(responseCode = "400", description = "invalid request review parameters"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found")
    })
    @GetMapping("{filename}/{fileStatusError}")
    public ResponseEntity<?> downloadTemplate(@PathVariable("filename") String fileName,@PathVariable("fileStatusError") int status) {
        String fileToDownload = templateFolder + File.separator + fileName;
         if(!fileToDownload.isEmpty()){
             if(status == 1){
                 fileToDownload = fileNameFullFolder + File.separator + fileName;
             }
             try {
                 InputStream inputStream = new FileInputStream(fileToDownload);
                 HttpHeaders headers = new HttpHeaders();
                 headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                 headers.setContentDispositionFormData("attachment", fileName);
                 InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
                 return ResponseEntity.ok()
                         .headers(headers)
                         .body(inputStreamResource);
             }catch (Exception e){
                 loggerFactory.error(e.getMessage());
                 throw new RuntimeException(e);
             }
         }
        return ResponseEntity.notFound().build();
    }
}

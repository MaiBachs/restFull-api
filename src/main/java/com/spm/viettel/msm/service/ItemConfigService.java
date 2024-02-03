
package com.spm.viettel.msm.service;
import com.spm.viettel.msm.dto.*;
import com.spm.viettel.msm.dto.enums.ActionChannelType;
import com.spm.viettel.msm.dto.request.SearchConfigureItemRequest;
import com.spm.viettel.msm.dto.response.SearchItemResultResponse;
import com.spm.viettel.msm.repository.smartphone.*;
import com.spm.viettel.msm.repository.smartphone.entity.*;
import com.spm.viettel.msm.utils.*;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPSClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.FloatType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import javax.persistence.Query;
import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class ItemConfigService extends BaseService{
    private final org.slf4j.Logger loggerFactory = LoggerFactory.getLogger(ItemConfigService.class);
    @Autowired
    private AppParamsRepository appParamsRepository;
    @Autowired
    private ItemConfigRepository itemConfigRepository;
    @Autowired
    private ItemConfigReasonService itemConfigReasonService;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private JobService jobService;
    @Autowired
    private ReasonRepository reasonRepository;
    @Autowired
    private ChannelTypeSmartPhoneRepository channelTypeSmartPhoneRepository;
    @Autowired
    private AppParamService appParamService;
    @Autowired
    @Qualifier("smartphoneSessionFactory")
    private SessionFactory smartPhoneSessionFactory;

    @Value("${TEMPLATE_PATH}")
    private String templateFolder;
    @Value("${FILE_TEMP_UPLOAD_PATH}")
    private String fileNameFullFolder;
    @Value("${ftp.working_dir}")
    private String ftpWorkingDir;
    @Value("${ftp.host}")
    private String ftpHost;
    @Value("${ftp.port}")
    private String ftpPort;
    @Value("${ftp.user}")
    private String ftpUser;
    @Value("${ftp.pass}")
    private String ftpPass;
    @Value("${ftp.is_ssl}")
    private String ftpIsSsl;
    public static final String TRUE = "true";

    private static final Logger log = LogManager.getLogger(ItemConfigService.class);

    public AppParamsSmartPhone getParamsByTypeAndCode(String type, String code) {
        return appParamsRepository.getAppParamsByTypeAndCode(type, code);
    }

    public ItemConfig findByJobIdAndChannelTypeId(Long jobId, Long channelTypeId){
        return itemConfigRepository.findByJobIdAndChannelTypeId(jobId, channelTypeId);
    }

    public SearchItemResultResponse searchAndDownloadConfigItem(SearchConfigureItemRequest request){
        SearchItemResultResponse response = new SearchItemResultResponse();
        Session smartphoneSession = null;
        List<ConfigureItemDto> configureItemDtos = new ArrayList<>();
        try {
            Map<String, Object> model = new HashMap<>();
            model.put("evaluationId", request.getEvaluationId());
            model.put("channelTypeId", request.getChannelTypeId());
            model.put("groupId", request.getGroupId());
            model.put("fromDate", request.getFromDate());
            model.put("toDate", request.getToDate());
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
            cfg.setObjectWrapper(new DefaultObjectWrapper(Configuration.VERSION_2_3_31));
            cfg.setClassForTemplateLoading(getClass(), "/sqls"); // Thay thế "/templates" bằng đường dẫn thực tế
            Template t = cfg.getTemplate("SearchAndDownloadItemConfig.sftl");
            Writer out = new StringWriter();
            t.process(model, out);
            String sql = out.toString();
            smartphoneSession = smartPhoneSessionFactory.openSession();
            Query query = smartphoneSession.createNativeQuery(sql)
                    .addScalar("evaluationId", LongType.INSTANCE)
                    .addScalar("evaluationName", StringType.INSTANCE)
                    .addScalar("groupId", LongType.INSTANCE)
                    .addScalar("groupName", StringType.INSTANCE)
                    .addScalar("jobId", LongType.INSTANCE)
                    .addScalar("jobName", StringType.INSTANCE)
                    .addScalar("id", LongType.INSTANCE)
                    .addScalar("channelType", StringType.INSTANCE)
                    .addScalar("channelTypeId", LongType.INSTANCE)
                    .addScalar("percent", FloatType.INSTANCE)
                    .addScalar("ok", LongType.INSTANCE)
                    .addScalar("nok", LongType.INSTANCE)
                    .addScalar("na", LongType.INSTANCE)
                    .addScalar("validation", StringType.INSTANCE)
                    .addScalar("url", StringType.INSTANCE)
                    .addScalar("createdDate", DateType.INSTANCE)
                    .addScalar("lastUpdate", DateType.INSTANCE)
                    .addScalar("reasonNok", StringType.INSTANCE)
                    .addScalar("gravedad", StringType.INSTANCE)
                    .setResultTransformer(Transformers.aliasToBean(ConfigureItemDto.class));
            if(request.getEvaluationId() != null){
                query.setParameter("evaluationId", request.getEvaluationId());
            }
            if(request.getChannelTypeId() != null){
                query.setParameter("channelTypeId", request.getChannelTypeId());
            }
            if(request.getGroupId() != null){
                query.setParameter("groupId", request.getGroupId());
            }
            if(StringUtils.isNotEmpty(request.getFromDate())){
                query.setParameter("fromDate", request.getFromDate());
            }
            if(StringUtils.isNotEmpty(request.getToDate())){
                query.setParameter("toDate", request.getToDate());
            }
            configureItemDtos = query.getResultList();
            // get Reason
            response.setItemConfigDtoList(configureItemDtos);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (smartphoneSession != null && smartphoneSession.isOpen()) {
                smartphoneSession.close();
            }
        }
        return response;
    }

    public SearchItemResultResponse searchConfigItem(SearchConfigureItemRequest request){
        SearchItemResultResponse response = new SearchItemResultResponse();
        Session smartphoneSession = null;
        List<ConfigureItemDto> configureItemDtos = new ArrayList<>();
        try {
            Map<String, Object> model = new HashMap<>();
            model.put("evaluationId", request.getEvaluationId());
            model.put("channelTypeId", request.getChannelTypeId());
            model.put("groupId", request.getGroupId());
            model.put("fromDate", request.getFromDate());
            model.put("toDate", request.getToDate());
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
            cfg.setObjectWrapper(new DefaultObjectWrapper(Configuration.VERSION_2_3_31));
            cfg.setClassForTemplateLoading(getClass(), "/sqls"); // Thay thế "/templates" bằng đường dẫn thực tế
            Template t = cfg.getTemplate("SearchItemConfig.sftl");
            Writer out = new StringWriter();
            t.process(model, out);
            String sql = out.toString();
            smartphoneSession = smartPhoneSessionFactory.openSession();
            Query query = smartphoneSession.createNativeQuery(sql)
                    .addScalar("evaluationId", LongType.INSTANCE)
                    .addScalar("evaluationName", StringType.INSTANCE)
                    .addScalar("groupId", LongType.INSTANCE)
                    .addScalar("groupName", StringType.INSTANCE)
                    .addScalar("jobId", LongType.INSTANCE)
                    .addScalar("jobName", StringType.INSTANCE)
                    .addScalar("id", LongType.INSTANCE)
                    .addScalar("channelType", StringType.INSTANCE)
                    .addScalar("channelTypeId", LongType.INSTANCE)
                    .addScalar("lastUpdate", DateType.INSTANCE)
                    .setResultTransformer(Transformers.aliasToBean(ConfigureItemDto.class));
            if(request.getEvaluationId() != null){
                query.setParameter("evaluationId", request.getEvaluationId());
            }
            if(request.getChannelTypeId() != null){
                query.setParameter("channelTypeId", request.getChannelTypeId());
            }
            if(request.getGroupId() != null){
                query.setParameter("groupId", request.getGroupId());
            }
            if(request.getFromDate() != null){
                query.setParameter("fromDate", request.getFromDate());
            }
            if(request.getToDate() != null){
                query.setParameter("toDate", request.getToDate());
            }
            if (request.isPaging()) {
                int pageSize = request.getPageSize() > 0 ? request.getPageSize() : Constants.PAGE_SIZE;
                int currentPage = request.getCurrentPage() > 0 ? request.getCurrentPage() - 1 : 0;
                String countSql = "SELECT COUNT(*) FROM ( " + sql + " )";
                Query queryCount = smartphoneSession.createNativeQuery(countSql);
                if(request.getEvaluationId() != null){
                    queryCount.setParameter("evaluationId", request.getEvaluationId());
                }
                if(request.getChannelTypeId() != null){
                    queryCount.setParameter("channelTypeId", request.getChannelTypeId());
                }
                if(request.getGroupId() != null){
                    queryCount.setParameter("groupId", request.getGroupId());
                }
                if(request.getFromDate() != null){
                    queryCount.setParameter("fromDate", request.getFromDate());
                }
                if(request.getToDate() != null){
                    queryCount.setParameter("toDate", request.getToDate());
                }
                Long countResults = ((BigDecimal) queryCount.getSingleResult()).longValue();
                int lastPageNumber = (int) (Math.ceil(countResults / Double.valueOf(pageSize)));
                if (currentPage < 0) {
                    currentPage = 0;
                } else if (currentPage > lastPageNumber) {
                    currentPage = lastPageNumber;
                }
                response.setTotalRecord(countResults);
                response.setTotalPage(lastPageNumber);
                response.setCurrentPage(currentPage);
                query.setMaxResults(pageSize);
                query.setFirstResult(currentPage * pageSize);
            }
            configureItemDtos = query.getResultList();
            response.setItemConfigDtoList(configureItemDtos);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (smartphoneSession != null && smartphoneSession.isOpen()) {
                smartphoneSession.close();
            }
        }
        return response;
    }

    public List<Reason> getReasonDtoByItemConfig(Long itemConfigId){
        Session smartphoneSession = null;
        List<Reason> reasons = null;
        try{
            smartphoneSession = smartPhoneSessionFactory.openSession();
            String hql = "SELECT new Reason(r.reasonId, r.code, r.name, r.note, r.status, r.createdDate, r.lastUpdate, icr.gravedad, icr.gravedadCode) FROM ItemConfigReason icr INNER JOIN Reason r ON r.reasonId = icr.reasonId WHERE icr.itemConfigId = :itemConfigId AND icr.status = 1";
            Query query = smartphoneSession.createQuery(hql);
            query.setParameter("itemConfigId", itemConfigId);
            reasons = query.getResultList();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (smartphoneSession != null && smartphoneSession.isOpen()) {
                smartphoneSession.close();
            }
        }
        return reasons;
    }

    public String buildImportResultFile(List<ItemConfigDto> itemConfigBeanList) {
        String templateFolder = this.templateFolder;
        String fileName = "import_item_result_" + Calendar.getInstance().getTimeInMillis() + ".xls";
        String fileNameFull = this.fileNameFullFolder + File.separator + fileName;
        String fileTemplate = "Template_import_item_result.xls";
        File templateFile = new File(templateFolder, fileTemplate);
        try {
            InputStream is = new FileInputStream(templateFile);
            File fileResult = new File(fileNameFull);
            OutputStream os = new FileOutputStream(fileResult);
            Context context = new Context();
            context.putVar("item", itemConfigBeanList);
            JxlsHelper.getInstance().processTemplate(is, os, context);
            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String buildSeachItemResultFile(List<ConfigureItemDto> configureItemDtoList) {
        String templateFolder = this.templateFolder;
        String fileName = "search_item_result_" + Calendar.getInstance().getTimeInMillis() + ".xls";
        String fileNameFull = this.fileNameFullFolder + File.separator + fileName;
        String fileTemplate = "Template_search_item_result.xls";
        File templateFile = new File(templateFolder, fileTemplate);
        try {
            InputStream is = new FileInputStream(templateFile);
            File fileResult = new File(fileNameFull);
            OutputStream os = new FileOutputStream(fileResult);
            Context context = new Context();
            context.putVar("item", configureItemDtoList);
            JxlsHelper.getInstance().processTemplate(is, os, context);
            return fileNameFull;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String rendataImportItem() {
        String templateFolder = this.templateFolder;
        String fileName = "Template_import_item_rendata_map.xls";
        String fileNameFull = this.templateFolder + File.separator + fileName;
        String fileTemplate = "Template_import_item_rendata.xls";
        File templateFile = new File(templateFolder, fileTemplate);

        List<Job> jobListEvaluation =  jobService.findJobsByAudit();
        List<Job> jobListGroup = jobService.findJobByParentIdInAndStatus(jobListEvaluation.stream().map(e->{
            return e.getJobId();
        }).collect(Collectors.toList()));
        List<Job> jobListItem = jobService.findJobByParentIdInAndStatus(jobListGroup.stream().map(g->{
            return g.getJobId();
        }).collect(Collectors.toList()));
        List<ChannelType> channelTypes = channelTypeSmartPhoneRepository.getPlansByCcAudit();
        List<Reason> reasons = reasonRepository.findReasonByStatus(Constants.STATUS_ACTIVE);

        try {
            InputStream is = new FileInputStream(templateFile);
            File fileResult = new File(fileNameFull);
            OutputStream os = new FileOutputStream(fileResult);
            Context context = new Context();
            context.putVar("channelType", channelTypes);
            context.putVar("evaluation", jobListEvaluation);
            context.putVar("group", jobListGroup);
            context.putVar("item", jobListItem);
            context.putVar("reason", reasons);
            JxlsHelper.getInstance().processTemplate(is, os, context);
            return fileNameFull;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Reason> findReasonByStatus(){
        return reasonRepository.findReasonByStatus(Constants.STATUS_ACTIVE);
    }

    public void updateValidationEdit(ItemConfig itemConfig, ConfigureItemDto configureItemDto){
        if(configureItemDto.getPhoto().equals(1l)){
            itemConfig.setValidation(Constants.VALIDATION_PHOTO);
        }else if(configureItemDto.getFile().equals(1l)){
            itemConfig.setValidation(Constants.VALIDATION_FILE);
        }else if(configureItemDto.getVideo().equals(1l)){
            itemConfig.setValidation(Constants.VALIDATION_VIDEO);
        }else if(configureItemDto.getAudio().equals(1l)){
            itemConfig.setValidation(Constants.VALIDATION_AUDIO);
        }
    }

    public ItemConfig edit(ConfigureItemDto configureItemDto){
        configureItemDto.setSuccessUpload(true);
        ItemConfig itemConfig = itemConfigRepository.findById(configureItemDto.getId()).orElse(null);
        itemConfig.setPercent(configureItemDto.getPercent() != null ? configureItemDto.getPercent() :itemConfig.getPercent());
        itemConfig.setOk(configureItemDto.getOk() != null ? configureItemDto.getOk() : itemConfig.getOk());
        itemConfig.setNok(configureItemDto.getNok() != null ? configureItemDto.getNok() : itemConfig.getNok());
        itemConfig.setNa(configureItemDto.getNa() != null ? configureItemDto.getNa() : itemConfig.getNa());
        updateValidationEdit(itemConfig, configureItemDto);
        itemConfig.setStatus(configureItemDto.getStatus() != null ? configureItemDto.getStatus() : itemConfig.getStatus());
        itemConfig.setUrl(configureItemDto.getUrl() != null ? null : itemConfig.getUrl());
        itemConfig.setLastUpdate(new Date());

        if(StringUtils.isNotEmpty(configureItemDto.getFileDto().getFileName()) && StringUtils.isNotEmpty(configureItemDto.getFileDto().getContent())){
            FTPClient ftpClient = TRUE.equalsIgnoreCase(ftpIsSsl) ? new FTPSClient() : new FTPClient();
            try{
                FTPHelper.connectAndLogin(ftpClient, ftpHost, ftpUser, ftpPass);
                String filePath = uploadFileToFtpServer(ftpClient, configureItemDto.getFileDto(), configureItemDto);
                itemConfig.setUrl(configureItemDto.getSuccessUpload() == true ? filePath : itemConfig.getUrl());
                itemConfig.setFileName(configureItemDto.getSuccessUpload() == true ? configureItemDto.getFileDto().getFileName() : itemConfig.getFileName());
            }catch (Exception e){
                e.printStackTrace();
                configureItemDto.setSuccessUpload(false);
            }finally {
                try {
                    FTPHelper.disconnect(ftpClient);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        List<ItemConfigReason> itemConfigReasons = new ArrayList<>();
        List<AppParamsSmartPhone> gravedadApparams = appParamService.findAppParamsByTypeAndStatus(Constants.CODE_GRAVEDAD_USING);
        List<ItemConfigReason> itemConfigReasons1 = itemConfigReasonService.findByItemConfigId(itemConfig.getId());
        if(configureItemDto.getReasonList() != null){
            for(Reason reason: configureItemDto.getReasonList()){
                ItemConfigReason checkItemConfigReason = itemConfigReasons1.stream().filter(ic->ic.getItemConfigId().equals(itemConfig.getId()) && ic.getReasonId().equals(reason.getReasonId())).findFirst().orElse(null);
                if(checkItemConfigReason != null){
                    checkItemConfigReason.setLastUpdate(new Date());
                    checkItemConfigReason.setGravedad(reason.getGravedad());
                    checkItemConfigReason.setStatus(Constants.ACTIVE);
                    checkItemConfigReason.setReasonId(reason.getReasonId());
                    AppParamsSmartPhone gravedadApp = gravedadApparams.stream().filter(grave->grave.getValue().equals(reason.getGravedad().trim().toUpperCase())).findFirst().orElse(null);
                    if(gravedadApp == null){
                        return null;
                    }else{
                        checkItemConfigReason.setStatus(reason.getStatus());
                        checkItemConfigReason.setGravedadCode(gravedadApp.getCode());
                        itemConfigReasons.add(checkItemConfigReason);
                    }
                }else{
                    ItemConfigReason itemConfigReason = new ItemConfigReason();
                    itemConfigReason.setItemConfigId(configureItemDto.getId());
                    itemConfigReason.setReasonId(reason.getReasonId());
                    itemConfigReason.setGravedad(reason.getGravedad());
                    itemConfigReason.setGravedadCode(reason.getGravedadCode());
                    itemConfigReason.setStatus(reason.getStatus());
                    itemConfigReason.setCreatedDate(new Date());
                    itemConfigReason.setLastUpdate(new Date());
                    itemConfigReasons.add(itemConfigReason);
                }
            }
        }
        itemConfigReasonService.saveAll(itemConfigReasons);
        List<ItemConfigReason> itemConfigReasons2 = itemConfigReasonService.findByItemConfigIdAndStatus(itemConfig.getId(), Constants.STATUS_ACTIVE);
        if(configureItemDto.getNok() != null && configureItemDto.getNok().equals(1l) && itemConfigReasons2.size() == 0){
            itemConfig.setStatus(Constants.INACTIVE);
        }

        return itemConfigRepository.save(itemConfig);
    }

    public String uploadFileToFtpServer(FTPClient ftpClient, FileDto fileDto, ConfigureItemDto configureItemDto) {
        try {
            String ext = "";
            String[] names = fileDto.getFileName().split("\\.");
            if (names.length > 1) {
                ext = "." + names[names.length - 1];
            }
            String fileName = Calendar.getInstance().getTimeInMillis() + "_msm" + ext;
            String fileNameFull = fileNameFullFolder + File.separator + fileName;
            byte[] decodedImg = Base64.getDecoder().decode(fileDto.getContent().split(",")[1].getBytes(StandardCharsets.UTF_8));
            Path destinationFile = Paths.get(fileNameFullFolder, fileName);
            Files.write(destinationFile, decodedImg);
            // upload to ftp server
            final String remotePath = FileUtils.concatPath(ftpWorkingDir, fileName);
            System.out.println("=======Start upload to pdf file:" + fileName);
            System.out.println("=======Start upload to pdf remote file:" + remotePath);
            FTPHelper.upload(ftpClient, fileNameFull, remotePath);
            org.apache.commons.io.FileUtils.deleteQuietly(new File(fileNameFull));
            System.out.println("=======End upload to pdf file:" + fileName);
            System.out.println("=======End upload to pdf remote file:" + remotePath);
            return remotePath;
        } catch (Exception e) {
            e.printStackTrace();
            configureItemDto.setSuccessUpload(false);
        }
        configureItemDto.setSuccessUpload(false);
        return null;
    }

    public ItemConfig save(ItemConfig itemConfig){
        return itemConfigRepository.save(itemConfig);
    }

    public List<ItemConfig> saveAll(List<ItemConfig> itemConfigs){
        return itemConfigRepository.saveAll(itemConfigs);
    }

    public ItemConfig saveItemConfig(ItemConfigDto itemConfigDto, ChannelType channelType1, ItemConfig config, Job jobItemMap, Job jobEvaluationMap,Job jobGroupMap, String[] formatOkNokNaUsing){
        ItemConfig itemConfig = new ItemConfig();
        itemConfig.setChannelTypeId(channelType1.getChannelTypeId());
        if(config != null){
            itemConfig.setId(config.getId());
            itemConfig.setCreatedDate(config.getCreatedDate());
            itemConfig.setLastUpdate(new Date());
            itemConfigReasonService.deleteAllByItemConfigId(config.getId());
        }else{
            itemConfig.setCreatedDate(new Date());
        }
        itemConfig.setLastUpdate(new Date());
        itemConfig.setPercent(Float.parseFloat(itemConfigDto.getPercent()));
        itemConfig.setValidation(itemConfigDto.getValidation().toUpperCase());
        itemConfig.setJobId(jobItemMap.getJobId());
        itemConfig.setEvaluationId(jobEvaluationMap.getJobId());
        itemConfig.setGroupId(jobGroupMap.getJobId());
        if(itemConfigDto.getOk().equalsIgnoreCase(formatOkNokNaUsing[0])){
            itemConfig.setOk(1l);
        }else{
            itemConfig.setOk(0l);
        }
        if(itemConfigDto.getNok().equalsIgnoreCase(formatOkNokNaUsing[0])){
            itemConfig.setNok(1l);
        }else{
            itemConfig.setNok(0l);
        }
        if(itemConfigDto.getNa().equalsIgnoreCase(formatOkNokNaUsing[0])){
            itemConfig.setNa(1l);
        }else{
            itemConfig.setNa(0l);
        }
        itemConfig.setStatus(1l);
        return itemConfig;
    }

    public ItemConfigReason saveItemConfigReason(ItemConfig config, Reason reason, AppParamsSmartPhone gravedad){
        ItemConfigReason itemConfigReason = null;
        itemConfigReason = itemConfigReasonService.findItemConfigReasonByItemConfigIdAndReasonId(config.getId(), reason.getReasonId());
        if(itemConfigReason == null){
            itemConfigReason = new ItemConfigReason();
            itemConfigReason.setItemConfigId(config.getId());
            itemConfigReason.setReasonId(reason.getReasonId());
            itemConfigReason.setStatus(1l);
            itemConfigReason.setGravedad(gravedad.getValue());
            itemConfigReason.setGravedadCode(gravedad.getCode());
            itemConfigReason.setCreatedDate(new Date());
            itemConfigReason.setLastUpdate(new Date());
            return itemConfigReason;
        }else {
            itemConfigReason.setStatus(1l);
            itemConfigReason.setGravedad(gravedad.getValue());
            itemConfigReason.setGravedadCode(gravedad.getCode());
            itemConfigReason.setLastUpdate(new Date());
            return itemConfigReason;
        }
    }


    public static boolean equalItemConfigAndItemConfigDto(ItemConfig itemConfig, ItemConfigDto itemConfigDto){
        boolean check = true;
        Float percentItemConfigDto = Float.parseFloat(itemConfigDto.getPercent()+".0");
        String okItemConfigDto = itemConfigDto.getOk().trim().toUpperCase();
        String nokItemConfigDto = itemConfigDto.getNok().trim().toUpperCase();
        String naItemConfigDto = itemConfigDto.getNa().trim().toUpperCase();
        if(!itemConfig.getPercent().equals(percentItemConfigDto)){
            check =  false;
        }
        if((okItemConfigDto.equals("YES") && !itemConfig.getOk().equals(1l))
                || (okItemConfigDto.equals("NO") && !itemConfig.getOk().equals(0l))){
            check =  false;
        }
        if((nokItemConfigDto.equals("YES") && !itemConfig.getNok().equals(1l))
                || (nokItemConfigDto.equals("NO") && !itemConfig.getNok().equals(0l))){
            check =  false;
        }
        if((naItemConfigDto.equals("YES") && !itemConfig.getNa().equals(1l))
                || (naItemConfigDto.equals("NO") && !itemConfig.getNa().equals(0l))){
            check =  false;
        }
        if(!itemConfigDto.getValidation().trim().toUpperCase().equals(itemConfig.getValidation())){
            check =  false;
        }

        return check;
    }

    public boolean checkDuplicate(ItemConfigDto itemConfigDto1, ItemConfigDto itemConfigDto2){
        boolean check = false;
        if((itemConfigDto1.getChannelType().trim().toUpperCase().equals(itemConfigDto2.getChannelType().trim().toUpperCase()))
        && (itemConfigDto1.getEvaluation().trim().toUpperCase().equals(itemConfigDto2.getEvaluation().trim().toUpperCase()))
        && (itemConfigDto1.getGroup().trim().toUpperCase().equals(itemConfigDto2.getGroup().trim().toUpperCase()))
        && (itemConfigDto1.getItem().trim().toUpperCase().equals(itemConfigDto2.getItem().trim().toUpperCase()))
        && (itemConfigDto1.getPercent().trim().toUpperCase().equals(itemConfigDto2.getPercent().trim().toUpperCase()))
        && (itemConfigDto1.getOk().trim().toUpperCase().equals(itemConfigDto2.getOk().trim().toUpperCase()))
        && (itemConfigDto1.getNok().trim().toUpperCase().equals(itemConfigDto2.getNok().trim().toUpperCase()))
        && (itemConfigDto1.getNa().trim().toUpperCase().equals(itemConfigDto2.getNa().trim().toUpperCase()))
        && (itemConfigDto1.getValidation().trim().toUpperCase().equals(itemConfigDto2.getValidation().trim().toUpperCase()))
        && (Objects.equals(itemConfigDto1.getReasonNok(), itemConfigDto2.getReasonNok()))
        && (Objects.equals(itemConfigDto1.getGravedad(), itemConfigDto2.getGravedad()))){
            check = true;
        }
        return (check);
    }

    public void checkUpdate(ItemConfigDto itemConfigDto1, List<ItemConfigDto> itemConfigDtoList, int i, int k ,Locale locale){
        if((itemConfigDto1.getChannelType().trim().toUpperCase().equals(itemConfigDtoList.get(k).getChannelType().trim().toUpperCase()))
                && (itemConfigDto1.getEvaluation().trim().toUpperCase().equals(itemConfigDtoList.get(k).getEvaluation().trim().toUpperCase()))
                && (itemConfigDto1.getGroup().trim().toUpperCase().equals(itemConfigDtoList.get(k).getGroup().trim().toUpperCase()))
                && (itemConfigDto1.getItem().trim().toUpperCase().equals(itemConfigDtoList.get(k).getItem().trim().toUpperCase()))) {
            itemConfigDtoList.get(k).setComment(messageSource.getMessage(MessageKey.ITEM_IMPORT_UPDATED_BY_ROW, new String[]{String.valueOf(i+1)}, locale));
        }
    }

    public ItemConfigReason removeItem(ItemConfig itemConfig, ItemConfigDto itemConfigDto, Reason reason, Locale locale){
        if(reason != null){
            List<ItemConfigReason> itemConfigReasons = itemConfigReasonService.findByItemConfigIdAndStatus(itemConfig.getId(), Constants.STATUS_ACTIVE);
            ItemConfigReason itemConfigReasons1 = itemConfigReasons.stream().filter(icr->icr.getItemConfigId().equals(itemConfig.getId())
                    && icr.getReasonId().equals(reason.getReasonId())
                    && icr.getGravedad().equals(itemConfigDto.getGravedad().trim().toUpperCase())
            ).findFirst().orElse(null);
            if(itemConfigReasons1 == null){
                itemConfigDto.setComment(messageSource.getMessage(MessageKey.ITEM_IMPORT_RECORD_NOT_FOUND, null,locale));
            }else{
                itemConfigReasons1.setStatus(Constants.STATUS_INACTIVE);
                itemConfigReasons1.setLastUpdate(new Date());
                if(itemConfigReasons.size() == 1){
                    itemConfig.setStatus(Constants.STATUS_INACTIVE);
                    itemConfig.setLastUpdate(new Date());
                }
                itemConfigDto.setPass(true);
                return itemConfigReasons1;
            }
        }else{
            itemConfig.setStatus(Constants.STATUS_INACTIVE);
            itemConfig.setLastUpdate(new Date());
            itemConfigDto.setPass(true);
        }
        return null;
    }

    public List<ItemConfigDto> importItemOrRemove(List<ItemConfigDto> itemConfigDtoList, String[] channelListUsing, Locale locale, ActionChannelType actionType){
        List<ItemConfigDto> listItemOk = null;
        List<ItemConfig> itemConfigs = new ArrayList<>();
        Long id = 0l;

        String[] formatValidationUsing = getParamsByTypeAndCode(Constants.TYPE_ITEM_CONFIG_USING, Constants.CODE_VALIDATION_USING).getValue().split(";");
        String[] formatOkNokNaUsing = getParamsByTypeAndCode(Constants.TYPE_ITEM_CONFIG_USING, Constants.CODE_OK_NOK_NA_USING).getValue().split(";");

        List<Job> jobListEvaluation =  jobService.findJobsByAudit();
        List<Job> jobListGroup = jobService.findJobByParentIdInAndStatus(jobListEvaluation.stream().map(e->{
            return e.getJobId();
        }).collect(Collectors.toList()));
        List<Job> jobListItem = jobService.findJobByParentIdInAndStatus(jobListGroup.stream().map(g->{
            return g.getJobId();
        }).collect(Collectors.toList()));
        List<ChannelType> channelTypes = channelTypeSmartPhoneRepository.getPlansByCcAudit();
        List<Reason> reasons = reasonRepository.findReasonByStatus(Constants.STATUS_ACTIVE);
        List<AppParamsSmartPhone> gravedadApparams = appParamService.findAppParamsByTypeAndStatus(Constants.CODE_GRAVEDAD_USING);

        for(int i=0; i<itemConfigDtoList.size(); i++){
            id += 1;
            ItemConfigDto itemConfigDto = itemConfigDtoList.get(i);
            if(StringUtils.isEmpty(itemConfigDto.getChannelType())){
                itemConfigDto.setComment(messageSource.getMessage(MessageKey.ITEM_IMPORT_CHANNEL_IS_NOT_EMPTY, null,locale));
                continue;
            }
            String channelType = StringUtils.trim(itemConfigDto.getChannelType());
            ChannelType channelType1 = channelTypes.stream().filter(ct->ct.getName().equalsIgnoreCase(channelType)).findFirst().orElse(null);
            String evaluation = StringUtils.trim(itemConfigDto.getEvaluation());
            String group = StringUtils.trim(itemConfigDto.getGroup());
            String item = StringUtils.trim(itemConfigDto.getItem());
            AppParamsSmartPhone gravedad= null;
            if(StringUtils.isNotEmpty(itemConfigDto.getGravedad())){
                String gravedadName = itemConfigDto.getGravedad().trim().toUpperCase();
                gravedad = gravedadApparams.stream().filter(ap->gravedadName.equals(ap.getValue())).findFirst().orElse(null);
            }

            //<editor-fold defaultstate="collapsed" desc="Validate file import">
            if(StringUtils.isEmpty(itemConfigDto.getEvaluation())){
                itemConfigDto.setComment(messageSource.getMessage(MessageKey.ITEM_IMPORT_EVALUATION_IS_NOT_EMPTY, null,locale));
            }else if(StringUtils.isEmpty(itemConfigDto.getGroup())){
                itemConfigDto.setComment(messageSource.getMessage(MessageKey.ITEM_IMPORT_GROUP_IS_NOT_EMPTY, null,locale));
            }else if(StringUtils.isEmpty(itemConfigDto.getItem())){
                itemConfigDto.setComment(messageSource.getMessage(MessageKey.ITEM_IMPORT_ITEM_IS_NOT_EMPTY, null,locale));
            }else if(StringUtils.isEmpty(itemConfigDto.getNa())){
                itemConfigDto.setComment(messageSource.getMessage(MessageKey.ITEM_IMPORT_NA_IS_NOT_EMPTY, null,locale));
            }else if(StringUtils.isEmpty(itemConfigDto.getOk())){
                itemConfigDto.setComment(messageSource.getMessage(MessageKey.ITEM_IMPORT_OK_IS_NOT_EMPTY, null,locale));
            }else if(StringUtils.isEmpty(itemConfigDto.getNok())){
                itemConfigDto.setComment(messageSource.getMessage(MessageKey.ITEM_IMPORT_NOK_IS_NOT_EMPTY, null,locale));
            }else if(StringUtils.isEmpty(itemConfigDto.getPercent())){
                itemConfigDto.setComment(messageSource.getMessage(MessageKey.ITEM_IMPORT_PERCENT_IS_NOT_EMPTY, null,locale));
            }else if(StringUtils.isEmpty(itemConfigDto.getValidation())){
                itemConfigDto.setComment(messageSource.getMessage(MessageKey.ITEM_IMPORT_VALIDATION_IS_NOT_EMPTY, null,locale));
            }else if(!Pattern.compile("^(100|([1-9][0-9])|[0-9])$").matcher(itemConfigDto.getPercent()).matches()){
                itemConfigDto.setComment(messageSource.getMessage(MessageKey.ITEM_IMPORT_PERCENT_FORMAT_ERROR, null,locale));
            }else if(channelType1 == null || !Arrays.asList(channelListUsing).contains(channelType1.getChannelTypeId().toString())){
                itemConfigDto.setComment(messageSource.getMessage(MessageKey.ITEM_IMPORT_CHANNEL_FORMAT_ERROR, null,locale));
            }else if(!Arrays.asList(formatOkNokNaUsing).contains(itemConfigDto.getOk().toUpperCase())){
                itemConfigDto.setComment(messageSource.getMessage(MessageKey.ITEM_IMPORT_OK_FORMAT_ERROR, null,locale));
            }else if(!Arrays.asList(formatOkNokNaUsing).contains(itemConfigDto.getNok().toUpperCase())){
                itemConfigDto.setComment(messageSource.getMessage(MessageKey.ITEM_IMPORT_NOK_FORMAT_ERROR, null,locale));
            }else if(!Arrays.asList(formatOkNokNaUsing).contains(itemConfigDto.getNa().toUpperCase())){
                itemConfigDto.setComment(messageSource.getMessage(MessageKey.ITEM_IMPORT_NA_FORMAT_ERROR, null,locale));
            }else if(itemConfigDto.getNok().equalsIgnoreCase(formatOkNokNaUsing[1]) && (StringUtils.isNotEmpty(itemConfigDto.getReasonNok()) || StringUtils.isNotEmpty(itemConfigDto.getGravedad()))){
                itemConfigDto.setComment(messageSource.getMessage(MessageKey.ITEM_IMPORT_CHECK_REASONOK_GRAVEDAD_ARE_EMPTY, null,locale));
            }else if(itemConfigDto.getNok().equalsIgnoreCase(formatOkNokNaUsing[0]) && (StringUtils.isEmpty(itemConfigDto.getReasonNok()) || StringUtils.isEmpty(itemConfigDto.getGravedad()))){
                itemConfigDto.setComment(messageSource.getMessage(MessageKey.ITEM_IMPORT_CHECK_REASONOK_GRAVEDAD_ARE_NOT_EMPTY, null,locale));
            }else if(!(itemConfigDto.getOk().equalsIgnoreCase(formatOkNokNaUsing[0]) || itemConfigDto.getNok().equalsIgnoreCase(formatOkNokNaUsing[0]) || itemConfigDto.getNa().equalsIgnoreCase(formatOkNokNaUsing[0]))){
                itemConfigDto.setComment(messageSource.getMessage(MessageKey.ITEM_IMPORT_OK_NOK_NA_MUST_HAVE_YES, null,locale));
            }else if(!Arrays.asList(formatValidationUsing).contains(itemConfigDto.getValidation().toUpperCase())){
                itemConfigDto.setComment(messageSource.getMessage(MessageKey.ITEM_IMPORT_VALIDATION_FORMAT_ERROR, null,locale));
            }else if(gravedad == null && StringUtils.isNotEmpty(itemConfigDto.getGravedad())){
                itemConfigDto.setComment(messageSource.getMessage(MessageKey.ITEM_IMPORT_GRAVEDAD_FORMAT_ERROR, null,locale));
            }
            //</editor-fold>
            else {
                //validate reason
                Reason reason = null;
                if(!StringUtils.isEmpty(itemConfigDto.getReasonNok())){
                    reason = reasons.stream().filter(r->r.getName().equals(itemConfigDto.getReasonNok().trim().toUpperCase())).findFirst().orElse(null);
                    if(reason == null){
                        itemConfigDto.setComment(messageSource.getMessage(MessageKey.ITEM_IMPORT_REASON_NOK_FORMAT_EROR, null,locale));
                        continue;
                    }
                }
                //check duplicate data
                for (int k = 0 ; k < i; k++){
                    if(itemConfigDtoList.get(k).getComment() == null && checkDuplicate(itemConfigDto, itemConfigDtoList.get(k))){
                        itemConfigDto.setComment(messageSource.getMessage(MessageKey.ITEM_IMPORT_DUPLICATE_DATA, new String[]{String.valueOf(k+1)}, locale));
                        break;
                    }else if(itemConfigDtoList.get(k).getComment() == null && reason != null){
                        checkUpdate(itemConfigDto, itemConfigDtoList,i , k,locale);
                    }
                }
                if(!StringUtils.isEmpty(itemConfigDto.getComment())){
                    continue;
                }
                // Check evaluation does not exist
                Job jobEvaluationMap = jobListEvaluation.stream().filter(j -> j.getName().equalsIgnoreCase(evaluation)).findFirst().orElse(null);
                if(jobEvaluationMap == null){
                    itemConfigDto.setComment(messageSource.getMessage(MessageKey.ITEM_IMPORT_EVALUDATION_NOT_FOUND, null,locale));
                }else{
                    // Check group does not exist
                    Job jobGroupMap = jobListGroup.stream().filter(j -> j.getName().equalsIgnoreCase(group) && j.getParentId().equals(jobEvaluationMap.getJobId()))
                            .findFirst().orElse(null);
                    if(jobGroupMap == null){
                        itemConfigDto.setComment(messageSource.getMessage(MessageKey.ITEM_IMPORT_GROUP_NOT_FOUND, null,locale));
                    }else{
                        // Check item does not exist
                        Job jobItemMap = jobListItem.stream().filter(j -> j.getName().equalsIgnoreCase(item) && j.getParentId().equals(jobGroupMap.getJobId()))
                                .findFirst().orElse(null);
                        if(jobItemMap == null){
                            itemConfigDto.setComment(messageSource.getMessage(MessageKey.ITEM_IMPORT_ITEM_NOT_FOUND, null,locale));
                        }else{
                            // Check config item  in database
                            ItemConfig config = findByJobIdAndChannelTypeId(jobItemMap.getJobId(), channelType1.getChannelTypeId());
                            boolean compare = false;
                            if(config != null){
                                compare = equalItemConfigAndItemConfigDto(config, itemConfigDto);
                            }
                            //<editor-fold defaultstate="collapsed" desc="Save or update item">
                            for (int k = 0 ; k < i; k++){
                                //check item pass (!ispass)
                                if(!itemConfigDto.isPass()){
                                    checkUpdate(itemConfigDto, itemConfigDtoList,i , k,locale);
                                }
                            }
                            if (StringUtils.isEmpty(itemConfigDto.getComment())) {
                                switch (actionType){
                                    case ADD:
                                        // macth record databases or no
                                        if(compare == false){
                                            ItemConfig itemConfig = saveItemConfig(itemConfigDto, channelType1, config, jobItemMap, jobEvaluationMap,jobGroupMap, formatOkNokNaUsing);
                                            itemConfig.setIdc(id);
                                            itemConfigDto.setIdc(id);
                                            if(reason != null){
                                                itemConfig.setItemConfigReason(saveItemConfigReason(itemConfig, reason, gravedad));
                                            }
                                            itemConfigs.add(itemConfig);
                                            itemConfigDto.setPass(true);
                                        }else{
                                            config.setStatus(1l);
                                            config.setLastUpdate(new Date());
                                            if(reason != null){
                                                config.setItemConfigReason(saveItemConfigReason(config, reason, gravedad));
                                            }
                                            itemConfigs.add(config);
                                            itemConfigDto.setPass(true);
                                        }
                                        break;
                                    case DELETE:
                                        if(compare == false){
                                            itemConfigDto.setComment(messageSource.getMessage(MessageKey.ITEM_IMPORT_RECORD_NOT_FOUND, null,locale));
                                        }else{
                                            if(config.getStatus().equals(Constants.STATUS_INACTIVE)){
                                                itemConfigDto.setComment(messageSource.getMessage(MessageKey.ITEM_IMPORT_RECORD_NOT_FOUND, null,locale));
                                            }else{
                                                config.setItemConfigReason(removeItem(config, itemConfigDto, reason, locale));
                                                if(itemConfigDto.getComment() != null){
                                                    itemConfigs.add(config);
                                                }
                                            }
                                        }
                                        break;
                                }
                            }
                            //</editor-fold>
                        }
                    }
                }
            }
        }
        // check score and save config
        List<ItemConfig> listAll = itemConfigRepository.findAllItem();
        listItemOk = itemConfigDtoList.stream().filter(ic->ic.getComment() == null).collect(Collectors.toList());
        listAll.addAll(itemConfigs);
        listAll = listAll.stream().collect(Collectors
                .toMap(it->it.getJobId().toString() + it.getChannelTypeId().toString(), it->it, (existing, replacement) -> replacement))
                .values().stream().collect(Collectors.toList());
        //copy
        List<ItemConfig> finalListAll = listAll;
        //check score is 100% and filter itemconfig valid
        itemConfigs = itemConfigs.stream().map(it->{
            List<ItemConfig> checkList = finalListAll.stream().filter(i->it.getChannelTypeId().equals(i.getChannelTypeId()) && it.getEvaluationId().equals(i.getEvaluationId())).collect(Collectors.toList());
            for(ItemConfig i: checkList){
                it.setTotalPercent(it.getTotalPercent() + i.getPercent());
            }
            if(it.getTotalPercent() != 100.0f){
                return null;
            }
            return it;
        }).filter(Objects::nonNull).collect(Collectors.toList());
        // check itemDto invalid and comment
        List<Long> idcs = itemConfigs.stream().map(i->i.getIdc()).collect(Collectors.toList());
        listItemOk = listItemOk.stream().filter(i->idcs.contains(i.getIdc())).collect(Collectors.toList());
        itemConfigDtoList = itemConfigDtoList.stream().map(i->{
            if(i.getComment() == null){
                if(!idcs.contains(i.getIdc())){
                    i.setComment(messageSource.getMessage(MessageKey.ITEM_IMPORT_TOTAL_PERCENT_EVALUATION_MUST_REACH_100, null,locale));
                }
            }
            return i;
        }).collect(Collectors.toList());
        //save item config
        itemConfigs.stream().map(i->{
            ItemConfig itemConfig = itemConfigRepository.save(i);
            i.getItemConfigReason().setItemConfigId(itemConfig.getId());
            itemConfigReasonService.save(i.getItemConfigReason());
            return null;
        }).collect(Collectors.toList());
        return listItemOk;
    }

    public static void resposeValidationForFe(ConfigureItemDto configureItemDto){
        if(configureItemDto.getValidation().equals(Constants.VALIDATION_PHOTO)){
            configureItemDto.setPhoto(1l);
        }else if(configureItemDto.getValidation().equals(Constants.VALIDATION_FILE)){
            configureItemDto.setFile(1l);
        }else if(configureItemDto.getValidation().equals(Constants.VALIDATION_AUDIO)){
            configureItemDto.setAudio(1l);
        }else if(configureItemDto.getValidation().equals(Constants.VALIDATION_VIDEO)){
            configureItemDto.setVideo(1l);
        }
    }

    public ConfigureItemDto detailItemConfig(Long itemConfigId){
        Session smartphoneSession = null;
        ConfigureItemDto configureItemDto = new ConfigureItemDto();
        try {
            Map<String, Object> model = new HashMap<>();
            model.put("itemConfigId", itemConfigId);
            smartphoneSession = smartPhoneSessionFactory.openSession();
            Query query = getSessionFactory(ConfigureItemDto.class,"DetailItemConfig.sftl",model,smartphoneSession);
            query.setParameter("itemConfigId", itemConfigId);
            configureItemDto = (ConfigureItemDto) query.getSingleResult();
        } catch (Exception ex) {
            loggerFactory.error(ex.getMessage());
        } finally {
            if (smartphoneSession != null && smartphoneSession.isOpen()) {
                smartphoneSession.close();
            }
        }
        List<Reason> reasonList = getReasonDtoByItemConfig(itemConfigId);
        configureItemDto.setReasonList(reasonList);
        resposeValidationForFe(configureItemDto);
        return configureItemDto;
    }

    public FileDto downloadFileToFtpServer(String filePath) {
        FTPFileWriter ftpFileWriter = new FTPFileWriter();
        FileDto fileDto = new FileDto();
        String finalFilePath = null;
        OutputStream outputstream = null;
        String pdfData = "data:application/octet-stream;base64,";
        String pngData = "data:image/png;base64,";
        String jpegData = "data:image/jpeg;base64,";
        String mp3Data = "data:audio/mpeg;base64,";
        String mp4Data = "data:video/mpeg;base64,";
        int lastIndexOfFileSeparator = filePath.lastIndexOf("/");
        String pathFile = filePath.substring(0, lastIndexOfFileSeparator);
        String fileName = filePath.substring(lastIndexOfFileSeparator + 1);
        try {
            ftpFileWriter.open(FTPProperties.builder().server(ftpHost)
                    .port(Integer.parseInt(ftpPort)).username(ftpUser).password(ftpPass)
                    .autoStart(false).keepAliveTimeout(10).build());
            if (ftpFileWriter.isConnected()) {
                String tmpdir = fileNameFullFolder;
                File folder = new File(tmpdir);
                if (!folder.exists()) {
                    folder.mkdirs();
                }

                if (ftpFileWriter.checkFileExists(pathFile, fileName)) {
                    finalFilePath = tmpdir + File.separator + Calendar.getInstance().getTimeInMillis() + "_" + fileName;
                    loggerFactory.debug("===========FTPFilePath file path {}", filePath);
                    outputstream = new FileOutputStream(finalFilePath);
                    ftpFileWriter.loadFile(fileName, outputstream);
                    outputstream.flush();
                    loggerFactory.debug("===========FinalFilePath file path {}", finalFilePath);
                } else {
                    loggerFactory.error("File " + fileName + " not found on FTP server " + ftpHost);
                    loggerFactory.error("===========File not found on FTP server", filePath);
                }
            }
        } catch (Exception ftpException) {
            loggerFactory.error("FTP server " + ftpHost + ": " + ftpException.getMessage());
        } finally {
            try {
                if (outputstream != null) {
                    outputstream.close();
                }
            } catch (IOException ioe) {
                loggerFactory.error("Error in closing the Stream {}", ioe);
            }
            ftpFileWriter.close();
            if (StringUtils.isNotEmpty(finalFilePath)) {
                fileDto.setFileName(fileName);
                StringBuilder data = new StringBuilder();
                if (fileName.toLowerCase().endsWith("pdf")) {
                    data.append(pdfData);
                } else if (fileName.toLowerCase().endsWith("png")) {
                    data.append(pngData);
                } else if (fileName.toLowerCase().endsWith("jpg") || fileName.toLowerCase().endsWith("jpeg")) {
                    data.append(jpegData);
                }else if(fileName.toLowerCase().endsWith("mp3")){
                    data.append(mp3Data);
                } else if(fileName.toLowerCase().endsWith("mp4")){
                    data.append(mp4Data);
                }
                String base64 = FileUtils.base64encoder(finalFilePath);
                if (StringUtils.isNotEmpty(base64)) {
                    data.append(base64);
                    fileDto.setContent(data.toString());
                }
                org.apache.commons.io.FileUtils.deleteQuietly(new File(finalFilePath));
            }
        }
        return fileDto;
    }
}


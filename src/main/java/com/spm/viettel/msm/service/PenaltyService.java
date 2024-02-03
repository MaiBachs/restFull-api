
package com.spm.viettel.msm.service;


import com.spm.viettel.msm.dto.ChannelWithGroupDTO;
import com.spm.viettel.msm.dto.ItemConfigDto;
import com.spm.viettel.msm.dto.PenaltyDto;
import com.spm.viettel.msm.dto.UserTokenDto;
import com.spm.viettel.msm.dto.request.SearchPenaltyRequest;
import com.spm.viettel.msm.dto.response.SearchPenaltyResponse;
import com.spm.viettel.msm.repository.sm.ChannelTypeRepository;
import com.spm.viettel.msm.repository.sm.PositionRepository;
import com.spm.viettel.msm.repository.sm.entity.Position;
import com.spm.viettel.msm.repository.smartphone.PenaltyGravedadRepository;
import com.spm.viettel.msm.repository.smartphone.PenaltyRepository;
import com.spm.viettel.msm.repository.smartphone.entity.Job;
import com.spm.viettel.msm.repository.smartphone.entity.Penalty;
import com.spm.viettel.msm.repository.smartphone.entity.PenaltyGravedad;
import com.spm.viettel.msm.utils.Config;
import com.spm.viettel.msm.utils.Constants;
import com.spm.viettel.msm.utils.MessageKey;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import javax.persistence.Query;
import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PenaltyService {
    @Autowired
    private PenaltyRepository penaltyRepository;
    @Autowired
    private ItemConfigService itemConfigService;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private ChannelTypeService channelTypeService;
    @Autowired
    private JobService jobService;
    @Autowired
    private ChannelTypeRepository channelTypeRepository;
    @Autowired
    private PositionRepository positionRepository;
    @Autowired
    private PenaltyGravedadRepository penaltyGravedadRepository;
    @Autowired
    @Qualifier("smartphoneSessionFactory")
    private SessionFactory smartPhoneSessionFactory;
    @Value("${TEMPLATE_PATH}")
    private String templateFolder;
    @Value("${FILE_TEMP_UPLOAD_PATH}")
    private String fileNameFullFolder;

    public String buildImportResultFile(List<PenaltyDto> penaltyDtoList) {
        String templateFolder = this.templateFolder;
        String fileName = "import_penalty_result_" + Calendar.getInstance().getTimeInMillis() + ".xls";
        String fileNameFull = this.fileNameFullFolder + File.separator + fileName;
        String fileTemplate = "Template_import_penalty_result.xls";
        File templateFile = new File(templateFolder, fileTemplate);
        try {
            InputStream is = new FileInputStream(templateFile);
            File fileResult = new File(fileNameFull);
            OutputStream os = new FileOutputStream(fileResult);
            Context context = new Context();
            context.putVar("penalty", penaltyDtoList);
            JxlsHelper.getInstance().processTemplate(is, os, context);
            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String buildSearchResultFile(List<Penalty> penaltyList) {
        String templateFolder = Config.getPropValues("TEMPLATE_PATH");
        String fileName = "search_penalty_result_" + Calendar.getInstance().getTimeInMillis() + ".xls";
        String fileNameFull = Config.getPropValues("FILE_TEMP_UPLOAD_PATH") + File.separator + fileName;
        String fileTemplate = "Template_search_penalty_result.xls";
        File templateFile = new File(templateFolder, fileTemplate);
        try {
            InputStream is = new FileInputStream(templateFile);
            File fileResult = new File(fileNameFull);
            OutputStream os = new FileOutputStream(fileResult);
            Context context = new Context();
            context.putVar("penalty", penaltyList);
            JxlsHelper.getInstance().processTemplate(is, os, context);
            return fileNameFull;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Penalty findPenaltyByEvaluationIdAndUserTypeId(Long evaluationId, Long userTypeId){
        Session smartphoneSession = null;
        Penalty penalty = null;
        try{
            smartphoneSession = smartPhoneSessionFactory.openSession();
            String hql = "SELECT p FROM Penalty p WHERE p.evaluationId = :evaluationId AND p.userTypeId =:userTypeId";
            Query query = smartphoneSession.createQuery(hql);
            query.setParameter("evaluationId", evaluationId);
            query.setParameter("userTypeId", userTypeId);
            penalty = (Penalty) query.getResultList().stream().findFirst().orElse(null);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (smartphoneSession != null && smartphoneSession.isOpen()) {
                smartphoneSession.close();
            }
        }
        return penalty;
    }

    public List<Penalty> saveAll(List<Penalty> penalties){
        return penaltyRepository.saveAll(penalties);
    }

    public SearchPenaltyResponse searchPenalty(SearchPenaltyRequest request){
        SearchPenaltyResponse response = new SearchPenaltyResponse();
        Session smartphoneSession = null;
        List<Penalty> penalties = new ArrayList<>();
        try {
            Map<String, Object> model = new HashMap<>();
            model.put("evaluationId", request.getEvaluationId() );
            model.put("userTypeId",request.getUserTypeId());
            model.put("fromDate", request.getFromDate());
            model.put("toDate", request.getToDate());
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
            cfg.setObjectWrapper(new DefaultObjectWrapper(Configuration.VERSION_2_3_31));
            cfg.setClassForTemplateLoading(getClass(), "/sqls"); // Thay thế "/templates" bằng đường dẫn thực tế
            Template t = cfg.getTemplate("SearchPenalty.sftl");
            Writer out = new StringWriter();
            t.process(model, out);
            String sql = out.toString();
            smartphoneSession = smartPhoneSessionFactory.openSession();
            Query query = smartphoneSession.createNativeQuery(sql)
                    .addScalar("id", LongType.INSTANCE)
                    .addScalar("evaluationType", StringType.INSTANCE)
                    .addScalar("userTypeId", LongType.INSTANCE)
                    .addScalar("userType", StringType.INSTANCE)
                    .addScalar("createdBy", StringType.INSTANCE)
                    .addScalar("createdDate", DateType.INSTANCE)
                    .addScalar("updatedBy", StringType.INSTANCE)
                    .addScalar("updateDate", DateType.INSTANCE)
                    .addScalar("status", LongType.INSTANCE)
                    .addScalar("evaluationId", LongType.INSTANCE)
                    .setResultTransformer(Transformers.aliasToBean(Penalty.class));
            if(request.getEvaluationId() != null){
                query.setParameter("evaluationId", request.getEvaluationId());
            }
            if(request.getUserTypeId() != null){
                query.setParameter("userTypeId", request.getUserTypeId());
            }
            if(StringUtils.isNotEmpty(request.getFromDate())){
                query.setParameter("fromDate", request.getFromDate());
            }
            if(StringUtils.isNotEmpty(request.getToDate())){
                query.setParameter("toDate", request.getToDate());
            }
            if (request.isPaging()) {
                int pageSize = request.getPageSize() > 0 ? request.getPageSize() : Constants.PAGE_SIZE;
                int currentPage = request.getCurrentPage() > 0 ? request.getCurrentPage() : 0;
                String countSql = "SELECT COUNT(*) FROM ( " + sql + " )";
                Query queryCount = smartphoneSession.createNativeQuery(countSql);
                if(request.getEvaluationId() != null){
                    queryCount.setParameter("evaluationId", request.getEvaluationId());
                }
                if(request.getUserTypeId() != null){
                    queryCount.setParameter("userTypeId", request.getUserTypeId());
                }
                if(StringUtils.isNotEmpty(request.getFromDate())){
                    queryCount.setParameter("fromDate", request.getFromDate());
                }
                if(StringUtils.isNotEmpty(request.getToDate())){
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
                response.setCurrentPage(currentPage+1);
                query.setMaxResults(pageSize);
                query.setFirstResult(currentPage * pageSize);
            }
            penalties = query.getResultList();
            response.setPenaltyList(penalties);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (smartphoneSession != null && smartphoneSession.isOpen()) {
                smartphoneSession.close();
            }
        }
        return response;
    }

    public Penalty save(Penalty penalty){
        return penaltyRepository.save(penalty);
    }

    public static String downloadTemplate() {
        String fileName = "Template_import_penalty_export.xls";
        String fileNameFull = Config.getPropValues("TEMPLATE_PATH") + File.separator + fileName;
        return fileNameFull;
    }

    public List<Penalty> addPenalty(List<PenaltyDto> penaltyDtoList, List<Penalty> listPenaltyOk, Locale locale, UserTokenDto userLogined){
        String[] formatGravedadUsing = itemConfigService.getParamsByTypeAndCode(Constants.TYPE_ITEM_CONFIG_USING, Constants.CODE_GRAVEDAD_USING).getValue().split(";");
        String[] formatPenalidadUsing = itemConfigService.getParamsByTypeAndCode(Constants.TYPE_PENALTY_USING, Constants.CODE_PENALTY_PENALIDAD).getValue().split(";");
        List<Job> jobListEvaluation =  jobService.findJobsByAudit();
        List<Position> positions = positionRepository.findByStatus(1l);
        for(int i = 0; i  < penaltyDtoList.size(); i++){
            PenaltyDto penaltyDto = penaltyDtoList.get(i);
            String typeEvaluation = StringUtils.isNotEmpty(penaltyDto.getTypeEvaluation()) ? penaltyDto.getTypeEvaluation().trim().toUpperCase() : penaltyDto.getTypeEvaluation();
            String userType = StringUtils.isNotEmpty(penaltyDto.getUserType()) ? penaltyDto.getUserType().trim().toUpperCase() : penaltyDto.getUserType();
            String gravedad = StringUtils.isNotEmpty(penaltyDto.getGravedad()) ? penaltyDto.getGravedad().trim().toUpperCase() : penaltyDto.getGravedad();
            String penalidad = StringUtils.isNotEmpty(penaltyDto.getPenalidad()) ? penaltyDto.getPenalidad().trim().toUpperCase() : penaltyDto.getPenalidad();
            Position position = positions.stream().filter(p->Objects.equals(p.getPosName().toUpperCase(), userType)).findFirst().orElse(null);
            Job jobEvaluationMap = jobListEvaluation.stream().filter(j -> j.getName().equalsIgnoreCase(typeEvaluation)).findFirst().orElse(null);
            //<editor-fold defaultstate="collapsed" desc="Validate file import">
            if(StringUtils.isEmpty(penaltyDto.getTypeEvaluation())){
                penaltyDto.setComment(messageSource.getMessage(MessageKey.PENALTY_IMPORT_EVALUATION_IS_NOT_EMPTY, null,locale));
            }else if(StringUtils.isEmpty(penaltyDto.getUserType())){
                penaltyDto.setComment(messageSource.getMessage(MessageKey.PENALTY_IMPORT_USER_TYPE_IS_NOT_EMPTY, null,locale));
            }else if(StringUtils.isEmpty(penaltyDto.getGravedad())){
                penaltyDto.setComment(messageSource.getMessage(MessageKey.PENALTY_IMPORT_GRAVEDAD_IS_NOT_EMPTY, null,locale));
            }else if(StringUtils.isEmpty(penaltyDto.getPenalidad())){
                penaltyDto.setComment(messageSource.getMessage(MessageKey.PENALTY_IMPORT_PENALIDAD_IS_NOT_EMPTY, null,locale));
            }else if(jobEvaluationMap == null){
                penaltyDto.setComment(messageSource.getMessage(MessageKey.PENALTY_IMPORT_EVALUDATION_NOT_FOUND, null,locale));
            }else if(position == null){
                penaltyDto.setComment(messageSource.getMessage(MessageKey.PENALTY_IMPORT_USER_TYPE_INVALID, null,locale));
            }else if(!Arrays.asList(formatGravedadUsing).contains(gravedad)){
                penaltyDto.setComment(messageSource.getMessage(MessageKey.PENALTY_IMPORT_GRAVEDAD_INVALID, null,locale));
            }else if(!StringUtils.isNumeric(penalidad) && !Arrays.asList(formatPenalidadUsing).contains(penalidad)){
                penaltyDto.setComment(messageSource.getMessage(MessageKey.PENALTY_IMPORT_PENALIDAD_INVALID, null,locale));
            }else if(StringUtils.isNumeric(penalidad) && Long.valueOf(penalidad) < 0l){
                penaltyDto.setComment(messageSource.getMessage(MessageKey.PENALTY_IMPORT_PENALIDAD_INVALID, null,locale));
            }
            //</editor-fold>
            else{
                for(int j = 0; j < i; j++){
                    if(StringUtils.isEmpty(penaltyDtoList.get(i).getComment())){
                        if(checkDuplicatePenalty(penaltyDto, penaltyDtoList.get(j))){
                            penaltyDto.setComment(messageSource.getMessage(MessageKey.PENALTY_IMPORT_DUPLICATE_ROW, new String[]{String.valueOf(j+1)},locale));
                            break;
                        }
                    }
                }
                if(penaltyDto.getComment() != null){
                    continue;
                }
                for (int k = 0 ; k < i; k++){
                    //check item pass (!ispass)
                    if(!penaltyDto.isPass()){
                        checkUpdate(penaltyDto, penaltyDtoList,i , k,locale);
                    }
                }
                if(StringUtils.isEmpty(penaltyDto.getComment())){
                    Penalty penalty = findPenaltyByEvaluationIdAndUserTypeId(jobEvaluationMap.getJobId(), position.getPosId());
                    if(penalty != null){
                        penalty.setStatus(1l);
                        penalty.setUpdateDate(new Date());
                        penalty.setUpdatedBy(userLogined.getStaffCode());
                        listPenaltyOk.add(penalty);
                        penalty.setIndexp(listPenaltyOk.size()-1);

                        PenaltyGravedad penaltyGravedad = penaltyGravedadRepository.findByPenaltyIdAndGravedad(penalty.getId(), gravedad);
                        if(penaltyGravedad == null){
                            penaltyGravedad = new PenaltyGravedad();
                            penaltyGravedad.setGravedad(gravedad);
                            penaltyGravedad.setPenalidad(penalidad);
                            penaltyGravedad.setStatus(1l);
                        }else{
                            penaltyGravedad.setPenalidad(penalidad);
                            penaltyGravedad.setStatus(1l);
                        }
                        penalty.setPenaltyGravedad(penaltyGravedad);
                    }else {
                        Penalty penalty1 = new Penalty();
                        penalty1.setEvaluationId(jobEvaluationMap.getJobId());
                        penalty1.setUserType(userType);
                        penalty1.setUserTypeId(position.getPosId());
                        penalty1.setCreatedDate(new Date());
                        penalty1.setUpdateDate(new Date());
                        penalty1.setStatus(1l);
                        if(userLogined != null){
                            penalty1.setCreatedBy(userLogined.getStaffCode());
                            penalty1.setUpdatedBy(userLogined.getStaffCode());
                        }
                        listPenaltyOk.add(penalty1);
                        penalty1.setIndexp(listPenaltyOk.size()-1);

                        PenaltyGravedad penaltyGravedad = new PenaltyGravedad();
                        penaltyGravedad.setGravedad(gravedad);
                        penaltyGravedad.setPenalidad(penalidad);
                        penaltyGravedad.setStatus(1l);
                        penalty1.setPenaltyGravedad(penaltyGravedad);
                    }
                    penaltyDto.setPass(true);
                    penaltyDto.setIndexp(listPenaltyOk.size()-1);
                }
                //</editor-fold>
            }
        }
        List<Penalty> result = listPenaltyOk.stream().collect(Collectors.toList());
                // filter updated
        listPenaltyOk = listPenaltyOk.stream().map(p->{
            List<PenaltyDto> penaltyDtos = penaltyDtoList.stream().filter(p1->p1.getComment() != null && Objects.equals(p1.getIndexp(), p.getIndexp())).collect(Collectors.toList());
            if(penaltyDtos.size() == 0){
                return p;
            }else {
                return null;
            }
        }).filter(p2-> p2 != null).collect(Collectors.toList());
        // copy penaltyGravedad
        List<Penalty> savePenaltyGravedad = listPenaltyOk.stream().collect(Collectors.toList());
        // filter duplicate
        listPenaltyOk = listPenaltyOk.stream().collect(Collectors.toMap(p->Objects.hash(p.getUserTypeId(), p.getEvaluationId()), p->p, (p1, p2)->p1)).values().stream().collect(Collectors.toList());
        penaltyRepository.saveAll(listPenaltyOk);
        List<PenaltyGravedad> penaltyGravedads = new ArrayList<>();
        // set id penaltyId for penaltyGravedad
        List<Penalty> finalListPenaltyOk = listPenaltyOk;
        savePenaltyGravedad.stream().map(p->{
            Penalty penalty = finalListPenaltyOk.stream().filter(r->r.getUserTypeId().equals(p.getUserTypeId())
                    && r.getEvaluationId().equals(p.getEvaluationId())).findFirst().orElse(null);
            PenaltyGravedad penaltyGravedad = p.getPenaltyGravedad();
            penaltyGravedad.setPenaltyId(penalty.getId());
            penaltyGravedads.add(penaltyGravedad);
            return p;
        }).collect(Collectors.toList());

        penaltyGravedadRepository.saveAll(penaltyGravedads);
        return result;
    }

    public void checkUpdate(PenaltyDto penaltyDto, List<PenaltyDto> penaltyDtoList, int i, int k ,Locale locale){
        if((penaltyDto.getTypeEvaluation().trim().toUpperCase().equals(penaltyDtoList.get(k).getTypeEvaluation().trim().toUpperCase()))
                && (penaltyDto.getUserType().trim().toUpperCase().equals(penaltyDtoList.get(k).getUserType().trim().toUpperCase()))
                && penaltyDto.getGravedad().trim().toUpperCase().equals(penaltyDtoList.get(k).getGravedad().trim().toUpperCase())) {
            penaltyDtoList.get(k).setComment(messageSource.getMessage(MessageKey.PENALTY_IMPORT_UPDATED_BY_ROW, new String[]{String.valueOf(i+1)}, locale));
        }
    }

    public boolean checkDuplicatePenalty(PenaltyDto source, PenaltyDto target){
        boolean check = false;
        if(Objects.equals(source.getTypeEvaluation().trim().toUpperCase(), target.getTypeEvaluation().trim().toUpperCase())
                && Objects.equals(source.getUserType().trim().toUpperCase(), target.getUserType().trim().toUpperCase())
                && Objects.equals(source.getGravedad().trim().toUpperCase(), target.getGravedad().trim().toUpperCase())
                && Objects.equals(source.getPenalidad().trim().toUpperCase() , target.getPenalidad().trim().toUpperCase())){
            check = true;
        }
        return check;
    }

    public Penalty detail(Long penaltyId){
        Penalty penalty = penaltyRepository.findByPenaltyId(penaltyId);
        List<PenaltyGravedad> penaltyGravedads = penaltyGravedadRepository.findByPenaltyIdAndStatus(penaltyId, 1l);
        penalty.setDetailData(penaltyGravedads);
        return penalty;
    }

//    public void removePenalty(List<PenaltyDto> penaltyDtoList, List<Penalty> listPenaltyRemoveOk, Locale locale, String[] channelListUsing, String[] userTypeUsing){
//        for(int i = 0; i < penaltyDtoList.size(); i++){
//            PenaltyDto penaltyDto = penaltyDtoList.get(i);
//            String typeEvaluation = penaltyDto.getTypeEvaluation().trim().toUpperCase();
//            String userType = penaltyDto.getUserType().trim().toUpperCase();
//            //<editor-fold defaultstate="collapsed" desc="Validate file import">
//            if(StringUtils.isEmpty(penaltyDto.getTypeEvaluation())){
//                penaltyDto.setComment(messageSource.getMessage(MessageKey.PENALTY_IMPORT_EVALUATION_IS_NOT_EMPTY, null,locale));
//            }else if(StringUtils.isEmpty(penaltyDto.getUserType())){
//                penaltyDto.setComment((messageSource.getMessage(MessageKey.PENALTY_IMPORT_USER_TYPE_IS_NOT_EMPTY, null,locale)));
//            }else if(!Arrays.asList(userTypeUsing).contains(userType)){
//                penaltyDto.setComment((messageSource.getMessage(MessageKey.PENALTY_IMPORT_USER_TYPE_INVALID, null,locale)));
//            }
//            //</editor-fold>
//            else{
//                List<Job> jobListEvaluation = jobService.findJobsByChannelTypeIdAndParentId(channelType.getChannelTypeId(),null);
//                List<Job> jobjobListEvaluationMap = jobListEvaluation.stream().filter(j -> j.getName().equalsIgnoreCase(typeEvaluation))
//                        .collect(Collectors.toList());
//                if(jobjobListEvaluationMap.size() == 0){
//                    penaltyDto.setComment(messageSource.getMessage(MessageKey.PENALTY_IMPORT_EVALUDATION_NOT_FOUND, null,locale));
//                }else{
//                    for(int j = 0; j < listPenaltyRemoveOk.size(); j++){
//                        if(listPenaltyRemoveOk.get(j).getEvaluationType().trim().equalsIgnoreCase(penaltyDto.getTypeEvaluation().trim())
//                                && listPenaltyRemoveOk.get(j).getUserType().trim().equalsIgnoreCase(penaltyDto.getUserType().trim())){
//                            penaltyDto.setComment((messageSource.getMessage(MessageKey.PENALTY_IMPORT_DUPLICATE_ROW, new String[]{String.valueOf(j+1)},locale)));
//                            break;
//                        }
//                    }
//                    Long evaluationId = jobjobListEvaluationMap.get(0).getJobId();
//                    String userTypes = penaltyDto.getUserType().toUpperCase();;
//                    Penalty penalty = findPenaltyByEvaluationIdAndUserType(evaluationId, userTypes);
//                    if(penalty == null){
//                        penaltyDto.setComment((messageSource.getMessage(MessageKey.PENALTY_IMPORT_PENALTY_NOT_FOUND, null,locale)));
//                    }
//                    if(StringUtils.isEmpty(penaltyDto.getComment())){
//                        penalty.setStatus(0l);
//                        listPenaltyRemoveOk.add(penalty);
//                    }
//                }
//            }
//        }
//    }
}

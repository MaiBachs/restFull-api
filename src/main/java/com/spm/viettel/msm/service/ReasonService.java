package com.spm.viettel.msm.service;

import com.spm.viettel.msm.dto.request.ReasonRequest;
import com.spm.viettel.msm.factory.ResponseFactory;
import com.spm.viettel.msm.repository.smartphone.ReasonRepository;
import com.spm.viettel.msm.repository.smartphone.entity.Reason;
import com.spm.viettel.msm.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ReasonService {

    public static final String NOT_NULL = "NOT NULL";
    @Autowired
    private ReasonRepository reasonRepository;

    @Autowired
    private ResponseFactory responseFactory;

    public List<Reason> searchReasonByName(ReasonRequest request) {
        return reasonRepository.searchReasonByName(request.getName());
    }

    public ResponseEntity<?> addNewReason(ReasonRequest request) {
        Reason reason = new Reason();
        if(request.getName().isEmpty() && request.getName() == null){
            return responseFactory.error(HttpStatus.OK, String.valueOf(HttpStatus.OK.value()), NOT_NULL);
        }
        if(request.getCode().isEmpty() && request.getCode() == null){
            return responseFactory.error(HttpStatus.OK, String.valueOf(HttpStatus.OK.value()), NOT_NULL);
        }
        reason.setCode(request.getCode());
        reason.setName(request.getName());
        reason.setNote(request.getNote());
        reason.setCreatedDate(new Date());
        reason.setStatus(Constants.STATUS_ACTIVE);
        reasonRepository.save(reason);
        return new ResponseEntity<>(reason,HttpStatus.OK);
    }

    public ResponseEntity<?> editReason(ReasonRequest request, Long reasonId) {
        Reason reason = reasonRepository.findById(reasonId).orElse(null);
        if(request.getName().isEmpty() && request.getName() == null){
            return responseFactory.error(HttpStatus.OK, String.valueOf(HttpStatus.OK.value()), NOT_NULL);
        }
        if(request.getCode().isEmpty() && request.getCode() == null){
            return responseFactory.error(HttpStatus.OK, String.valueOf(HttpStatus.OK.value()), NOT_NULL);
        }
        reason.setCode(request.getCode());
        reason.setName(request.getName());
        reason.setNote(request.getNote());
        reason.setLastUpdate(new Date());
        reason.setStatus(request.getStatus());
        reasonRepository.save(reason);
        return new ResponseEntity<>(reason, HttpStatus.OK);
    }
}

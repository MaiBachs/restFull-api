package com.spm.viettel.msm.service;

import com.spm.viettel.msm.repository.sm.OtherObjectTypeRepository;
import com.spm.viettel.msm.repository.sm.entity.OtherObjectType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OtherObjectTypeService {
    @Autowired
    private OtherObjectTypeRepository otherObjectTypeRepository;

    public List<OtherObjectType> getListOtherObjectType() {
        return otherObjectTypeRepository.getListOtherObjectType();
    }

}

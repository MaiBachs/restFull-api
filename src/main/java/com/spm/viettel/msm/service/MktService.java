package com.spm.viettel.msm.service;

import com.spm.viettel.msm.repository.sm.MktSizeRepository;
import com.spm.viettel.msm.repository.sm.MktUnitTypeRepository;
import com.spm.viettel.msm.repository.sm.entity.MktSize;
import com.spm.viettel.msm.repository.sm.entity.MktUnitType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MktService {

    private final MktUnitTypeRepository mktUnitTypeRepository;
    private final MktSizeRepository mktSizeRepository;

    @Autowired
    public MktService(MktUnitTypeRepository mktUnitTypeRepository, MktSizeRepository mktSizeRepository) {
        this.mktUnitTypeRepository = mktUnitTypeRepository;
        this.mktSizeRepository = mktSizeRepository;
    }

    public List<MktUnitType> getListMktUnitTypes() {
        return mktUnitTypeRepository.findMktUnitTypeByStatus(1);
    }

    public List<MktSize> getListMktSize() {
        return mktSizeRepository.getMktSizesByStatus(1);
    }

}

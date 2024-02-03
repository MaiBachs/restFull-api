package com.spm.viettel.msm.repository.sm;

import com.slyak.spring.jpa.GenericJpaRepository;
import com.slyak.spring.jpa.TemplateQuery;
import com.spm.viettel.msm.dto.StockInfoDTO;
import com.spm.viettel.msm.dto.StockTypeDTO;
import com.spm.viettel.msm.repository.sm.entity.StockType;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StockRepository extends GenericJpaRepository<StockType,Long> {

    @TemplateQuery(value = "getListStockType")
    List<StockTypeDTO> getListStockType();
}

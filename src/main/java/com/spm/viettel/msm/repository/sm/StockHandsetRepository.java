package com.spm.viettel.msm.repository.sm;

import com.slyak.spring.jpa.GenericJpaRepository;
import com.slyak.spring.jpa.TemplateQuery;
import com.spm.viettel.msm.dto.StockInfoDTO;
import com.spm.viettel.msm.repository.sm.entity.StockHandset;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StockHandsetRepository extends GenericJpaRepository<StockHandset,Long> {
    @TemplateQuery()
    List<StockInfoDTO> searchListStockInfos(@Param("nameTable") String nameTable,
                                            @Param("owner_id") Long owner_id,
                                            @Param("stockName") String stockName,
                                            @Param("typeId") Long typeId,
                                            @Param("channelIsVTUnit") int channelIsVTUnit);
}

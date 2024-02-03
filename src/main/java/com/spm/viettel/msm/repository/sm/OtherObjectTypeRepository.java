package com.spm.viettel.msm.repository.sm;

import com.slyak.spring.jpa.GenericJpaRepository;
import com.spm.viettel.msm.repository.sm.entity.OtherObjectType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface OtherObjectTypeRepository extends GenericJpaRepository<OtherObjectType, Long> {

    @Query("SELECT ob FROM OtherObjectType ob WHERE ob.status = 1 AND ob.mkt=1")
    List<OtherObjectType> getListOtherObjectType();
}

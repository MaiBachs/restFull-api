package com.spm.viettel.msm.repository.sm;

import com.slyak.spring.jpa.GenericJpaRepository;
import com.slyak.spring.jpa.TemplateQuery;
import com.spm.viettel.msm.repository.sm.entity.MapChannelSurveyStaff;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MapChannelSurveyStaffRepository extends GenericJpaRepository<MapChannelSurveyStaff, Long> {
    @TemplateQuery
    List<MapChannelSurveyStaff> searchSurveyConfig(@Param("branchId") Long branchId
            ,@Param("bcId") Long bcId
            ,@Param("userId") Long userId
            ,@Param("channelCode") String channelCode
            ,@Param("status") Long status
            ,@Param("createdDate") String fromDate
            ,@Param("createdDate") String toDate);

    @TemplateQuery
    MapChannelSurveyStaff findToProcessExcelData(@Param("branchId") Long branchId
            , @Param("bcId") Long bcId
            , @Param("userId") Long userId
            , @Param("channelId") Long channelId
            , @Param("start_vote_date") String start_vote_date);
}

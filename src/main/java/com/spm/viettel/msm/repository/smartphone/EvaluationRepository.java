package com.spm.viettel.msm.repository.smartphone;

import com.spm.viettel.msm.repository.smartphone.entity.MapChannelTypeCheckList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Repository
public interface EvaluationRepository extends JpaRepository<MapChannelTypeCheckList,Long>{
    MapChannelTypeCheckList getMapChannelTypeCheckListById(long id);

    @Modifying
    @Transactional(value = "smartphoneTransactionManager")
    @Query("UPDATE MapChannelTypeCheckList " +
            "SET " +
            "    quantityPerMonth = :quantityPerMonth, " +
            "    approvalScore = :approvalScore, " +
            "    dateEvaluation1 = :dateEvaluation1, " +
            "    dateEvaluation2 = :dateEvaluation2, " +
            "    dateEvaluation3 = :dateEvaluation3, " +
            "    dateEvaluation4 = :dateEvaluation4, " +
            "    updatedBy = :updatedBy, " +
            "    updatedDate = :updatedDate " +
            "WHERE " +
            "    id = :id")
    int updateEvaluation(@Param("quantityPerMonth") Long quantityPerMonth,
                                             @Param("approvalScore") Float approvalScore,
                                             @Param("dateEvaluation1") Date dateEvaluation1,
                                             @Param("dateEvaluation2") Date dateEvaluation2,
                                             @Param("dateEvaluation3") Date dateEvaluation3,
                                             @Param("dateEvaluation4") Date dateEvaluation4,
                                             @Param("updatedBy") String updatedBy,
                                             @Param("updatedDate") Date updatedDate,
                                             @Param("id") Long id);


    @Modifying
    @Transactional(value = "smartphoneTransactionManager")
    @Query(value = "INSERT INTO MAP_CHANNEL_TYPE_CHECK_LIST " +
            "(BR_ID, BR_CODE, CHANNEL_TYPE_ID, JOB_ID, QUANTITY_PER_MONTH, " +
            "APPROVAL_SCORE, DATE_EVALUATION_1, DATE_EVALUATION_2, DATE_EVALUATION_3, DATE_EVALUATION_4, " +
            "CREATED_BY, CREATED_DATE, UPDATED_BY, UPDATED_DATE) " +
            "VALUES " +
            "(:branchId, :branchCode, :channelTypeId, :jobId, :quantityPerMonth, " +
            ":approvalScore, :dateEvaluation1, :dateEvaluation2, :dateEvaluation3, :dateEvaluation4, " +
            ":createdBy, :createdDate, :updatedBy, :updatedDate)",
            nativeQuery = true)
    int insertMapChannelTypeCheckList(@Param("branchId") Long branchId,
                                       @Param("branchCode") String branchCode,
                                       @Param("channelTypeId") Long channelTypeId,
                                       @Param("jobId") Long jobId,
                                       @Param("quantityPerMonth") Long quantityPerMonth,
                                       @Param("approvalScore") Float approvalScore,
                                       @Param("dateEvaluation1") String dateEvaluation1,
                                       @Param("dateEvaluation2") String dateEvaluation2,
                                       @Param("dateEvaluation3") String dateEvaluation3,
                                       @Param("dateEvaluation4") String dateEvaluation4,
                                       @Param("createdBy") String createdBy,
                                       @Param("createdDate") String createdDate,
                                       @Param("updatedBy") String updatedBy,
                                       @Param("updatedDate") String updatedDate);

    @Transactional
    @Modifying
    @Query("DELETE FROM MapChannelTypeCheckList WHERE id = :id")
    void deleteMapChannelTypeCheckListById(@Param("id") Long id);
}

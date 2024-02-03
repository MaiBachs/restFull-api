package com.spm.viettel.msm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EvaluationManageDTO {
    private Long id;

    private Long evaluationId;

    private String evaluationName;

    private String branchId;

    private String branchCode;

    private String typeChannel;

    private String channelCode;

    private Long channelId;

    private String auditorCode;

    private Long auditorId;

    private Long score;

    private Date scheduledDate;

    private Long statusEvaluation;

    private Date visitTime;

    private Date reviewDate;

    private String period;

    private String actionPlan;

    private String reviewingUser;

    private String statusActionPlan;

    private String checkListResultComment;

    private Long status;

    private String hinhAnhURL;

    public void convertPeriod(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.getScheduledDate());
        Integer month = calendar.get(Calendar.MONTH) + 1;
        if(month == 1){
            setPeriod("Enero");
        }else if(month == 2){
            setPeriod("Febrero");
        }else if(month == 3){
            setPeriod("Marzo");
        }else if(month == 4){
            setPeriod("Abril");
        }else if(month == 5){
            setPeriod("Puede");
        }else if(month == 6){
            setPeriod("Junio");
        }else if(month == 7){
            setPeriod("Julio");
        }else if(month == 8){
            setPeriod("Agosto");
        }else if(month == 9){
            setPeriod("Septiembre");
        }else if(month == 10){
            setPeriod("Octubre");
        }else if(month == 11){
            setPeriod("Noviembre");
        }else {
            setPeriod("Diciembre");
        }
    }

}

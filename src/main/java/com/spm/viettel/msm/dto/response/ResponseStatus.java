package com.spm.viettel.msm.dto.response;

import com.spm.viettel.msm.factory.IResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ResponseStatus implements IResponseStatus {
	private String code;
	private String message;
}

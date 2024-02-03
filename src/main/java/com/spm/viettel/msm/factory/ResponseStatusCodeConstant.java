package com.spm.viettel.msm.factory;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseStatusCodeConstant {
	public static final String SUCCESS = "000001";
	public static final String GENERAL_ERROR = "000002";
	public static final String FIELD_MISSING = "000003";
	public static final String UNAUTHORIZED = "000004";

	public static final String FILE_EMPTY = "000005";
	public static final String FILE_FORMAT = "000006";
	public static final String FIELD_TOO_LARGE = "000007";
	public static final String NOT_FOUND = "000008";
	public static final String RECORD = "000009";
}

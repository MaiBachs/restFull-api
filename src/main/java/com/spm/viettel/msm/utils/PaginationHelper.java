package com.spm.viettel.msm.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;

public class PaginationHelper {
    public static final int DEFAULT_RECORD_PER_PAGE = 20;
    public static final int DEFAULT_CURRENT_PAGE = 0;
    public static final Direction DEFAULT_SORT_DIRECTION;
    public static final String DEFAULT_SORT_PROPERTY = "id";
    public static final String ASC = "asc";

    private PaginationHelper() {
    }

//    public static PageRequest generatePageRequest(final String currentPage, final String pageSize, final String sortColumn, final String sortType) {
//        String property = (String) Optional.ofNullable(sortColumn).orElse("id");
//        Direction direction = Optional.ofNullable(sortType).isPresent() && ((String)Optional.of(sortType).get()).equals("asc") ? Direction.ASC : DEFAULT_SORT_DIRECTION;
//        return PageRequest.of(getPage(currentPage), getSize(pageSize), new Sort(direction, new String[]{StringHelper.underscoreToUppercase(property)}));
//    }

    public static PageRequest generatePageRequest(final int currentPage, final int pageSize) {
        return PageRequest.of(currentPage, pageSize);
    }

//    public static PageRequest generatePageRequest(final Map<String, String> params) {
//        return generatePageRequest((String)params.get("page"), (String)params.get("offset"), (String)params.get("sort_column"), (String)params.get("sort_type"));
//    }

    static {
        DEFAULT_SORT_DIRECTION = Direction.DESC;
    }
}

package com.amnex.rorsync.util;

import org.springframework.stereotype.Component;

@Component
public class Constants {

    public static final Long SUCCESS_CODE = 200L;
    public static final Long NOT_FOUND_CODE = 400L;
    public static final Long INTERNAL_ERROR_CODE = 500L;
    public static final String EXTERNAL_API_KEY = "ROR_SYNC_KEY";
    public static final String SUCCESS = "success";
    public static final String FAILED = "failed";
    public static final String No_DATA_FOUND = "no data found";
    public final static String INVALID_PASSWORD = "Invalid username or password.";
    public final static String SOMETHING_WENT_WRONG = "something went wrong.";
}

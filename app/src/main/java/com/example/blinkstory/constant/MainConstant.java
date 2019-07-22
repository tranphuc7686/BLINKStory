package com.example.blinkstory.constant;

public class MainConstant {
    public static final String BASE_URL = "http://10.10.10.187:5000/api/common";
    public static final int APPLICATION = 1;
    public static final String GET_LIST_CATEGORY_URL = BASE_URL+"/categories/"+APPLICATION;
    public static final String  GET_LIST_ELEMENT_URL = BASE_URL+"/datas/";
    public static final int VIEW_TYPE_ITEM = 0;
    public static final int VIEW_TYPE_LOADING = 1;
    public static final String CATEGORY_ID_EXTRA = "categoryidextra";
    public static final String CATEGORY_THUBMAIL_EXTRA = "categorythubmailextra";
    public static final String CATEGORY_NAME_EXTRA = "categorynamelextra";
}

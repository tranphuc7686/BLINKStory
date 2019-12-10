package com.example.blinkstory.constant;

import java.util.Random;

public class MainConstant {
    public static final String BASE_URL = "http://52.43.108.161/api/common";
    public static final int APPLICATION = 2;
    public static final String GET_LIST_CATEGORY_URL = BASE_URL+"/categories/"+APPLICATION;
    public static final String  GET_LIST_ELEMENT_URL = BASE_URL+"/datas/";
    public static final int VIEW_TYPE_ITEM = 0;
    public static final int VIEW_TYPE_LOADING = 1;
    public static final String CATEGORY_ID_EXTRA = "categoryidextra";
    public static final String CATEGORY_THUBMAIL_EXTRA = "categorythubmailextra";
    public static final String CATEGORY_NAME_EXTRA = "categorynamelextra";
    public static final String URL_UPLOAD_DATA = BASE_URL+"/datas/upload";
    public static final String URL_ADD_DATA = BASE_URL;
    public static final int TYPE_IMAGE = 0;
    public static final int TYPE_VIDEO = 1;
    //ads
    public static final String APP_ADS = "ca-app-pub-8810699871819525~2668960367";
    public static final String XENKE_ADS = "ca-app-pub-8810699871819525/9042797027" ;
    public static final String TRATHUONG_ADS = "ca-app-pub-8810699871819525/3790470342";
    public static final String BANNER_ADS = "ca-app-pub-8810699871819525/5550629796";
    public static final String TEST_DEVIED = "A959664D54C3C716F0FC9E9D01A11F1D";

    public static int generateRandomIntIntRange(int min, int max) {
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

}

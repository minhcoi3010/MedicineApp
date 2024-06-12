package com.example.btlandroidapi.retrofit;

import com.example.btlandroidapi.MedicineInterface;

public class MedicineUtils {
    public static final String BASE_URL = "http://192.168.29.106:5000";

    public static MedicineInterface getBookService(){
        return RetrofitClient.getClient(BASE_URL).create(MedicineInterface.class);
    }

}

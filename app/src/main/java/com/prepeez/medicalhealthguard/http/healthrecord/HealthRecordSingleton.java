package com.prepeez.medicalhealthguard.http.healthrecord;

import static com.prepeez.medicalhealthguard.constants.keyConst.BASE_URL;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.prepeez.medicalhealthguard.constants.Const;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HealthRecordSingleton {
    private HealthRecordInterface healthRecordInterface = null;

    public HealthRecordInterface getHealthRecordInterface() {
        Gson gson = new GsonBuilder().setLenient().create();
        if (healthRecordInterface == null){
            Retrofit retrofit =new  Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            healthRecordInterface = retrofit.create(HealthRecordInterface.class);

        }

        return healthRecordInterface;
    }
}

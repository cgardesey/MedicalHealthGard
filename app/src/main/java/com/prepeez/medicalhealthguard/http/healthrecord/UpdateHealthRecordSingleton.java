package com.prepeez.medicalhealthguard.http.healthrecord;

import static com.prepeez.medicalhealthguard.constants.keyConst.BASE_URL;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.prepeez.medicalhealthguard.constants.Const;
import com.prepeez.medicalhealthguard.http.patient.UpdatePatientInterface;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UpdateHealthRecordSingleton {
    private UpdateHealthRecordInterface healthRecordsInterface = null;

    public UpdateHealthRecordInterface updateRecordsInterface() {
        Gson gson = new GsonBuilder().setLenient().create();
        if (healthRecordsInterface == null){
            Retrofit retrofit =new  Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            healthRecordsInterface = retrofit.create(UpdateHealthRecordInterface.class);

        }

        return healthRecordsInterface;
    }
}

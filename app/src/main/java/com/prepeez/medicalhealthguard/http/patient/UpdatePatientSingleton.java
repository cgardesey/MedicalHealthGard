package com.prepeez.medicalhealthguard.http.patient;

import static com.prepeez.medicalhealthguard.constants.keyConst.BASE_URL;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.prepeez.medicalhealthguard.constants.Const;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UpdatePatientSingleton {
    private UpdatePatientInterface patientsInterface = null;

    public UpdatePatientInterface updateRecordsInterface() {
        Gson gson = new GsonBuilder().setLenient().create();
        if (patientsInterface == null){
            Retrofit retrofit =new  Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            patientsInterface = retrofit.create(UpdatePatientInterface.class);

        }

        return patientsInterface;
    }
}

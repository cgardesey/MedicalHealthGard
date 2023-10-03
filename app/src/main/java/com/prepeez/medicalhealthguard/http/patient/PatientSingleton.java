package com.prepeez.medicalhealthguard.http.patient;

import static com.prepeez.medicalhealthguard.constants.keyConst.BASE_URL;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.prepeez.medicalhealthguard.constants.Const;
import com.prepeez.medicalhealthguard.http.healthrecord.HealthRecordInterface;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PatientSingleton {
    private PatientInterface patientInterface = null;

    public PatientInterface getPatientInterface() {
        Gson gson = new GsonBuilder().setLenient().create();
        if (patientInterface == null){
            Retrofit retrofit =new  Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            patientInterface = retrofit.create(PatientInterface.class);

        }

        return patientInterface;
    }
}

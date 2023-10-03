package com.prepeez.medicalhealthguard.http.patient;

import static com.prepeez.medicalhealthguard.constants.keyConst.BASE_URL;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.prepeez.medicalhealthguard.constants.Const;
import com.prepeez.medicalhealthguard.http.healthrecord.GetHealthRecordsInterface;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FetchPatientsSingleton {
    private GetPatientsInterface patientsInterface = null;

    public GetPatientsInterface getPatientsInterface() {
        Gson gson = new GsonBuilder().setLenient().create();
        if (patientsInterface == null){
            Retrofit retrofit =new  Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            patientsInterface = retrofit.create(GetPatientsInterface.class);

        }

        return patientsInterface;
    }
}

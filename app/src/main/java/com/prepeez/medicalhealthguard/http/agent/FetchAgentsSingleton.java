package com.prepeez.medicalhealthguard.http.agent;

import static com.prepeez.medicalhealthguard.constants.keyConst.BASE_URL;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.prepeez.medicalhealthguard.constants.Const;
import com.prepeez.medicalhealthguard.http.patient.GetPatientsInterface;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FetchAgentsSingleton {
    private GetAgentsInterface agentsInterface = null;

    public GetAgentsInterface getAgentsInterface() {
        Gson gson = new GsonBuilder().setLenient().create();
        if (agentsInterface == null){
            Retrofit retrofit =new  Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            agentsInterface = retrofit.create(GetAgentsInterface.class);

        }

        return agentsInterface;
    }
}

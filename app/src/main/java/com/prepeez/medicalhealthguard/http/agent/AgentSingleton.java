package com.prepeez.medicalhealthguard.http.agent;

import static com.prepeez.medicalhealthguard.constants.keyConst.BASE_URL;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.prepeez.medicalhealthguard.constants.Const;
import com.prepeez.medicalhealthguard.http.patient.PatientInterface;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AgentSingleton {
    private AgentInterface agentInterface = null;

    public AgentInterface getAgentInterface() {
        Gson gson = new GsonBuilder().setLenient().create();
        if (agentInterface == null){
            Retrofit retrofit =new  Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            agentInterface = retrofit.create(AgentInterface.class);

        }

        return agentInterface;
    }
}

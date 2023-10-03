package com.prepeez.medicalhealthguard.http.agent;

import com.prepeez.medicalhealthguard.pojo.Agent;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UpdateAgentInterface {

    //@FormUrlEncoded
    @POST("2ctv/mhg/public/api/update_agent/{agentid}")
    Call<Agent> update(@Path("agentid") String agentid,
                       @Body Agent agent
    );
}

package com.prepeez.medicalhealthguard.http.agent;

import com.prepeez.medicalhealthguard.pojo.Agent;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GetAgentsInterface {
    @GET("2ctv/mhg/public/api/agents")
    Call<List<Agent>> fetchAll();
}

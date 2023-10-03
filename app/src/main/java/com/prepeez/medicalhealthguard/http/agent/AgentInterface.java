package com.prepeez.medicalhealthguard.http.agent;


import com.prepeez.medicalhealthguard.pojo.Patient;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AgentInterface {
    @FormUrlEncoded
    @POST("2ctv/mhg/public/api/save_agent")
    Call<Patient> addRecord(
            @Field("agentid") String agentid,
            @Field("zone") String zone,
            @Field("picture") String picture,
            @Field("title") String title,
            @Field("firstname") String firstname,
            @Field("lastname") String lastname,
            @Field("othername") String othername,
            @Field("gender") String gender,
            @Field("dateofbirth") String dateofbirth,
            @Field("contact") String contact,
            @Field("address") String address,
            @Field("dateofemployment") String dateofemployment,
            @Field("active") int active
    );
}

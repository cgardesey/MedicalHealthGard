package com.prepeez.medicalhealthguard.http.healthrecord;

import com.prepeez.medicalhealthguard.pojo.HealthRecord;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GetHealthRecordsInterface {
    @GET("2ctv/mhg/public/api/health_records")
    Call<List<HealthRecord>> fetchAll();
}

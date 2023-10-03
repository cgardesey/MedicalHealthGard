package com.prepeez.medicalhealthguard.http.healthrecord;

import com.prepeez.medicalhealthguard.pojo.HealthRecord;
import com.prepeez.medicalhealthguard.pojo.Patient;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UpdateHealthRecordInterface {

    //@FormUrlEncoded
    @POST("2ctv/mhg/public/api/update_health_record/{healthrecordid}")
    Call<HealthRecord> update(@Path("healthrecordid") String healthrecordid,
                              @Body HealthRecord healthRecord
    );
}

package com.prepeez.medicalhealthguard.http.patient;

import com.prepeez.medicalhealthguard.pojo.Patient;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UpdatePatientInterface {

    //@FormUrlEncoded
    @POST("2ctv/mhg/public/api/update_patient/{patientid}")
    Call<Patient> update(@Path("patientid") String patientid,
                               @Body Patient patient
                               );
}

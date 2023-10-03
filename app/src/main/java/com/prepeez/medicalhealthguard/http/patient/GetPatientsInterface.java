package com.prepeez.medicalhealthguard.http.patient;

import com.prepeez.medicalhealthguard.pojo.HealthRecord;
import com.prepeez.medicalhealthguard.pojo.Patient;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GetPatientsInterface {
    @GET("2ctv/mhg/public/api/patients")
    Call<List<Patient>> fetchAll();
}

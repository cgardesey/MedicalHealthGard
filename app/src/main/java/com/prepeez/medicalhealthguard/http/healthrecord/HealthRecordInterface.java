package com.prepeez.medicalhealthguard.http.healthrecord;


import com.prepeez.medicalhealthguard.pojo.HealthRecord;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface HealthRecordInterface {
    @FormUrlEncoded
    @POST("2ctv/mhg/public/api/save_health_record")
    Call<HealthRecord> addRecord(
            @Field("healthrecordid") String healthrecordid,
            @Field("weight") String weight,
            @Field("height") String height,
            @Field("heartrate") String heartrate,
            @Field("systolicbp") String systolicbp,
            @Field("diastolicbp") String diastolicbp,
            @Field("chiefpresentingcomplaint") String chiefpresentingcomplaint,
            @Field("historyofpreseningcomplaint") String historyofpreseningcomplaint,
            @Field("ondiriectquestioning") String ondiriectquestioning,
            @Field("generalphysicalexam") String generalphysicalexam,
            @Field("cardiovascularsyst") String cardiovascularsyst,
            @Field("respiratorysys") String respiratorysys,
            @Field("gastrointestinalsyst") String gastrointestinalsyst,
            @Field("centralnervoussyst") String centralnervoussyst,
            @Field("digitalrectalexam") String digitalrectalexam,
            @Field("vaginalexam") String vaginalexam,
            @Field("otherexamandfindings") String otherexamandfindings,
            @Field("fbc") String fbc,
            @Field("mps") String mps,
            @Field("ltf") String ltf,
            @Field("widal") String widal,
            @Field("kft") String kft,
            @Field("menstralperiod") String menstralperiod,
            @Field("regulariyofcycle") String regulariyofcycle,
            @Field("durationofcycle") String durationofcycle,
            @Field("flow") String flow,
            @Field("pain") String pain,
            @Field("termpregnancies") String termpregnancies,
            @Field("abortion") String abortion,
            @Field("livebirth") String livebirth,
            @Field("prematurebirth") String prematurebirth,
            @Field("multiplegestations") String multiplegestations,
            @Field("differentialdiagnosis") String differentialdiagnosis,
            @Field("diagnosis") String diagnosis,
            @Field("treatment") String treatment,
            @Field("bodytemperature") String bodytemperature,
            @Field("location") String location,
            @Field("nextscheduledvisit") String nextscheduledvisit,
            @Field("sms") String sms,
            @Field("smsstatus") String smsstatus,
            @Field("patientid") String patientid,
            @Field("agentid") String agentid
    );
}

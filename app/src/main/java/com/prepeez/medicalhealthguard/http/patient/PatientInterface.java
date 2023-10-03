package com.prepeez.medicalhealthguard.http.patient;


import com.prepeez.medicalhealthguard.pojo.HealthRecord;
import com.prepeez.medicalhealthguard.pojo.Patient;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface PatientInterface {
    @FormUrlEncoded
    @POST("2ctv/mhg/public/api/save_patient")
    Call<Patient> addRecord(
            @Field("patientid") String patientid,
            @Field("zone") String zone,
            @Field("picture") String picture,
            @Field("title") String title,
            @Field("firstname") String firstname,
            @Field("lastname") String lastname,
            @Field("othername") String othername,
            @Field("gender") String gender,
            @Field("dateofbirth") String dateofbirth,
            @Field("maritalstatus") String maritalstatus,
            @Field("contact") String contact,
            @Field("address") String address,
            @Field("location") String location,
            @Field("occupation") String occupation,
            @Field("nextofkintitle") String nextofkintitle,
            @Field("nextofkinfirstname") String nextofkinfirstname,
            @Field("nextofkinlastname") String nextofkinlastname,
            @Field("nextofkinothername") String nextofkinothername,
            @Field("nextofkingender") String nextofkingender,
            @Field("nextofkindateofbirth") String nextofkindateofbirth,
            @Field("nextofkinmaritalstatus") String nextofkinmaritalstatus,
            @Field("nextofkincontact") String nextofkincontact,
            @Field("nextofkinaddress") String nextofkinaddress,
            @Field("nextofkinlocation") String nextofkinlocation,
            @Field("nhisnumber") String nhisnumber,
            @Field("nhispicture") String nhispicture,
            @Field("abobloodgroup") String abobloodgroup,
            @Field("sicklecellbloodgroup") String sicklecellbloodgroup,
            @Field("allergichistory") String allergichistory,
            @Field("drughistory") String drughistory,
            @Field("pastsurgicalhistory") String pastsurgicalhistory,
            @Field("pasthistory") String pasthistory,
            @Field("familyhistory") String familyhistory,
            @Field("ageofmenarche") String ageofmenarche,
            @Field("gravidity") String gravidity,
            @Field("accounttype") String accounttype,
            @Field("grouptype") String grouptype,
            @Field("groupid") String groupid,
            @Field("active") int active,
            @Field("sms") String sms,
            @Field("smsstatus") String smsstatus
    );
}

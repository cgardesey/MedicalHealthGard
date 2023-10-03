package com.prepeez.medicalhealthguard.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.prepeez.medicalhealthguard.activity.AddHealthRecordActivity;
import com.prepeez.medicalhealthguard.activity.AgentActivity;
import com.prepeez.medicalhealthguard.http.agent.FetchAgentsSingleton;
import com.prepeez.medicalhealthguard.http.healthrecord.FetchHealthRecordsSingleton;
import com.prepeez.medicalhealthguard.http.healthrecord.HealthRecordSingleton;
import com.prepeez.medicalhealthguard.http.patient.FetchPatientsSingleton;
import com.prepeez.medicalhealthguard.http.patient.PatientSingleton;
import com.prepeez.medicalhealthguard.http.patient.UpdatePatientSingleton;
import com.prepeez.medicalhealthguard.pojo.Agent;
import com.prepeez.medicalhealthguard.pojo.HealthRecord;
import com.prepeez.medicalhealthguard.pojo.Patient;
import com.prepeez.medicalhealthguard.realm.RealmAgent;
import com.prepeez.medicalhealthguard.realm.RealmHealthRecord;
import com.prepeez.medicalhealthguard.realm.RealmPatient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.prepeez.medicalhealthguard.activity.AddPatientByHealthProfActivity.saveToRealm;
import static com.prepeez.medicalhealthguard.constants.Const.format;
import static com.prepeez.medicalhealthguard.constants.Const.isNetworkAvailable;


public class NetworkReceiver extends BroadcastReceiver {

    //Calendar calendar = Calendar.getInstance(Locale.UK);
    Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        //throw new UnsupportedOperationException("Not yet implemented");

        mContext = context;

        if (isNetworkAvailable(context)) {
            //toastMain(true);
            //context.sendBroadcast(new Intent(Const.NETWORK_INTENT_FILTER));
            Log.d("ebei", "connected");
            syncAdditionsWithServer_healthRecord();
            syncAdditionsWithServer_patient();
            syncAdditionsWithServer_agent();
            //syncUpdatesWithServer_patient();
        }

        else {
            //toastMain(false);
        }
    }

    private void syncUpdatesWithServer_patient() {
        Log.d("ebei", "syncUpdatesWithServer_patient");
        FetchPatientsSingleton fetchPatientsSingleton = new FetchPatientsSingleton();
        Call<List<Patient>> listCall = fetchPatientsSingleton.getPatientsInterface().fetchAll();
        listCall.enqueue(new Callback<List<Patient>>() {
            @Override
            public void onResponse(Call<List<Patient>> call, Response<List<Patient>> response) {
                if (response.isSuccessful()) {
                    Log.d("ebei", "isSuccessful");

                    for (final Patient patient : response.body()) {

                        final RealmPatient realmPatient1 = Realm.getDefaultInstance().where(RealmPatient.class).equalTo("patientid", patient.getPatientid()).findFirst();
                        if (realmPatient1 == null) {
                            continue;
                        }
                        if (realmPatient1.getCreated_at() == null) {
                            continue;
                        }

                        //"2018-08-09 22:09:48";
                        java.util.Date server_date = null;
                        java.util.Date local_storage_date = null;

                        try {
                            Log.d("ebei", "try");
                            server_date = format.parse(patient.getUpdated_at());
                            local_storage_date = format.parse(realmPatient1.getUpdated_at());

                            Log.d("ebei", patient.getUpdated_at() + " " + realmPatient1.getUpdated_at());
                        } catch (ParseException e) {
                            e.printStackTrace();
                            Log.d("ebei", e.getMessage());
                            continue;
                        }

                        if (server_date.after(local_storage_date)) {
                            Log.d("ebei", "after");
                            //sync from server
                            Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realmPatient1.setPatientid(patient.getPatientid());
                                    realmPatient1.setPicture(patient.getPicture());
                                    realmPatient1.setTitle(patient.getTitle());
                                    realmPatient1.setFirstname(patient.getFirstname());
                                    realmPatient1.setLastname(patient.getLastname());
                                    realmPatient1.setOthername(patient.getOthername());
                                    realmPatient1.setGender(patient.getGender());
                                    realmPatient1.setDateofbirth(patient.getDateofbirth());
                                    realmPatient1.setMaritalstatus(patient.getMaritalstatus());
                                    realmPatient1.setContact(patient.getContact());
                                    realmPatient1.setAddress(patient.getAddress());
                                    realmPatient1.setLocation(patient.getLocation());
                                    realmPatient1.setOccupation(patient.getOccupation());
                                    realmPatient1.setNextofkintitle(patient.getNextofkintitle());
                                    realmPatient1.setNextofkinfirstname(patient.getNextofkinfirstname());
                                    realmPatient1.setNextofkinlastname(patient.getNextofkinlastname());
                                    realmPatient1.setNextofkinothername(patient.getNextofkinothername());
                                    realmPatient1.setNextofkingender(patient.getNextofkingender());
                                    realmPatient1.setNextofkindateofbirth(patient.getNextofkindateofbirth());
                                    realmPatient1.setMaritalstatus(patient.getMaritalstatus());
                                    realmPatient1.setNextofkincontact(patient.getNextofkincontact());
                                    realmPatient1.setNextofkinaddress(patient.getNextofkinaddress());
                                    realmPatient1.setNextofkinlocation(patient.getNextofkinlocation());
                                    realmPatient1.setNhisnumber(patient.getNhisnumber());
                                    realmPatient1.setNhispicture(patient.getNhispicture());
                                    realmPatient1.setAbobloodgroup(patient.getAbobloodgroup());
                                    realmPatient1.setSicklecellbloodgroup(patient.getSicklecellbloodgroup());
                                    realmPatient1.setAllergichistory(patient.getAllergichistory());
                                    realmPatient1.setDrughistory(patient.getDrughistory());
                                    realmPatient1.setPastsurgicalhistory(patient.getPastsurgicalhistory());
                                    realmPatient1.setPasthistory(patient.getPasthistory());
                                    realmPatient1.setFamilyhistory(patient.getFamilyhistory());
                                    realmPatient1.setAgeofmenarche(patient.getAgeofmenarche());
                                    realmPatient1.setGravidity(patient.getGravidity());
                                    realmPatient1.setAccounttype(patient.getAccounttype());
                                    realmPatient1.setGrouptype(patient.getGrouptype());
                                    realmPatient1.setGroupid(patient.getGroupid());
                                    realmPatient1.setActive(patient.getActive());
                                    realmPatient1.setSmsstatus(patient.getSmsstatus());
                                    realmPatient1.setCreated_at(patient.getCreated_at());
                                    realmPatient1.setUpdated_at(patient.getUpdated_at());

                                    realm.copyToRealmOrUpdate(realmPatient1);
                                }
                            });
                        }
                        if (server_date.before(local_storage_date)) {
                            Log.d("ebei", "before");
                            //syc to server
                            pushUpdates_patient(realmPatient1);
                        }
                    }
                }
                else {
                    Log.d("ebei", "unsuccessful");
                }
            }

            @Override
            public void onFailure(Call<List<Patient>> call, Throwable t) {
                Log.d("ebei", t.getMessage());
            }
        });
    }

    private void pushUpdates_patient(final RealmPatient record) {

        final RealmPatient realmPatient = new RealmPatient(
                record.getPatientid(),
                record.getZone(),
                record.getPicture(),
                record.getTitle(),
                record.getFirstname(),
                record.getLastname(),
                record.getOthername(),
                record.getGender(),
                record.getDateofbirth(),
                record.getMaritalstatus(),
                record.getContact(),
                record.getAddress(),
                record.getLocation(),
                record.getOccupation(),
                record.getNextofkintitle(),
                record.getNextofkinfirstname(),
                record.getNextofkinlastname(),
                record.getNextofkinothername(),
                record.getNextofkingender(),
                record.getNextofkindateofbirth(),
                record.getNextofkinmaritalstatus(),
                record.getNextofkincontact(),
                record.getNextofkinaddress(),
                record.getNextofkinlocation(),
                record.getNhisnumber(),
                record.getNhispicture(),
                record.getAbobloodgroup(),
                record.getSicklecellbloodgroup(),
                record.getAllergichistory(),
                record.getDrughistory(),
                record.getPastsurgicalhistory(),
                record.getPasthistory(),
                record.getFamilyhistory(),
                record.getAgeofmenarche(),
                record.getGravidity(),
                record.getAccounttype(),
                record.getGrouptype(),
                record.getGroupid(),
                record.getActive(),
                record.getSms(),
                record.getSmsstatus(),
                record.getCreated_at(),
                record.getUpdated_at()
        );

        Patient patient = new Patient(
                record.getId(),
                record.getPatientid(),
                record.getZone(),
                record.getPicture(),
                record.getTitle(),
                record.getFirstname(),
                record.getLastname(),
                record.getOthername(),
                record.getGender(),
                record.getDateofbirth(),
                record.getMaritalstatus(),
                record.getContact(),
                record.getAddress(),
                record.getLocation(),
                record.getOccupation(),
                record.getNextofkintitle(),
                record.getNextofkinfirstname(),
                record.getNextofkinlastname(),
                record.getNextofkinothername(),
                record.getNextofkingender(),
                record.getNextofkindateofbirth(),
                record.getNextofkinmaritalstatus(),
                record.getNextofkincontact(),
                record.getNextofkinaddress(),
                record.getNextofkinlocation(),
                record.getNhisnumber(),
                record.getNhispicture(),
                record.getAbobloodgroup(),
                record.getSicklecellbloodgroup(),
                record.getAllergichistory(),
                record.getDrughistory(),
                record.getPastsurgicalhistory(),
                record.getPasthistory(),
                record.getFamilyhistory(),
                record.getAgeofmenarche(),
                record.getGravidity(),
                record.getAccounttype(),
                record.getGrouptype(),
                record.getGroupid(),
                record.getActive(),
                record.getSms(),
                record.getSmsstatus()
        );

        UpdatePatientSingleton singleton = new UpdatePatientSingleton();
        Call<Patient> recordCall = singleton.updateRecordsInterface().update(record.getPatientid(), patient);

        recordCall.enqueue(new Callback<Patient>() {
            @Override
            public void onResponse(Call<Patient> call, final Response<Patient> response) {
                if (response.isSuccessful()) {
//                    final RealmPatient realmPatient_callback = new RealmPatient(
//                            patient.getPatientid(),
//                            patient.getPicture(),
//                            patient.getTitle(),
//                            patient.getFirstname(),
//                            patient.getLastname(),
//                            patient.getOthername(),
//                            patient.getGender(),
//                            patient.getDateofbirth(),
//                            patient.getMaritalstatus(),
//                            patient.getContact(),
//                            patient.getAddress(),
//                            patient.getLocation(),
//                            patient.getOccupation(),
//                            patient.getNextofkintitle(),
//                            patient.getNextofkinfirstname(),
//                            patient.getNextofkinlastname(),
//                            patient.getNextofkinothername(),
//                            patient.getNextofkingender(),
//                            patient.getNextofkindateofbirth(),
//                            patient.getNextofkinmaritalstatus(),
//                            patient.getNextofkincontact(),
//                            patient.getNextofkinaddress(),
//                            patient.getNextofkinlocation(),
//                            patient.getNhisnumber(),
//                            patient.getNhispicture(),
//                            patient.getAbobloodgroup(),
//                            patient.getSicklecellbloodgroup(),
//                            patient.getAllergies(),
//                            patient.getSpecializedcondition(),
//                            patient.getAccounttype(),
//                            patient.getActive(),
//                            1,
//                            1,
//                            patient.getCreated_at(),
//                            patient.getUpdated_at()
//                    );
                    //Const.showToast(mContext, "Data Updated Successfully");

                    Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realmPatient.setId(record.getId());
                            realmPatient.setUpdated_at(response.body().getUpdated_at());
                            realm.copyToRealmOrUpdate(realmPatient);
                        }
                    });
                }
            }

            private void updateRealmRecord(RealmPatient realmPatient, int i) {
            }

            @Override
            public void onFailure(Call<Patient> call, Throwable t) {
                //Const.showToast(getApplicationContext(), "You may not have internet connection");

            }
        });
    }

    private void syncAdditionsWithServer_patient() {

        /*RealmConfiguration myConfig = new RealmConfiguration.Builder()
                .name("myrealm.realm")
                .schemaVersion(1)
                .migration()
                .build();

        Realm realm = Realm.getInstance(myConfig);*/

        Realm realm = Realm.getDefaultInstance();

        final int size_devicestorage = realm.where(RealmPatient.class).findAll().size();

        FetchPatientsSingleton fetchPatientsSingleton = new FetchPatientsSingleton();
        Call<List<Patient>> listCall = fetchPatientsSingleton.getPatientsInterface().fetchAll();
        listCall.enqueue(new Callback<List<Patient>>() {
            @Override
            public void onResponse(Call<List<Patient>> call, Response<List<Patient>> response) {
                if (response.isSuccessful()) {

                    int size_server = response.body().size();

                    if (size_devicestorage > size_server) {
                        //sync to server
                        syncToServer_patient();
                    } else if (size_server > size_devicestorage) {
                        //sync from server
                        for (Patient patient : response.body()) {

                            if (Realm.getDefaultInstance().where(RealmPatient.class).equalTo("patientid", patient.getPatientid()).findAll().size() > 0) {
                                continue;
                            } else {

                                RealmPatient realmPatient = new RealmPatient(
                                        patient.getPatientid(),
                                        patient.getZone(),
                                        patient.getPicture(),
                                        patient.getTitle(),
                                        patient.getFirstname(),
                                        patient.getLastname(),
                                        patient.getOthername(),
                                        patient.getGender(),
                                        patient.getDateofbirth(),
                                        patient.getMaritalstatus(),
                                        patient.getContact(),
                                        patient.getAddress(),
                                        patient.getLocation(),
                                        patient.getOccupation(),
                                        patient.getNextofkintitle(),
                                        patient.getNextofkinfirstname(),
                                        patient.getNextofkinlastname(),
                                        patient.getNextofkinothername(),
                                        patient.getNextofkingender(),
                                        patient.getNextofkindateofbirth(),
                                        patient.getNextofkinmaritalstatus(),
                                        patient.getNextofkincontact(),
                                        patient.getNextofkinaddress(),
                                        patient.getNextofkinlocation(),
                                        patient.getNhisnumber(),
                                        patient.getNhispicture(),
                                        patient.getAbobloodgroup(),
                                        patient.getSicklecellbloodgroup(),
                                        patient.getAllergichistory(),
                                        patient.getDrughistory(),
                                        patient.getPastsurgicalhistory(),
                                        patient.getPasthistory(),
                                        patient.getFamilyhistory(),
                                        patient.getAgeofmenarche(),
                                        patient.getGravidity(),
                                        patient.getAccounttype(),
                                        patient.getGrouptype(),
                                        patient.getGroupid(),
                                        patient.getActive(),
                                        patient.getSms(),
                                        patient.getSmsstatus(),
                                        patient.getCreated_at(),
                                        patient.getUpdated_at()

                                );
                                saveToRealm(realmPatient);
                            }
                        }
                        //syncFromServer();
                    }

                }
            }

            @Override
            public void onFailure(Call<List<Patient>> call, Throwable t) {
                Log.d("cyril", t.getMessage());
            }
        });
    }

    private void syncToServer_patient() {
        //Log.d("cyril", "syncToServer_patient");
        Realm realm = Realm.getDefaultInstance();
        RealmResults<RealmPatient> results = realm.where(RealmPatient.class).notEqualTo("smsstatus", "Sent").findAll();

        for (RealmPatient realmPatient : results) {
            sendData_patient(realmPatient);
        }
    }

    private void sendData_patient(final RealmPatient record) {

        final RealmPatient realmPatient = new RealmPatient(
                record.getPatientid(),
                record.getZone(),
                record.getPicture(),
                record.getTitle(),
                record.getFirstname(),
                record.getLastname(),
                record.getOthername(),
                record.getGender(),
                record.getDateofbirth(),
                record.getMaritalstatus(),
                record.getContact(),
                record.getAddress(),
                record.getLocation(),
                record.getOccupation(),
                record.getNextofkintitle(),
                record.getNextofkinfirstname(),
                record.getNextofkinlastname(),
                record.getNextofkinothername(),
                record.getNextofkingender(),
                record.getNextofkindateofbirth(),
                record.getNextofkinmaritalstatus(),
                record.getNextofkincontact(),
                record.getNextofkinaddress(),
                record.getNextofkinlocation(),
                record.getNhisnumber(),
                record.getNhispicture(),
                record.getAbobloodgroup(),
                record.getSicklecellbloodgroup(),
                record.getAllergichistory(),
                record.getDrughistory(),
                record.getPastsurgicalhistory(),
                record.getPasthistory(),
                record.getFamilyhistory(),
                record.getAgeofmenarche(),
                record.getGravidity(),
                record.getAccounttype(),
                record.getGrouptype(),
                record.getGroupid(),
                record.getActive(),
                record.getSms(),
                record.getSmsstatus(),
                record.getCreated_at(),
                record.getUpdated_at()
        );

        PatientSingleton patientSingleton = new PatientSingleton();
        Call<Patient> patientCall = patientSingleton.getPatientInterface().addRecord(
                record.getPatientid(),
                record.getZone(),
                record.getPicture(),
                record.getTitle(),
                record.getFirstname(),
                record.getLastname(),
                record.getOthername(),
                record.getGender(),
                record.getDateofbirth(),
                record.getMaritalstatus(),
                record.getContact(),
                record.getAddress(),
                record.getLocation(),
                record.getOccupation(),
                record.getNextofkintitle(),
                record.getNextofkinfirstname(),
                record.getNextofkinlastname(),
                record.getNextofkinothername(),
                record.getNextofkingender(),
                record.getNextofkindateofbirth(),
                record.getNextofkinmaritalstatus(),
                record.getNextofkincontact(),
                record.getNextofkinaddress(),
                record.getNextofkinlocation(),
                record.getNhisnumber(),
                record.getNhispicture(),
                record.getAbobloodgroup(),
                record.getSicklecellbloodgroup(),
                record.getAllergichistory(),
                record.getDrughistory(),
                record.getPastsurgicalhistory(),
                record.getPasthistory(),
                record.getFamilyhistory(),
                record.getAgeofmenarche(),
                record.getGravidity(),
                record.getAccounttype(),
                record.getGrouptype(),
                record.getGroupid(),
                record.getActive(),
                record.getSms(),
                record.getSmsstatus()
        );

        patientCall.enqueue(new Callback<Patient>() {
            @Override
            public void onResponse(Call<Patient> call, Response<Patient> response) {

                if (response.isSuccessful()) {

                    Log.d("REALM_RESULTS", "SUCCESSFULL");

                    realmPatient.setId(record.getId());
                    realmPatient.setCreated_at(response.body().getCreated_at());
                    realmPatient.setUpdated_at(response.body().getUpdated_at());

                    final Realm realm = Realm.getDefaultInstance();

                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm mRealm) {
                            mRealm.copyToRealmOrUpdate(realmPatient);
                        }
                    }, new Realm.Transaction.OnSuccess() {
                        @Override
                        public void onSuccess() {
                            Log.d("REALM_RESULTS", "SUCCESSFULL");
                            realm.close();
                        }
                    }, new Realm.Transaction.OnError() {
                        @Override
                        public void onError(Throwable error) {
                            Log.d("REALM_RESULTS", error.toString());
                        }
                    });


                    /*realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.copyToRealmOrUpdate(record);
                        }
                    }, new Realm.Transaction.OnSuccess() {
                        @Override
                        public void onSuccess() {
                            //Const.showToast();
                            Log.d("Cyril", "response.isSuccessful()");
                            realm.close();
                        }
                    });*/

                    //realm.beginTransaction();
                    //realm.commitTransaction();
                } else {
                    Log.d("REALM_RESULTS", "FAILED");
                }

            }

            @Override
            public void onFailure(Call<Patient> call, Throwable t) {

            }
        });


    }

    public void syncAdditionsWithServer_healthRecord() {

        /*RealmConfiguration myConfig = new RealmConfiguration.Builder()
                .name("myrealm.realm")
                .schemaVersion(1)
                .migration()
                .build();

        Realm realm = Realm.getInstance(myConfig);*/

        Realm realm = Realm.getDefaultInstance();

        final int size_devicestorage = realm.where(RealmHealthRecord.class).findAll().size();

        FetchHealthRecordsSingleton fetchHealthRecordsSingleton = new FetchHealthRecordsSingleton();
        Call<List<HealthRecord>> listCall = fetchHealthRecordsSingleton.getHRecordsInterface().fetchAll();
        listCall.enqueue(new Callback<List<HealthRecord>>() {
            @Override
            public void onResponse(Call<List<HealthRecord>> call, Response<List<HealthRecord>> response) {
                if (response.isSuccessful()) {

                    int size_server = response.body().size();

                    if (size_devicestorage > size_server) {
                        //sync to server
                        syncToServer_health_record();
                    } else if (size_server > size_devicestorage) {
                        //sync from server
                        for (HealthRecord healthRecord : response.body()) {

                            if (Realm.getDefaultInstance().where(RealmHealthRecord.class).equalTo("created_at", healthRecord.getCreated_at()).findAll().size() > 0) {
                                continue;
                            } else {

                                RealmHealthRecord realmHealthRecord = new RealmHealthRecord(
                                        healthRecord.getHealthrecordid(),
                                        healthRecord.getWeight(),
                                        healthRecord.getHeight(),
                                        healthRecord.getHeartrate(),
                                        healthRecord.getSystolicbp(),
                                        healthRecord.getDiastolicbp(),
                                        healthRecord.getChiefpresentingcomplaint(),
                                        healthRecord.getHistoryofpreseningcomplaint(),
                                        healthRecord.getOndiriectquestioning(),
                                        healthRecord.getGeneralphysicalexam(),
                                        healthRecord.getCardiovascularsyst(),
                                        healthRecord.getRespiratorysys(),
                                        healthRecord.getGastrointestinalsyst(),
                                        healthRecord.getCentralnervoussyst(),
                                        healthRecord.getDigitalrectalexam(),
                                        healthRecord.getVaginalexam(),
                                        healthRecord.getOtherexamandfindings(),
                                        healthRecord.getFbc(),
                                        healthRecord.getMps(),
                                        healthRecord.getLtf(),
                                        healthRecord.getWidal(),
                                        healthRecord.getKft(),
                                        healthRecord.getMenstralperiod(),
                                        healthRecord.getRegulariyofcycle(),
                                        healthRecord.getDurationofcycle(),
                                        healthRecord.getFlow(),
                                        healthRecord.getPain(),
                                        healthRecord.getTermpregnancies(),
                                        healthRecord.getAbortion(),
                                        healthRecord.getLivebirth(),
                                        healthRecord.getPrematurebirth(),
                                        healthRecord.getMultiplegestations(),
                                        healthRecord.getDifferentialdiagnosis(),
                                        healthRecord.getDiagnosis(),
                                        healthRecord.getTreatment(),
                                        healthRecord.getBodytemperature(),
                                        healthRecord.getLocation(),
                                        healthRecord.getNextscheduledvisit(),
                                        healthRecord.getSms(),
                                        healthRecord.getSmsstatus(),
                                        healthRecord.getPatientid(),
                                        healthRecord.getPatientid(),
                                        healthRecord.getCreated_at(),
                                        healthRecord.getUpdated_at()
                                );
                                AddHealthRecordActivity.saveToRealm(realmHealthRecord);
                            }
                        }
                        //syncFromServer();
                    }

                }
            }

            @Override
            public void onFailure(Call<List<HealthRecord>> call, Throwable t) {
                Log.d("cyril", t.getMessage());
            }
        });
    }

    public  void syncToServer_health_record() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<RealmHealthRecord> results = realm.where(RealmHealthRecord.class).notEqualTo("smsstatus", "Sent").findAll();

        for (RealmHealthRecord realmHealthRecord : results) {
            sendData_health_record(realmHealthRecord);
        }

    }

    public void sendData_health_record(final RealmHealthRecord record) {

        final RealmHealthRecord realmHealthRecord = new RealmHealthRecord(
                record.getHealthrecordid(),
                record.getWeight(),
                record.getHeight(),
                record.getHeartrate(),
                record.getSystolicbp(),
                record.getDiastolicbp(),
                record.getChiefpresentingcomplaint(),
                record.getHistoryofpreseningcomplaint(),
                record.getOndiriectquestioning(),
                record.getGeneralphysicalexam(),
                record.getCardiovascularsyst(),
                record.getRespiratorysys(),
                record.getGastrointestinalsyst(),
                record.getCentralnervoussyst(),
                record.getDigitalrectalexam(),
                record.getVaginalexam(),
                record.getOtherexamandfindings(),
                record.getFbc(),
                record.getMps(),
                record.getLtf(),
                record.getWidal(),
                record.getKft(),
                record.getMenstralperiod(),
                record.getRegulariyofcycle(),
                record.getDurationofcycle(),
                record.getFlow(),
                record.getPain(),
                record.getTermpregnancies(),
                record.getAbortion(),
                record.getLivebirth(),
                record.getPrematurebirth(),
                record.getMultiplegestations(),
                record.getDifferentialdiagnosis(),
                record.getDiagnosis(),
                record.getTreatment(),
                record.getBodytemperature(),
                record.getLocation(),
                record.getNextscheduledvisit(),
                record.getSms(),
                record.getSmsstatus(),
                record.getPatientid(),
                record.getPatientid(),
                record.getCreated_at(),
                record.getUpdated_at()
        );


        HealthRecordSingleton healthRecordSingleton = new HealthRecordSingleton();
        Call<HealthRecord> healthRecordCall = healthRecordSingleton.getHealthRecordInterface().addRecord(
                record.getHealthrecordid(),
                record.getWeight(),
                record.getHeight(),
                record.getHeartrate(),
                record.getSystolicbp(),
                record.getDiastolicbp(),
                record.getChiefpresentingcomplaint(),
                record.getHistoryofpreseningcomplaint(),
                record.getOndiriectquestioning(),
                record.getGeneralphysicalexam(),
                record.getCardiovascularsyst(),
                record.getRespiratorysys(),
                record.getGastrointestinalsyst(),
                record.getCentralnervoussyst(),
                record.getDigitalrectalexam(),
                record.getVaginalexam(),
                record.getOtherexamandfindings(),
                record.getFbc(),
                record.getMps(),
                record.getLtf(),
                record.getWidal(),
                record.getKft(),
                record.getMenstralperiod(),
                record.getRegulariyofcycle(),
                record.getDurationofcycle(),
                record.getFlow(),
                record.getPain(),
                record.getTermpregnancies(),
                record.getAbortion(),
                record.getLivebirth(),
                record.getPrematurebirth(),
                record.getMultiplegestations(),
                record.getDifferentialdiagnosis(),
                record.getDiagnosis(),
                record.getTreatment(),
                record.getBodytemperature(),
                record.getLocation(),
                record.getNextscheduledvisit(),
                record.getSms(),
                record.getSmsstatus(),
                record.getPatientid(),
                record.getAgentid()
        );

        healthRecordCall.enqueue(new Callback<HealthRecord>() {
            @Override
            public void onResponse(Call<HealthRecord> call, Response<HealthRecord> response) {

                if (response.isSuccessful()) {

                    Log.d("REALM_RESULTS", "SUCCESSFULL");


                    realmHealthRecord.setId(record.getId());
                    realmHealthRecord.setCreated_at(response.body().getCreated_at());
                    realmHealthRecord.setUpdated_at(response.body().getUpdated_at());

                    final Realm realm = Realm.getDefaultInstance();

                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm mRealm) {
                            mRealm.copyToRealmOrUpdate(realmHealthRecord);
                        }
                    }, new Realm.Transaction.OnSuccess() {
                        @Override
                        public void onSuccess() {
                            Log.d("REALM_RESULTS", "SUCCESSFULL");
                            realm.close();
                        }
                    }, new Realm.Transaction.OnError() {
                        @Override
                        public void onError(Throwable error) {
                            Log.d("REALM_RESULTS", error.toString());
                        }
                    });


                    /*realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.copyToRealmOrUpdate(record);
                        }
                    }, new Realm.Transaction.OnSuccess() {
                        @Override
                        public void onSuccess() {
                            //Const.showToast();
                            Log.d("Cyril", "response.isSuccessful()");
                            realm.close();
                        }
                    });*/

                    //realm.beginTransaction();
                    //realm.commitTransaction();
                } else {
                    Log.d("REALM_RESULTS", "FAILED");
                }

            }

            @Override
            public void onFailure(Call<HealthRecord> call, Throwable t) {

            }
        });


    }

    public void syncAdditionsWithServer_agent() {
        Realm realm = Realm.getDefaultInstance();

        final int size_devicestorage = realm.where(RealmAgent.class).findAll().size();

        FetchAgentsSingleton fetchAgentsSingleton = new FetchAgentsSingleton();
        Call<List<Agent>> listCall = fetchAgentsSingleton.getAgentsInterface().fetchAll();
        listCall.enqueue(new Callback<List<Agent>>() {
            @Override
            public void onResponse(Call<List<Agent>> call, Response<List<Agent>> response) {
                if (response.isSuccessful()) {

                    int size_server = response.body().size();
                    Log.d("cyril", "size_devicestorage: " + String.valueOf(size_devicestorage));
                    Log.d("cyril", "size_server: " + String.valueOf(size_server));
                    if (size_server > size_devicestorage) {
                        //sync from server
                        for (Agent agent : response.body()) {

                            if (Realm.getDefaultInstance().where(RealmAgent.class).equalTo("created_at", agent.getCreated_at()).findAll().size() > 0) {
                                continue;
                            } else {

                                RealmAgent realmAgent = new RealmAgent(
                                        agent.getAgentid(),
                                        agent.getZone(),
                                        agent.getPicture(),
                                        agent.getTitle(),
                                        agent.getFirstname(),
                                        agent.getLastname(),
                                        agent.getOthername(),
                                        agent.getGender(),
                                        agent.getDateofbirth(),
                                        agent.getContact(),
                                        agent.getAddress(),
                                        agent.getDateofemployment(),
                                        agent.getActive(),
                                        agent.getCreated_at(),
                                        agent.getUpdated_at()
                                );
                                AgentActivity.saveToRealm(realmAgent);
                            }
                        }
                        //syncFromServer();
                    }

                }
            }

            @Override
            public void onFailure(Call<List<Agent>> call, Throwable t) {
                Log.d("cyril", t.getMessage());
            }
        });
    }
}

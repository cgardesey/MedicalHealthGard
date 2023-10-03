package com.prepeez.medicalhealthguard.realm;

import com.google.gson.annotations.Expose;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;
import io.realm.annotations.RealmModule;


public class RealmHealthRecord extends RealmObject {
    @PrimaryKey
    private int id;

    private String healthrecordid;

    private String weight;
    
    private String height;
    
    private String heartrate;
    
    private String systolicbp;
    
    private String diastolicbp;

    private String chiefpresentingcomplaint;

    private String historyofpreseningcomplaint;

    private String ondiriectquestioning;

    private String generalphysicalexam;

    private String cardiovascularsyst;

    private String respiratorysys;

    private String gastrointestinalsyst;

    private String centralnervoussyst;

    private String digitalrectalexam;

    private String vaginalexam;

    private String otherexamandfindings;

    private String fbc;

    private String mps;

    private String ltf;

    private String widal;

    private String kft;

    private String menstralperiod;

    private String regulariyofcycle;

    private String durationofcycle;

    private String flow;

    private String pain;

    private String termpregnancies;

    private String abortion;

    private String livebirth;

    private String prematurebirth;

    private String multiplegestations;

    private String differentialdiagnosis;

    private String diagnosis;

    private String treatment;
    
    private String bodytemperature;
    
    private String location;

    private String nextscheduledvisit;

    private String sms;

    private String smsstatus;
    
    private String patientid;
    
    private String agentid;
    
    private String created_at;
    
    private String updated_at;

    public RealmHealthRecord() {

    }

    public RealmHealthRecord(String healthrecordid, String weight, String height, String heartrate, String systolicbp, String diastolicbp, String chiefpresentingcomplaint, String historyofpreseningcomplaint, String ondiriectquestioning, String generalphysicalexam, String cardiovascularsyst, String respiratorysys, String gastrointestinalsyst, String centralnervoussyst, String digitalrectalexam, String vaginalexam, String otherexamandfindings, String fbc, String mps, String ltf, String widal, String kft, String menstralperiod, String regulariyofcycle, String durationofcycle, String flow, String pain, String termpregnancies, String abortion, String livebirth, String prematurebirth, String multiplegestations, String differentialdiagnosis, String diagnosis, String treatment, String bodytemperature, String location, String nextscheduledvisit, String sms, String smsstatus, String patientid, String agentid, String created_at, String updated_at) {
        this.weight = healthrecordid;
        this.weight = weight;
        this.height = height;
        this.heartrate = heartrate;
        this.systolicbp = systolicbp;
        this.diastolicbp = diastolicbp;
        this.chiefpresentingcomplaint = chiefpresentingcomplaint;
        this.historyofpreseningcomplaint = historyofpreseningcomplaint;
        this.ondiriectquestioning = ondiriectquestioning;
        this.generalphysicalexam = generalphysicalexam;
        this.cardiovascularsyst = cardiovascularsyst;
        this.respiratorysys = respiratorysys;
        this.gastrointestinalsyst = gastrointestinalsyst;
        this.centralnervoussyst = centralnervoussyst;
        this.digitalrectalexam = digitalrectalexam;
        this.vaginalexam = vaginalexam;
        this.otherexamandfindings = otherexamandfindings;
        this.fbc = fbc;
        this.mps = mps;
        this.ltf = ltf;
        this.widal = widal;
        this.kft = kft;
        this.menstralperiod = menstralperiod;
        this.regulariyofcycle = regulariyofcycle;
        this.durationofcycle = durationofcycle;
        this.flow = flow;
        this.pain = pain;
        this.termpregnancies = termpregnancies;
        this.abortion = abortion;
        this.livebirth = livebirth;
        this.prematurebirth = prematurebirth;
        this.multiplegestations = multiplegestations;
        this.differentialdiagnosis = differentialdiagnosis;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.bodytemperature = bodytemperature;
        this.location = location;
        this.nextscheduledvisit = nextscheduledvisit;
        this.sms = sms;
        this.smsstatus = smsstatus;
        this.patientid = patientid;
        this.agentid = agentid;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHealthrecordid() {
        return healthrecordid;
    }

    public void setHealthrecordid(String healthrecordid) {
        this.healthrecordid = healthrecordid;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getHeartrate() {
        return heartrate;
    }

    public void setHeartrate(String heartrate) {
        this.heartrate = heartrate;
    }

    public String getSystolicbp() {
        return systolicbp;
    }

    public void setSystolicbp(String systolicbp) {
        this.systolicbp = systolicbp;
    }

    public String getDiastolicbp() {
        return diastolicbp;
    }

    public void setDiastolicbp(String diastolicbp) {
        this.diastolicbp = diastolicbp;
    }

    public String getChiefpresentingcomplaint() {
        return chiefpresentingcomplaint;
    }

    public void setChiefpresentingcomplaint(String chiefpresentingcomplaint) {
        this.chiefpresentingcomplaint = chiefpresentingcomplaint;
    }

    public String getHistoryofpreseningcomplaint() {
        return historyofpreseningcomplaint;
    }

    public void setHistoryofpreseningcomplaint(String historyofpreseningcomplaint) {
        this.historyofpreseningcomplaint = historyofpreseningcomplaint;
    }

    public String getOndiriectquestioning() {
        return ondiriectquestioning;
    }

    public void setOndiriectquestioning(String ondiriectquestioning) {
        this.ondiriectquestioning = ondiriectquestioning;
    }

    public String getGeneralphysicalexam() {
        return generalphysicalexam;
    }

    public void setGeneralphysicalexam(String generalphysicalexam) {
        this.generalphysicalexam = generalphysicalexam;
    }

    public String getCardiovascularsyst() {
        return cardiovascularsyst;
    }

    public void setCardiovascularsyst(String cardiovascularsyst) {
        this.cardiovascularsyst = cardiovascularsyst;
    }

    public String getRespiratorysys() {
        return respiratorysys;
    }

    public void setRespiratorysys(String respiratorysys) {
        this.respiratorysys = respiratorysys;
    }

    public String getGastrointestinalsyst() {
        return gastrointestinalsyst;
    }

    public void setGastrointestinalsyst(String gastrointestinalsyst) {
        this.gastrointestinalsyst = gastrointestinalsyst;
    }

    public String getCentralnervoussyst() {
        return centralnervoussyst;
    }

    public void setCentralnervoussyst(String centralnervoussyst) {
        this.centralnervoussyst = centralnervoussyst;
    }

    public String getDigitalrectalexam() {
        return digitalrectalexam;
    }

    public void setDigitalrectalexam(String digitalrectalexam) {
        this.digitalrectalexam = digitalrectalexam;
    }

    public String getVaginalexam() {
        return vaginalexam;
    }

    public void setVaginalexam(String vaginalexam) {
        this.vaginalexam = vaginalexam;
    }

    public String getOtherexamandfindings() {
        return otherexamandfindings;
    }

    public void setOtherexamandfindings(String otherexamandfindings) {
        this.otherexamandfindings = otherexamandfindings;
    }

    public String getFbc() {
        return fbc;
    }

    public void setFbc(String fbc) {
        this.fbc = fbc;
    }

    public String getMps() {
        return mps;
    }

    public void setMps(String mps) {
        this.mps = mps;
    }

    public String getLtf() {
        return ltf;
    }

    public void setLtf(String ltf) {
        this.ltf = ltf;
    }

    public String getWidal() {
        return widal;
    }

    public void setWidal(String widal) {
        this.widal = widal;
    }

    public String getKft() {
        return kft;
    }

    public void setKft(String kft) {
        this.kft = kft;
    }

    public String getMenstralperiod() {
        return menstralperiod;
    }

    public void setMenstralperiod(String menstralperiod) {
        this.menstralperiod = menstralperiod;
    }

    public String getRegulariyofcycle() {
        return regulariyofcycle;
    }

    public void setRegulariyofcycle(String regulariyofcycle) {
        this.regulariyofcycle = regulariyofcycle;
    }

    public String getDurationofcycle() {
        return durationofcycle;
    }

    public void setDurationofcycle(String durationofcycle) {
        this.durationofcycle = durationofcycle;
    }

    public String getFlow() {
        return flow;
    }

    public void setFlow(String flow) {
        this.flow = flow;
    }

    public String getPain() {
        return pain;
    }

    public void setPain(String pain) {
        this.pain = pain;
    }

    public String getTermpregnancies() {
        return termpregnancies;
    }

    public void setTermpregnancies(String termpregnancies) {
        this.termpregnancies = termpregnancies;
    }

    public String getAbortion() {
        return abortion;
    }

    public void setAbortion(String abortion) {
        this.abortion = abortion;
    }

    public String getLivebirth() {
        return livebirth;
    }

    public void setLivebirth(String livebirth) {
        this.livebirth = livebirth;
    }

    public String getPrematurebirth() {
        return prematurebirth;
    }

    public void setPrematurebirth(String prematurebirth) {
        this.prematurebirth = prematurebirth;
    }

    public String getMultiplegestations() {
        return multiplegestations;
    }

    public void setMultiplegestations(String multiplegestations) {
        this.multiplegestations = multiplegestations;
    }

    public String getDifferentialdiagnosis() {
        return differentialdiagnosis;
    }

    public void setDifferentialdiagnosis(String differentialdiagnosis) {
        this.differentialdiagnosis = differentialdiagnosis;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public String getBodytemperature() {
        return bodytemperature;
    }

    public void setBodytemperature(String bodytemperature) {
        this.bodytemperature = bodytemperature;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNextscheduledvisit() {
        return nextscheduledvisit;
    }

    public void setNextscheduledvisit(String nextscheduledvisit) {
        this.nextscheduledvisit = nextscheduledvisit;
    }

    public String getSms() {
        return sms;
    }

    public void setSms(String sms) {
        this.sms = sms;
    }

    public String getSmsstatus() {
        return smsstatus;
    }

    public void setSmsstatus(String smsstatus) {
        this.smsstatus = smsstatus;
    }

    public String getPatientid() {
        return patientid;
    }

    public void setPatientid(String patientid) {
        this.patientid = patientid;
    }

    public String getAgentid() {
        return agentid;
    }

    public void setAgentid(String agentid) {
        this.agentid = agentid;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}

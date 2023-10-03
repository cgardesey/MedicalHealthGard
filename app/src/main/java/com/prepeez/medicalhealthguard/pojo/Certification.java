package com.prepeez.medicalhealthguard.pojo;

/**
 * Created by Nana on 12/16/2017.
 */

public class Certification {
    private String institution, startDate, endDate, certification;

    public Certification()
    {

    }

    public Certification(String institution, String startDate, String endDate, String certification) {
        this.institution = institution;
        this.startDate = startDate;
        this.endDate = endDate;
        this.certification = certification;
    }

    @Override
    public boolean equals(Object obj) {
        if (!this.institution.equals(((Certification)obj).getInstitution())) {
            return false;
        }
        if (!this.startDate.equals(((Certification)obj).getStartDate())) {
            return false;
        }
        if (!this.endDate.equals(((Certification)obj).getEndDate())) {
            return false;
        }
        return this.certification.equals(((Certification) obj).getCertification());
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getCertification() {
        return certification;
    }

    public void setCertification(String certification) {
        this.certification = certification;

    }
}

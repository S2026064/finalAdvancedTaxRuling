/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.gov.sars.common;

import java.io.Serializable;

/**
 *
 * @author S2026064
 */
public class Router implements Serializable {

    private boolean administrator;
    private boolean application;
    private boolean report;
    private boolean applicant;

    public Router reset() {
        administrator = Boolean.FALSE;
        application = Boolean.FALSE;
        report = Boolean.FALSE;
        applicant = Boolean.FALSE;
        return this;
    }

    public boolean isAdministrator() {
        return administrator;
    }

    public void setAdministrator(boolean administrator) {
        this.administrator = administrator;
    }

    public boolean isApplication() {
        return application;
    }

    public void setApplication(boolean application) {
        this.application = application;
    }

    public boolean isReport() {
        return report;
    }

    public void setReport(boolean report) {
        this.report = report;
    }

    public boolean isApplicant() {
        return applicant;
    }

    public void setApplicant(boolean applicant) {
        this.applicant = applicant;
    }

}

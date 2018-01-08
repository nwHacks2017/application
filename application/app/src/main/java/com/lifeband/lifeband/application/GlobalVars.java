package com.lifeband.lifeband.application;

import com.lifeband.lifeband.PatientData;

public class GlobalVars {

    private PatientData currentPatientData;

    public PatientData getCurrentPatientData() {
        return currentPatientData;
    }

    public void setCurrentPatientData(PatientData currentPatientData) {
        this.currentPatientData = currentPatientData;
    }

}

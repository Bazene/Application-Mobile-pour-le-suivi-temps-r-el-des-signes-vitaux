package com.course.android.ct.moyosafiapp.models.Repository;

import androidx.lifecycle.LiveData;

import com.course.android.ct.moyosafiapp.models.dao.VitalSignRealTimeDao;
import com.course.android.ct.moyosafiapp.models.entity.VitalSignRealTime;

import io.reactivex.rxjava3.core.Flowable;

public class VitalSignRealTimeRepository {

    // VARIABLES
    private VitalSignRealTimeDao vitalSignRealTimeDao;

    // CONSTRUCT
    public VitalSignRealTimeRepository(VitalSignRealTimeDao vitalSignRealTimeDao) {
        this.vitalSignRealTimeDao = vitalSignRealTimeDao;
    }

    // FUNCTION
    // 1- insert vital sign in real time
    public void insertVitalSignRealTime(VitalSignRealTime vitalSignRealTime) {
        vitalSignRealTimeDao.insertVitalSignRealTime(vitalSignRealTime);
    }

    // 2- update vital sign in real time
    public void updateVitalSignRealTime(int id_patient, Float temperature, int heart_rate, int oxygen_level, String vital_hour, String vital_date, int nbrIteration) {
        vitalSignRealTimeDao.updateVitalSignRealTime(id_patient, temperature, heart_rate, oxygen_level, vital_hour, vital_date, nbrIteration);
    }

    public void updateVitalSignRT(VitalSignRealTime vitalSignRealTime) {
        vitalSignRealTimeDao.updateVitalSignRT(vitalSignRealTime);
    }
    // 3- get all vital sign
    public VitalSignRealTime getPatientVitalSignRealTime(int id_patient) {
        return vitalSignRealTimeDao.getPatientVitalSignRealTime(id_patient);
    }

    public LiveData<VitalSignRealTime> getVitalSignRealTimeForUi(){
        return vitalSignRealTimeDao.getVitalSignRealTimeForUi();
    }

    // for observing data using Rx Java
    public Flowable<VitalSignRealTime> observeChanges() {
        return vitalSignRealTimeDao.getNewVitalRealTime();
    }
}

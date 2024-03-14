package com.course.android.ct.moyosafiapp.models.Repository;


import androidx.lifecycle.LiveData;

import com.course.android.ct.moyosafiapp.models.dao.VitalSignDao;
import com.course.android.ct.moyosafiapp.models.entity.VitalSign;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

public class VitalSignRepository {

    // VARIABLES
    private VitalSignDao vitalSignDao;
//    Retrofit retrofit;
//    VitalSignService vitalSignService;

    // CONSTRUCT
    public VitalSignRepository(VitalSignDao vitalSignDao) {
        this.vitalSignDao = vitalSignDao;
//        this.retrofit = RetrofitClientInstance.getInstance(); // we get the instance of retrofit
//        vitalSignService = retrofit.create(VitalSignService.class);
    }

    // FUNCTIONS
    public LiveData<VitalSign> getLastVitalSignForUi(int id_patient) {
        return vitalSignDao.getLastVitalSignForUi(id_patient);
    }

    public VitalSign getLastVitalSignMedium() {
        return vitalSignDao.getLastVitalSignMedium();
    }

    public VitalSign getVitalSignForPatientConnected(int idPatient) {
        return vitalSignDao.getVitalSignForPatientConnected(idPatient);
    }

    public void updateVitalSign(VitalSign vitalSing) {
        vitalSignDao.updateVitalSign(vitalSing);
    }

    public void insertVitalSign(VitalSign vitalSign) {
        vitalSignDao.insert(vitalSign);
    }

    public void insertOtherVitalSign(VitalSign vitalSign) {
        vitalSignDao.insertOtherVitalSign(vitalSign);
    }

    // for observing data using Rx Java
    public Flowable<VitalSign> observeChangesOfVitalSign() {
        return vitalSignDao.getLastVitalSignForCalcul();
    }


    public void deleteVitalSign(VitalSign vitalSign) {
        vitalSignDao.delete(vitalSign);
    }

    // 3-
    public void deleteAllVitalSigns() {
        vitalSignDao.deleteAllVitalSigns();
    }

    // 4-
    public void deleteVitalSignPerHour(String vital_hour) {
        vitalSignDao.deleteVitalSign(vital_hour);
    }

    // 5-
    public LiveData<VitalSign> getVitalSign(int id) {
        return vitalSignDao.getVitalSign(id);
    }

    // 6-
    public  LiveData<List<VitalSign>> getVitalSignsSortedByDateTime() {
        return vitalSignDao.getVitalSignsSortedByDateTime();
    }
}

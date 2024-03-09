package com.course.android.ct.moyosafiapp.models.Repository;


import androidx.lifecycle.LiveData;

import com.course.android.ct.moyosafiapp.models.dao.VitalSignDao;
import com.course.android.ct.moyosafiapp.models.entity.VitalSign;

import java.util.List;

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
    public LiveData<VitalSign> getLastVitalSign() {
        return vitalSignDao.getLastVitalSign();
    }

    // 1-
    public void insertVitalSign(VitalSign vitalSign) {
        vitalSignDao.insert(vitalSign);
    }

    // 2-
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

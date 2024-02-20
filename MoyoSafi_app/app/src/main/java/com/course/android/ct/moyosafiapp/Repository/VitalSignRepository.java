package com.course.android.ct.moyosafiapp.Repository;


import androidx.lifecycle.LiveData;

import com.course.android.ct.moyosafiapp.database.dao.VitalSignDao;
import com.course.android.ct.moyosafiapp.database.models.VitalSign;

import java.util.List;

public class VitalSignRepository {

    // VARIABLES
    private VitalSignDao vitalSignDao;

    // CONSTRUCT
    public VitalSignRepository(VitalSignDao vitalSignDao) {
        this.vitalSignDao = vitalSignDao;
    }

    // FUNCTIONS
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

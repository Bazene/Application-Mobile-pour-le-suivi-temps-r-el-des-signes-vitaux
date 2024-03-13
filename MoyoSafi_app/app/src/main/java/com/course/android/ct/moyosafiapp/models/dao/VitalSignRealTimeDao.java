package com.course.android.ct.moyosafiapp.models.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.course.android.ct.moyosafiapp.models.entity.VitalSignRealTime;

import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface VitalSignRealTimeDao {
    @Insert
    void insertVitalSignRealTime(VitalSignRealTime vitalSignRealTime);

    @Query("UPDATE vitalSignRealTime SET temperature =:temperature, heart_rate = :heart_rate, oxygen_level = :oxygen_level, vital_date = :vital_date, vital_hour = :vital_hour, nbrIteration = :nbrIteration WHERE id_patient = :id_patient")
    void updateVitalSignRealTime(int id_patient, Float temperature, int heart_rate, int oxygen_level, String vital_date, String vital_hour,  int nbrIteration);

    @Update
    void updateVitalSignRT(VitalSignRealTime vitalSignRealTime);

    @Query("SELECT * FROM vitalSignRealTime WHERE id_patient = :id_patient")
    VitalSignRealTime getPatientVitalSignRealTime(int id_patient);

    @Query("SELECT * FROM vitalSignRealTime ORDER BY vital_date DESC, vital_hour DESC LIMIT 1")
    LiveData<VitalSignRealTime> getVitalSignRealTimeForUi();

    @Query("SELECT * FROM vitalSignRealTime ORDER BY vital_date DESC, vital_hour DESC LIMIT 1")
    Flowable<VitalSignRealTime> getNewVitalRealTime();

//    @Delete
//    void deleteAllRealTimeVitalSign(VitalSignRealTime vitalSignRealTime);
}

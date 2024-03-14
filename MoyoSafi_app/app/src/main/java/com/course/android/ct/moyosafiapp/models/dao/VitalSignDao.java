package com.course.android.ct.moyosafiapp.models.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.course.android.ct.moyosafiapp.models.entity.VitalSign;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface VitalSignDao {
    @Insert
    void insert(VitalSign vitalSign);

    @Insert
    void insertOtherVitalSign(VitalSign vitalSign);

    @Delete
    void delete(VitalSign vitalSign);

    @Query("DELETE FROM vitalsign")
    void deleteAllVitalSigns();

    @Query("DELETE FROM vitalsign WHERE vital_hour = :vital_hour")
    void deleteVitalSign(String vital_hour);

    @Query("SELECT * FROM vitalsign WHERE id=:id")
    LiveData<VitalSign> getVitalSign(int id);

    @Query("SELECT * FROM vitalsign ORDER BY vital_date DESC, vital_hour DESC")
    LiveData<List<VitalSign>> getVitalSignsSortedByDateTime();

    @Query("SELECT * FROM vitalsign WHERE id_patient=:id_patient ORDER BY vital_date DESC, vital_hour DESC LIMIT 1")
    LiveData<VitalSign> getLastVitalSignForUi(int id_patient);

    @Query("SELECT * FROM vitalsign WHERE id_patient = :id_patient ORDER BY vital_date DESC, vital_hour DESC LIMIT 1")
    VitalSign getVitalSignForPatientConnected(int id_patient);

    @Query("SELECT * FROM vitalsign ORDER BY vital_date DESC, vital_hour DESC LIMIT 1")
    VitalSign getLastVitalSignMedium();

    @Query("SELECT * FROM vitalsign ORDER BY vital_date DESC, vital_hour DESC LIMIT 1")
    Flowable<VitalSign> getLastVitalSignForCalcul();

    @Update
    void updateVitalSign(VitalSign vitalSign);
}
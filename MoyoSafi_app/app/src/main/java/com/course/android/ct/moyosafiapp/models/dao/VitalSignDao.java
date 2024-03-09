package com.course.android.ct.moyosafiapp.models.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.course.android.ct.moyosafiapp.models.entity.VitalSign;

import java.util.List;

@Dao
public interface VitalSignDao {
    @Insert
    void insert(VitalSign vitalSign);

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

    @Query("SELECT * FROM vitalsign ORDER BY vital_date DESC, vital_hour DESC LIMIT 1")
    LiveData<VitalSign> getLastVitalSign();
}

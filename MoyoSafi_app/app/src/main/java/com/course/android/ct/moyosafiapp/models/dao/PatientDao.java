package com.course.android.ct.moyosafiapp.models.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.course.android.ct.moyosafiapp.models.entity.Patient;

import java.util.List;

@Dao
public interface PatientDao {

    @Insert
    void insertPatient(Patient patient);

    @Update
    void updatePatient(Patient patient);

    @Delete
    void deletePatient(Patient patient);

    @Query("DELETE FROM patient")
    void deleteAllPatients();

    @Query("SELECT * FROM patient WHERE id=:id")
    LiveData<Patient> getPatient(int id);

    @Query("SELECT * FROM patient ORDER BY patient_date_created DESC")
    LiveData<List<Patient>> getPatientsSortedByDate();
}

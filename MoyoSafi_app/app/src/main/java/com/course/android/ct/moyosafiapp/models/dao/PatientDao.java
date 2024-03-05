package com.course.android.ct.moyosafiapp.models.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.course.android.ct.moyosafiapp.models.entity.Patient;

import java.util.List;

@Dao
public interface PatientDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPatient(Patient patient);

    @Update
    void updatePatient(Patient patient);

    @Query("UPDATE patient SET patient_role = :role WHERE patient_name = :patient_name AND patient_password = :patient_password")
    void updatePatientToken(String role, String patient_name, String patient_password);

    @Delete
    void deletePatient(Patient patient);

    @Query("DELETE FROM patient")
    void deleteAllPatients();

    @Query("SELECT * FROM patient WHERE id=:id")
    LiveData<Patient> getPatient(int id);

    @Query("SELECT * FROM patient")
    List<Patient> getAllPatientsForRepository();

    @Query("SELECT * FROM patient")
    LiveData<List<Patient>> getAllPatients();
}

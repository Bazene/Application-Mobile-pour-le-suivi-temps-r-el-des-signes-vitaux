package com.course.android.ct.moyosafiapp.models.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.course.android.ct.moyosafiapp.models.entity.Patient;

import java.util.List;

@Dao
public interface PatientDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPatient(Patient patient);

    @Query("UPDATE patient SET patient_role = :role WHERE patient_name = :patient_name AND patient_password = :patient_password")
    void updatePatientToken(String role, String patient_name, String patient_password);

    @Delete
    void deletePatient(Patient patient);

    @Query("DELETE FROM patient")
    void deleteAllPatients();

    @Query("SELECT * FROM patient WHERE patient_role =:patient_role")
    LiveData<Patient> getPatient(String patient_role);

    @Query("SELECT * FROM patient")
    List<Patient> getAllPatientsForRepository();

    @Query("SELECT * FROM patient")
    LiveData<List<Patient>> getAllPatients();

    @Query("SELECT id FROM patient WHERE patient_role = :patient_role")
    int getIdPatient(String patient_role);

    @Query("UPDATE patient SET patient_name=:userName, patient_phone_number=:phoneNumber, patient_age=:userAge, patient_weight=:userWeight, patient_size=:userSize, patient_commune=:userCommune, patient_quater=:userQuater,  patient_gender=:userGender WHERE patient_role=:token")
    void updateProfilePatient(String token, String userName, String phoneNumber, int userAge, long userWeight, long userSize, String userCommune, String userQuater, String userGender);

    @Query("UPDATE patient SET patient_picture = :imageString WHERE patient_role = :token")
    void updatePatientPicture(String token, byte[] imageString);
}
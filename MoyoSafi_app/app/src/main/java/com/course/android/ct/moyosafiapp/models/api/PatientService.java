package com.course.android.ct.moyosafiapp.models.api;

import com.course.android.ct.moyosafiapp.models.entity.Patient;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;

public interface PatientService {

   @POST("Controllers/API/api_user_create_account.php")
   Call<CreateAccountResponse> createPatient(@Body Patient patient);

   @POST("Controllers/API/api_log_patient.php")
   Call<LogPatientResponse> logPatient(@Body Map<String, String> patientData);

   @POST("Controllers/API/api_updatePatient.php")
   Call<UpdatePatientResponse> updatePatientProfileApi(@Body Map<String, String> patientProfile);

   @Multipart
   @POST("Controllers/API/api_updatePatientPicture.php")
   Call<UpdatePatientPictureResponse> updatePatientPictureApi(@PartMap Map<String, RequestBody> pictureRequestBodyMap);

   @GET("Controllers/getAllPatients")
   Call<List<Patient>> getAllPatients();
}

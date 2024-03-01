package com.course.android.ct.moyosafiapp.models.api;

import com.course.android.ct.moyosafiapp.models.entity.Patient;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface PatientService {

   @POST("Controllers/API/api_user_authentification.php")
   Call<CreateAccountResponse> createPatient(@Body Patient patient);

   @GET("Controllers/getAllPatients")
   Call<List<Patient>> getAllPatients();
}

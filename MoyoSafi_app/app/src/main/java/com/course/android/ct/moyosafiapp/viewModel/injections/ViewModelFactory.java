package com.course.android.ct.moyosafiapp.viewModel.injections;

import android.content.Context;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.course.android.ct.moyosafiapp.models.MoyoSafiDatabase;
import com.course.android.ct.moyosafiapp.models.Repository.NotificationsRepository;
import com.course.android.ct.moyosafiapp.models.Repository.PatientRepository;
import com.course.android.ct.moyosafiapp.models.Repository.VitalSignRepository;
import com.course.android.ct.moyosafiapp.viewModel.PatientViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ViewModelFactory implements ViewModelProvider.Factory {
    //----------------------------------------------------
    // VARIABLES
    //----------------------------------------------------

    // repositories
    private final PatientRepository patientRepository;
    private final NotificationsRepository notificationsRepository;
    private final VitalSignRepository vitalSignRepository;

    // executor
    private final ExecutorService executor;
    // factory
    private static ViewModelFactory factory;

    //----------------------------------------------------
    // CONSTRUCT
    //----------------------------------------------------
    public ViewModelFactory (Context context) {
        MoyoSafiDatabase moyoSafiDatabase = MoyoSafiDatabase.getInstance(context); // get instance of our database
        this.patientRepository = new PatientRepository(moyoSafiDatabase.patientDao());
        this.notificationsRepository = new NotificationsRepository(moyoSafiDatabase.notificationsDao());
        this.vitalSignRepository = new VitalSignRepository(moyoSafiDatabase.vitalSignDao());
        this.executor = Executors.newSingleThreadExecutor();
    }

    //----------------------------------------------------
    // FUNCTIONS
    //----------------------------------------------------

    // 1- first function
    public static ViewModelFactory getInstance(Context context) {
        if (factory == null) {
            synchronized (ViewModelFactory.class) {
                if(factory == null) {
                    factory = new ViewModelFactory(context);
                }
            }
        }
        return factory;
    }

    // 2- second function
    @Override
    @NotNull
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if(modelClass.isAssignableFrom(PatientViewModel.class)) {
            return (T) new PatientViewModel(patientRepository, notificationsRepository, vitalSignRepository,executor);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
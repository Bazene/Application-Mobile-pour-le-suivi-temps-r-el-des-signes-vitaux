package com.course.android.ct.moyosafiapp.models;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.course.android.ct.moyosafiapp.models.dao.NotificationsDao;
import com.course.android.ct.moyosafiapp.models.dao.PatientDao;
import com.course.android.ct.moyosafiapp.models.dao.VitalSignDao;
import com.course.android.ct.moyosafiapp.models.entity.Notifications;
import com.course.android.ct.moyosafiapp.models.entity.Patient;
import com.course.android.ct.moyosafiapp.models.entity.VitalSign;

@Database(entities = {Patient.class, VitalSign.class, Notifications.class}, version = 4, exportSchema = false)
public abstract class MoyoSafiDatabase extends RoomDatabase {

    // 1. SINGLETON OF OUR DATABASE (the reason of volatile)
    private static volatile MoyoSafiDatabase INSTANCE;

    // 2. DAO
    public abstract PatientDao patientDao();
    public abstract NotificationsDao notificationsDao();
    public abstract VitalSignDao vitalSignDao();

    // 3. INSTANCE DATA BASE FOR MOYOSAFI APP (MoyoSafiDatabase IS THE RETURNED TYPE, WISH WILL BE CREATED ONCE)
    public static MoyoSafiDatabase getInstance(Context context) {
        if(INSTANCE == null) {
            synchronized (MoyoSafiDatabase.class) { // Pour synchroniser une seule fois
                if(INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MoyoSafiDatabase.class, "MoyoSafi_MyDatabase.db")
//                            .addCallback(prepopulateDatabase()) // for test method here below
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    // METHOD FOR THE TEST
//    private static Callback prepopulateDatabase() {
//        return new Callback() {
//            @Override
//            public void onCreate(@NonNull SupportSQLiteDatabase db) {
//                super.onCreate(db);
//
//                Executors.newSingleThreadExecutor().execute(()-> {
//                    INSTANCE.patientDao().insertPatient(new Patient("BAZENE", "SERGE", "Amos", "Mascunlin", "bazenesergeamos0@gmail.com", "1234", "19-02-2024",22, "user" ));;
//                });
//
//            }
//        };
//    }
}
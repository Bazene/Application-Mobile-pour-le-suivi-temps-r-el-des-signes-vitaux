package com.course.android.ct.moyosafiapp.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.course.android.ct.moyosafiapp.database.dao.NotificationsDao;
import com.course.android.ct.moyosafiapp.database.dao.PatientDao;
import com.course.android.ct.moyosafiapp.database.dao.VitalSignDao;
import com.course.android.ct.moyosafiapp.database.models.Notifications;
import com.course.android.ct.moyosafiapp.database.models.Patient;
import com.course.android.ct.moyosafiapp.database.models.VitalSign;

import java.util.concurrent.Executors;

@Database(entities = {Patient.class, VitalSign.class, Notifications.class}, version = 1, exportSchema = false)
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
                            .addCallback(prepopulateDatabase())
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    // METHOD FOR THE TEST
    private static Callback prepopulateDatabase() {
        return new Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);

                Executors.newSingleThreadExecutor().execute(()-> {
                    INSTANCE.patientDao().insertPatient(new Patient("BAZENE", "SERGE", "Amos", "Mascunlin", "bazenesergeamos0@gmail.com", 975149026, "1234", "19-02-2024",22 ));;
                });

            }
        };
    }
}
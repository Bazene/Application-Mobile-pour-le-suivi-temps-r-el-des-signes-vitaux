//package com.course.android.ct.moyosafiapp.database;
//
//import android.content.Context;
//
//import androidx.room.Database;
//import androidx.room.Room;
//import androidx.room.RoomDatabase;
//
//import com.course.android.ct.moyosafiapp.database.dao.UserDao;
//import com.course.android.ct.moyosafiapp.models.Doctor;
//import com.course.android.ct.moyosafiapp.models.Notifications;
//import com.course.android.ct.moyosafiapp.models.Patient;
//import com.course.android.ct.moyosafiapp.models.Tuteur;
//import com.course.android.ct.moyosafiapp.models.User;
//import com.course.android.ct.moyosafiapp.models.VitalSign;
//
//@Database(entities = {Doctor.class, Notifications.class, Patient.class, Tuteur.class, User.class, VitalSign.class }, version = 1, exportSchema = false)
//public abstract class MoyoSafiDatabase extends RoomDatabase {
//
//    // 1. SINGLETON
//    private static volatile MoyoSafiDatabase INSTANCE;
//
//    // 2. DAO
//    public abstract UserDao userDao();
//
//    // 3. INSTANCE DATA BASE FOR MOYOSAFI APP (MoyoSafiDatabase IS THE RETURNED TYPE, WISH WILL BE CREATED ONCE)
//    private static MoyoSafiDatabase getInstance(Context context) {
//        if(INSTANCE == null) {
//            synchronized (MoyoSafiDatabase.class) { // Pour synchroniser une seule fois
//                if(INSTANCE == null) {
//                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
//                            MoyoSafiDatabase.class, "MoyoSafi_MyDatabase.db").build();
//                }
//            }
//        }
//        return INSTANCE;
//    }
//
//}

package com.course.android.ct.moyosafiapp.models.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.course.android.ct.moyosafiapp.models.entity.Notifications;

import java.util.List;

@Dao
public interface NotificationsDao {
    @Insert
    void insert(Notifications notifications);

    @Query("SELECT * FROM notifications ORDER BY notification_date DESC, notification_time DESC")
    LiveData<List<Notifications>> getAllNotifications();

    @Delete
    void deleteNotification(Notifications notifications);

    @Query("DELETE FROM notifications")
    void deleteAllNotifications();

    @Query("DELETE FROM notifications WHERE notification_date = :notification_date")
    void deleteNotificationPerDate(String notification_date);

    @Query("SELECT * FROM notifications WHERE id=:id")
    LiveData<Notifications> getNotification(int id);
}

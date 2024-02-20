package com.course.android.ct.moyosafiapp.Repository;

import androidx.lifecycle.LiveData;

import com.course.android.ct.moyosafiapp.database.dao.NotificationsDao;
import com.course.android.ct.moyosafiapp.database.models.Notifications;

import java.util.List;

public class NotificationsRepository {

    // VARIABLES
    private NotificationsDao notificationsDao;

    // CONSTRUCT
    public NotificationsRepository(NotificationsDao notificationsDao) {
        this.notificationsDao = notificationsDao;
    }

    // FUNCTIONS
    // 1-
    public void insertNotifications(Notifications notification) {
        notificationsDao.insert(notification);
    }

    // 2-
    public void deleteNotifications(Notifications notification) {
        notificationsDao.deleteNotification(notification);
    }

    // 3-
    public void deleteNotificationPerDate(String notification_date) {
        notificationsDao.deleteNotificationPerDate(notification_date);
    }

    // 4-
    public void deleteAllNotifications() {
        notificationsDao.deleteAllNotifications();
    }

    // 5-
    public LiveData<Notifications> getNotification(int id) {
        return notificationsDao.getNotification(id);
    }

    // 6-
    public LiveData<List<Notifications>> getNotificationsSortedByDateTime() {
        return notificationsDao.getNotificationsSortedByDateTime();
    }

}

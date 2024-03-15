package com.course.android.ct.moyosafiapp.models.Repository;

import androidx.lifecycle.LiveData;

import com.course.android.ct.moyosafiapp.models.dao.NotificationsDao;
import com.course.android.ct.moyosafiapp.models.entity.Notifications;

import java.util.List;

public class NotificationsRepository {

    // VARIABLES
    private NotificationsDao notificationsDao;

    // CONSTRUCT
    public NotificationsRepository(NotificationsDao notificationsDao) {
        this.notificationsDao = notificationsDao;
    }

    // FUNCTIONS
    public void insertNotifications(Notifications notification) {
        notificationsDao.insert(notification);
    }

    public LiveData<List<Notifications>> getAllNotifications() {
        return notificationsDao.getAllNotifications();
    }

    public void deleteNotifications(Notifications notification) {
        notificationsDao.deleteNotification(notification);
    }

    public void deleteNotificationPerDate(String notification_date) {
        notificationsDao.deleteNotificationPerDate(notification_date);
    }

    public void deleteAllNotifications() {
        notificationsDao.deleteAllNotifications();
    }

    public LiveData<Notifications> getNotification(int id) {
        return notificationsDao.getNotification(id);
    }

}

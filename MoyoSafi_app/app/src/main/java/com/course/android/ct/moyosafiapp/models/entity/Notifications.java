package com.course.android.ct.moyosafiapp.models.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;


@Entity(tableName = "notifications",
        foreignKeys = @ForeignKey(entity = Notifications.class, parentColumns="id", childColumns="id_patient"))
public class Notifications {

    // VARIABLES
    @PrimaryKey (autoGenerate = true)
    private int id;

    private int id_patient;
    private String notification_content;
    private String notification_date;
    private String notification_time;


    // CONSTRUCT
    public Notifications(int id_patient, String notification_content, String notification_date, String notification_time) {
        this.id_patient = id_patient;
        this.notification_content = notification_content;
        this.notification_date = notification_date;
        this.notification_time = notification_time;
    }

    // GETTERS AND SETTERS
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_patient() {
        return id_patient;
    }

    public void setId_patient(int id_patient) {
        this.id_patient = id_patient;
    }

    public String getNotification_content() {
        return notification_content;
    }

    public void setNotification_content(String notification_content) {
        this.notification_content = notification_content;
    }

    public String getNotification_date() {
        return notification_date;
    }

    public void setNotification_date(String notification_date) {
        this.notification_date = notification_date;
    }

    public String getNotification_time() {
        return notification_time;
    }

    public void setNotification_time(String notification_time) {
        this.notification_time = notification_time;
    }
}
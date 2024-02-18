//package com.course.android.ct.moyosafiapp.models;
//
//import androidx.room.Entity;
//import androidx.room.ForeignKey;
//import androidx.room.PrimaryKey;
//
//import java.util.Date;
//
//@Entity(foreignKeys = @ForeignKey(entity = User.class, parentColumns = "_id", childColumns = "id_user"))
//public class Notifications {
//
//    // VARIABLES
//    @PrimaryKey(autoGenerate = true)
//    private long id;
//    private long sender_id;
//    private long recipient_id;
//    private long user_id;
//    private String notification_content;
//    private Date notification_date;
//    private String notification_hour;
//
//    public void Notifications(long sender_id, long recipient_id, long user_id,String notification_content ,Date notification_date, String notification_hour) {
//        this.sender_id = sender_id;
//        this.recipient_id = recipient_id;
//        this.user_id = user_id;
//        this.notification_content = notification_content;
//        this.notification_date = notification_date;
//        this.notification_hour = notification_hour;
//    }
//
//    // GETTERS AND SETTERS
//    public long getId() {
//        return id;
//    }
//
//    public void setId(long id) {
//        this.id = id;
//    }
//
//    public long getUser_id() {
//        return user_id;
//    }
//
//    public void setUser_id(long user_id) {
//        this.user_id = user_id;
//    }
//
//    public Date getNotification_date() {
//        return notification_date;
//    }
//
//    public void setNotification_date(Date notification_date) {this.notification_date = notification_date;}
//
//    public String getNotification_hour() {
//        return notification_hour;
//    }
//
//    public void setNotification_hour(String notification_hour) {this.notification_hour = notification_hour;}
//
//    public long getSender_id() {return sender_id;}
//
//    public void setSender_id(long sender_id) {this.sender_id = sender_id;}
//
//    public long getRecipient_id() {return recipient_id;}
//
//    public void setRecipient_id(long recipient_id) {this.recipient_id = recipient_id;}
//
//    public String getNotification_content() {return notification_content;}
//
//    public void setNotification_content(String notification_content) {this.notification_content = notification_content;}
//}

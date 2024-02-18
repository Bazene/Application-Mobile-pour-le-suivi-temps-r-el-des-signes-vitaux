//package com.course.android.ct.moyosafiapp.models;
//
//import androidx.annotation.NonNull;
//import androidx.room.Entity;
//import androidx.room.PrimaryKey;
//
//import java.util.Date;
//
//@Entity
//public class User {
//    @PrimaryKey(autoGenerate = true)
//    @NonNull
//    private long id;
//    private String user_name; // required on created an account
//    private String postname; // required on created an account
//    private String surname; // required on created an accountrequired
//    private String gender; // required on created an accountrequired
//    private String mail; // required on created an account
//    private long phone_number; // required on created an account
//    private String user_password;
//    private String profil_picture;
//    private String commune;
//    private String quater;
//    private String street;
//    private String house_number;
//    private Date date_created;
//    private long day_of_birth; // required on created an account
//    private long month_of_birth; // required on created an account
//    private long year_of_birth; // required on created an account
//
//    public User(String use_name, String postname, String surname, long day_of_birth, long month_of_birth, long year_of_birth, String gender, long phone_number, String mail) {
//        this.user_name = user_name;
//        this.postname = postname;
//        this.surname = surname;
//        this.day_of_birth = day_of_birth;
//        this.month_of_birth = month_of_birth;
//        this.year_of_birth = year_of_birth;
//        this.gender = gender;
//        this.phone_number = phone_number;
//        this.mail = mail;
//    }
//
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
//    public String getUser_name() {
//        return user_name;
//    }
//
//    public void setUser_name(String user_name) {
//        this.user_name = user_name;
//    }
//
//    public String getPostname() {
//        return postname;
//    }
//
//    public void setPostname(String postname) {
//        this.postname = postname;
//    }
//
//    public String getSurname() {
//        return surname;
//    }
//
//    public void setSurname(String surname) {
//        this.surname = surname;
//    }
//
//    public long getDay_of_birth() {
//        return day_of_birth;
//    }
//
//    public void setDay_of_birth(long day_of_birth) {
//        this.day_of_birth = day_of_birth;
//    }
//
//    public long getMonth_of_birth() {
//        return month_of_birth;
//    }
//
//    public void setMonth_of_birth(long month_of_birth) {
//        this.month_of_birth = month_of_birth;
//    }
//
//    public long getYear_of_birth() {
//        return year_of_birth;
//    }
//
//    public void setYear_of_birth(long year_of_birth) {
//        this.year_of_birth = year_of_birth;
//    }
//
//    public String getGender() {
//        return gender;
//    }
//
//    public void setGender(String gender) {
//        this.gender = gender;
//    }
//
//    public long getPhone_number() {
//        return phone_number;
//    }
//
//    public void setPhone_number(long phone_number) {
//        this.phone_number = phone_number;
//    }
//
//    public String getMail() {
//        return mail;
//    }
//
//    public void setMail(String mail) {
//        this.mail = mail;
//    }
//
//    public String getUser_password() {
//        return user_password;
//    }
//
//    public void setUser_password(String user_password) {
//        this.user_password = user_password;
//    }
//
//    public String getProfil_picture() {
//        return profil_picture;
//    }
//
//    public void setProfil_picture(String profil_picture) {
//        this.profil_picture = profil_picture;
//    }
//
//    public String getQuater() {
//        return quater;
//    }
//
//    public void setQuater(String quater) {
//        this.quater = quater;
//    }
//
//    public String getCommune() {
//        return commune;
//    }
//
//    public void setCommune(String commune) {
//        this.commune = commune;
//    }
//
//    public String getStreet() {
//        return street;
//    }
//
//    public void setStreet(String street) {
//        this.street = street;
//    }
//
//    public String getHouse_number() {
//        return house_number;
//    }
//
//    public void setHouse_number(String house_number) {
//        this.house_number = house_number;
//    }
//
//    public Date getDate_created() {
//        return date_created;
//    }
//
//    public void setDate_created(Date date_created) {
//        this.date_created = date_created;
//    }
//}

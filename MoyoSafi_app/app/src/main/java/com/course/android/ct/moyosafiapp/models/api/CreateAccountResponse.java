package com.course.android.ct.moyosafiapp.models.api;

import com.google.gson.annotations.SerializedName;

public class CreateAccountResponse {
    // vairable returned
    @SerializedName("success")
    private Boolean success;

    @SerializedName("error")
    private String error;

    // GETTERS AND SETTERS
    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}

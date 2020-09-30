
package com.dpk.hammoq.GsonModule.SettingLoginClientPassModule;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Password {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("website")
    @Expose
    private String website;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("password")
    @Expose
    private String password;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}

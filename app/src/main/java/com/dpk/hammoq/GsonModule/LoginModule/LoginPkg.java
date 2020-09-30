
package com.dpk.hammoq.GsonModule.LoginModule;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginPkg {

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("err")
    @Expose
    private String err;

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}

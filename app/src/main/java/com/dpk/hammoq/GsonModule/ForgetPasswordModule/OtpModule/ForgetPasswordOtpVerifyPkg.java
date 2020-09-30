
package com.dpk.hammoq.GsonModule.ForgetPasswordModule.OtpModule;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ForgetPasswordOtpVerifyPkg {

    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("err")
    @Expose
    private String err;
    @SerializedName("assert")
    @Expose
    private Boolean _assert;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }

    public Boolean getAssert() {
        return _assert;
    }

    public void setAssert(Boolean _assert) {
        this._assert = _assert;
    }

}

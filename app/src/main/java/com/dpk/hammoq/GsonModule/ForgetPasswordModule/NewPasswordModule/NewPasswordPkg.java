
package com.dpk.hammoq.GsonModule.ForgetPasswordModule.NewPasswordModule;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewPasswordPkg {

    @SerializedName("data")
    @Expose
    private String data;
    @SerializedName("err")
    @Expose
    private String err;
    @SerializedName("assert")
    @Expose
    private Boolean _assert;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
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

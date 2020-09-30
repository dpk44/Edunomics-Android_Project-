
package com.dpk.hammoq.GsonModule.ChangePasswordModule;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChangePasswordPkg {

    @SerializedName("err")
    @Expose
    private String err;

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }

}

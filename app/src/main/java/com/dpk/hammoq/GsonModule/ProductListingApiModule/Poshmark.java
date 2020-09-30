
package com.dpk.hammoq.GsonModule.ProductListingApiModule;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Poshmark {

    @SerializedName("check")
    @Expose
    private Boolean check;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("title")
    @Expose
    private String title;

    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}

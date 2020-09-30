
package com.dpk.hammoq.GsonModule.ProductListingApiModule;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Images {

    @SerializedName("default_image")
    @Expose
    private String defaultImage;
    @SerializedName("brand_image")
    @Expose
    private String brandImage;
    @SerializedName("model_image")
    @Expose
    private Object modelImage;
    @SerializedName("side1_image")
    @Expose
    private Object side1Image;
    @SerializedName("side2_image")
    @Expose
    private Object side2Image;
    @SerializedName("front_image")
    @Expose
    private Object frontImage;
    @SerializedName("back_image")
    @Expose
    private Object backImage;
    @SerializedName("condition1_image")
    @Expose
    private Object condition1Image;
    @SerializedName("condition2_image")
    @Expose
    private Object condition2Image;
    @SerializedName("condition3_image")
    @Expose
    private Object condition3Image;
    @SerializedName("condition4_image")
    @Expose
    private Object condition4Image;
    @SerializedName("condition5_image")
    @Expose
    private Object condition5Image;

    public String getDefaultImage() {
        return defaultImage;
    }

    public void setDefaultImage(String defaultImage) {
        this.defaultImage = defaultImage;
    }

    public String getBrandImage() {
        return brandImage;
    }

    public void setBrandImage(String brandImage) {
        this.brandImage = brandImage;
    }

    public Object getModelImage() {
        return modelImage;
    }

    public void setModelImage(Object modelImage) {
        this.modelImage = modelImage;
    }

    public Object getSide1Image() {
        return side1Image;
    }

    public void setSide1Image(Object side1Image) {
        this.side1Image = side1Image;
    }

    public Object getSide2Image() {
        return side2Image;
    }

    public void setSide2Image(Object side2Image) {
        this.side2Image = side2Image;
    }

    public Object getFrontImage() {
        return frontImage;
    }

    public void setFrontImage(Object frontImage) {
        this.frontImage = frontImage;
    }

    public Object getBackImage() {
        return backImage;
    }

    public void setBackImage(Object backImage) {
        this.backImage = backImage;
    }

    public Object getCondition1Image() {
        return condition1Image;
    }

    public void setCondition1Image(Object condition1Image) {
        this.condition1Image = condition1Image;
    }

    public Object getCondition2Image() {
        return condition2Image;
    }

    public void setCondition2Image(Object condition2Image) {
        this.condition2Image = condition2Image;
    }

    public Object getCondition3Image() {
        return condition3Image;
    }

    public void setCondition3Image(Object condition3Image) {
        this.condition3Image = condition3Image;
    }

    public Object getCondition4Image() {
        return condition4Image;
    }

    public void setCondition4Image(Object condition4Image) {
        this.condition4Image = condition4Image;
    }

    public Object getCondition5Image() {
        return condition5Image;
    }

    public void setCondition5Image(Object condition5Image) {
        this.condition5Image = condition5Image;
    }

}

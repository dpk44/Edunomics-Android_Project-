
package com.dpk.hammoq.GsonModule.ProductListingApiModule;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Product {

    @SerializedName("images")
    @Expose
    private Images images;
    @SerializedName("mercari")
    @Expose
    private Mercari mercari;
    @SerializedName("poshmark")
    @Expose
    private Poshmark poshmark;
    @SerializedName("ebay")
    @Expose
    private Ebay ebay;
    @SerializedName("delist")
    @Expose
    private Delist delist;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("size")
    @Expose
    private String size;
    @SerializedName("brand")
    @Expose
    private String brand;
    @SerializedName("colorShade")
    @Expose
    private String colorShade;
    @SerializedName("material")
    @Expose
    private String material;
    @SerializedName("pattern")
    @Expose
    private String pattern;
    @SerializedName("seasonOrWeather")
    @Expose
    private String seasonOrWeather;
    @SerializedName("care")
    @Expose
    private String care;
    @SerializedName("madeIn")
    @Expose
    private String madeIn;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("waist")
    @Expose
    private String waist;
    @SerializedName("inseam")
    @Expose
    private String inseam;
    @SerializedName("rise")
    @Expose
    private String rise;
    @SerializedName("bottomDescription")
    @Expose
    private String bottomDescription;
    @SerializedName("price")
    @Expose
    private Integer price;
    @SerializedName("mrp")
    @Expose
    private Integer mrp;
    @SerializedName("msrp")
    @Expose
    private Integer msrp;
    @SerializedName("sku")
    @Expose
    private String sku;
    @SerializedName("upc")
    @Expose
    private String upc;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;
    @SerializedName("weightLB")
    @Expose
    private Integer weightLB;
    @SerializedName("weightOZ")
    @Expose
    private Integer weightOZ;
    @SerializedName("zipCode")
    @Expose
    private String zipCode;
    @SerializedName("packageLength")
    @Expose
    private Integer packageLength;
    @SerializedName("packageWidth")
    @Expose
    private Integer packageWidth;
    @SerializedName("packageHeight")
    @Expose
    private Integer packageHeight;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("shortDescription")
    @Expose
    private String shortDescription;
    @SerializedName("listed")
    @Expose
    private Boolean listed;
    @SerializedName("condition_name")
    @Expose
    private String conditionName;
    @SerializedName("keywords")
    @Expose
    private String keywords;
    @SerializedName("model")
    @Expose
    private String model;
    @SerializedName("modelNo")
    @Expose
    private String modelNo;
    @SerializedName("note")
    @Expose
    private String note;
    @SerializedName("costOfGoods")
    @Expose
    private String costOfGoods;
    @SerializedName("shippingFees")
    @Expose
    private String shippingFees;
    @SerializedName("profit")
    @Expose
    private Integer profit;
    @SerializedName("style")
    @Expose
    private String style;
    @SerializedName("activity")
    @Expose
    private String activity;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("manualProdList")
    @Expose
    private Boolean manualProdList;
    @SerializedName("manualProdListBy")
    @Expose
    private String manualProdListBy;
    @SerializedName("ebayUrl")
    @Expose
    private String ebayUrl;
    @SerializedName("poshMarkUrl")
    @Expose
    private String poshMarkUrl;
    @SerializedName("mercariUrl")
    @Expose
    private String mercariUrl;
    @SerializedName("prodStatus")
    @Expose
    private String prodStatus;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("others")
    @Expose
    private String others;
    @SerializedName("extraMeasures")
    @Expose
    private String extraMeasures;
    @SerializedName("line")
    @Expose
    private String line;

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }

    public Mercari getMercari() {
        return mercari;
    }

    public void setMercari(Mercari mercari) {
        this.mercari = mercari;
    }

    public Poshmark getPoshmark() {
        return poshmark;
    }

    public void setPoshmark(Poshmark poshmark) {
        this.poshmark = poshmark;
    }

    public Ebay getEbay() {
        return ebay;
    }

    public void setEbay(Ebay ebay) {
        this.ebay = ebay;
    }

    public Delist getDelist() {
        return delist;
    }

    public void setDelist(Delist delist) {
        this.delist = delist;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getColorShade() {
        return colorShade;
    }

    public void setColorShade(String colorShade) {
        this.colorShade = colorShade;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getSeasonOrWeather() {
        return seasonOrWeather;
    }

    public void setSeasonOrWeather(String seasonOrWeather) {
        this.seasonOrWeather = seasonOrWeather;
    }

    public String getCare() {
        return care;
    }

    public void setCare(String care) {
        this.care = care;
    }

    public String getMadeIn() {
        return madeIn;
    }

    public void setMadeIn(String madeIn) {
        this.madeIn = madeIn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWaist() {
        return waist;
    }

    public void setWaist(String waist) {
        this.waist = waist;
    }

    public String getInseam() {
        return inseam;
    }

    public void setInseam(String inseam) {
        this.inseam = inseam;
    }

    public String getRise() {
        return rise;
    }

    public void setRise(String rise) {
        this.rise = rise;
    }

    public String getBottomDescription() {
        return bottomDescription;
    }

    public void setBottomDescription(String bottomDescription) {
        this.bottomDescription = bottomDescription;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getMrp() {
        return mrp;
    }

    public void setMrp(Integer mrp) {
        this.mrp = mrp;
    }

    public Integer getMsrp() {
        return msrp;
    }

    public void setMsrp(Integer msrp) {
        this.msrp = msrp;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getWeightLB() {
        return weightLB;
    }

    public void setWeightLB(Integer weightLB) {
        this.weightLB = weightLB;
    }

    public Integer getWeightOZ() {
        return weightOZ;
    }

    public void setWeightOZ(Integer weightOZ) {
        this.weightOZ = weightOZ;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public Integer getPackageLength() {
        return packageLength;
    }

    public void setPackageLength(Integer packageLength) {
        this.packageLength = packageLength;
    }

    public Integer getPackageWidth() {
        return packageWidth;
    }

    public void setPackageWidth(Integer packageWidth) {
        this.packageWidth = packageWidth;
    }

    public Integer getPackageHeight() {
        return packageHeight;
    }

    public void setPackageHeight(Integer packageHeight) {
        this.packageHeight = packageHeight;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public Boolean getListed() {
        return listed;
    }

    public void setListed(Boolean listed) {
        this.listed = listed;
    }

    public String getConditionName() {
        return conditionName;
    }

    public void setConditionName(String conditionName) {
        this.conditionName = conditionName;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getModelNo() {
        return modelNo;
    }

    public void setModelNo(String modelNo) {
        this.modelNo = modelNo;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCostOfGoods() {
        return costOfGoods;
    }

    public void setCostOfGoods(String costOfGoods) {
        this.costOfGoods = costOfGoods;
    }

    public String getShippingFees() {
        return shippingFees;
    }

    public void setShippingFees(String shippingFees) {
        this.shippingFees = shippingFees;
    }

    public Integer getProfit() {
        return profit;
    }

    public void setProfit(Integer profit) {
        this.profit = profit;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Boolean getManualProdList() {
        return manualProdList;
    }

    public void setManualProdList(Boolean manualProdList) {
        this.manualProdList = manualProdList;
    }

    public String getManualProdListBy() {
        return manualProdListBy;
    }

    public void setManualProdListBy(String manualProdListBy) {
        this.manualProdListBy = manualProdListBy;
    }

    public String getEbayUrl() {
        return ebayUrl;
    }

    public void setEbayUrl(String ebayUrl) {
        this.ebayUrl = ebayUrl;
    }

    public String getPoshMarkUrl() {
        return poshMarkUrl;
    }

    public void setPoshMarkUrl(String poshMarkUrl) {
        this.poshMarkUrl = poshMarkUrl;
    }

    public String getMercariUrl() {
        return mercariUrl;
    }

    public void setMercariUrl(String mercariUrl) {
        this.mercariUrl = mercariUrl;
    }

    public String getProdStatus() {
        return prodStatus;
    }

    public void setProdStatus(String prodStatus) {
        this.prodStatus = prodStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others;
    }

    public String getExtraMeasures() {
        return extraMeasures;
    }

    public void setExtraMeasures(String extraMeasures) {
        this.extraMeasures = extraMeasures;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

}

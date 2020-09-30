
package com.dpk.hammoq.GsonModule.PaymentDetailsModule;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("amount")
    @Expose
    private Double amount;
    @SerializedName("receipt_url")
    @Expose
    private String receiptUrl;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("receipt_no")
    @Expose
    private String receiptNo;
    @SerializedName("network_status")
    @Expose
    private String networkStatus;
    @SerializedName("seller_message")
    @Expose
    private String sellerMessage;
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("charge_id")
    @Expose
    private String chargeId;
    @SerializedName("balance_transaction_id")
    @Expose
    private String balanceTransactionId;
    @SerializedName("__v")
    @Expose
    private Integer v;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getReceiptUrl() {
        return receiptUrl;
    }

    public void setReceiptUrl(String receiptUrl) {
        this.receiptUrl = receiptUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }

    public String getNetworkStatus() {
        return networkStatus;
    }

    public void setNetworkStatus(String networkStatus) {
        this.networkStatus = networkStatus;
    }

    public String getSellerMessage() {
        return sellerMessage;
    }

    public void setSellerMessage(String sellerMessage) {
        this.sellerMessage = sellerMessage;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getChargeId() {
        return chargeId;
    }

    public void setChargeId(String chargeId) {
        this.chargeId = chargeId;
    }

    public String getBalanceTransactionId() {
        return balanceTransactionId;
    }

    public void setBalanceTransactionId(String balanceTransactionId) {
        this.balanceTransactionId = balanceTransactionId;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

}

package com.dpk.hammoq.utilsNetwork;

import com.dpk.hammoq.GsonModule.ChangePasswordModule.ChangePasswordPkg;
import com.dpk.hammoq.GsonModule.ForgetPasswordModule.ForgetPasswordPkg;
import com.dpk.hammoq.GsonModule.ForgetPasswordModule.NewPasswordModule.NewPasswordPkg;
import com.dpk.hammoq.GsonModule.ForgetPasswordModule.OtpModule.ForgetPasswordOtpVerifyPkg;
import com.dpk.hammoq.GsonModule.LoginModule.LoginPkg;
import com.dpk.hammoq.GsonModule.PaymentDetailsModule.PaymentDetailsPkg;
import com.dpk.hammoq.GsonModule.ProductListingApiModule.ProductListApi;
import com.dpk.hammoq.GsonModule.RegisterModule.RegisterPkg;
import com.dpk.hammoq.GsonModule.SettingClientLoginEditPassModule.SettingLoginClientEditPassPkg;
import com.dpk.hammoq.GsonModule.SettingLoginClientPassModule.SettingLoginClientPassPkg;

import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;


public interface UrlsApi {
    @GET("client/signin")
    Call<LoginPkg> LOGIN_PKG_CALL(@QueryMap Map<String, String> loginParameters);

    @GET("client/password")
    Call<SettingLoginClientPassPkg> SETTING_LOGIN_CLIENT_PASS_PKG_CALL(@HeaderMap Map<String, String> mTokenAccess);

    @GET("client/forgotpassword/{email}")
    Call<ForgetPasswordPkg> FORGET_PASSWORD_PKG_CALL(@Path("email") String mEmail);

    @GET("client/forgotpassword/confirmcode/{code}/{email}")
    Call<ForgetPasswordOtpVerifyPkg> FORGET_PASSWORD_OTP_VERIFY_PKG_CALL(@Path("code") String mCode,
                                                                         @Path("email") String mEmail);

    @GET("client/payment/getpayments")
    Call<PaymentDetailsPkg> PAYMENT_DETAILS_PKG_CALL(@HeaderMap Map<String, String> mTokenAccess);

    @FormUrlEncoded
    @POST("client/forgotpassword/update")
    Call<NewPasswordPkg> NEW_PASSWORD_PKG_CALL(@HeaderMap Map<String, String> mTokenAccess,
                                               @FieldMap Map<String, String> parameterNewEmailPass);

    @FormUrlEncoded
    @POST("client/resetpassword")
    Call<ChangePasswordPkg> CHANGE_PASSWORD_PKG_CALL(@HeaderMap Map<String, String> mTokenAccess,
                                                     @FieldMap Map<String, String> parameterOldNewPass);

    @FormUrlEncoded
    @POST("client/signup")
    Call<RegisterPkg> REGISTER_PKG_CALL(@FieldMap Map<String, String> parameterRegister);

    @Multipart
    @POST("http://stageapp.avoidpoints.com/api/client/product")
    Call<ProductListApi> PRODUCT_LIST_API_CAL(@Part MultipartBody.Part default_image,
                                              @Part MultipartBody.Part brand_image,
                                              @Part MultipartBody.Part model_image,
                                              @Part MultipartBody.Part side1_image,
                                              @Part MultipartBody.Part side2_image,
                                              @Part MultipartBody.Part front_image,
                                              @Part MultipartBody.Part back_image,
                                              @Part MultipartBody.Part condition1_image,
                                              @Part MultipartBody.Part condition2_image,
                                              @Part MultipartBody.Part condition3_image,
                                              @Part MultipartBody.Part condition4_image,
                                              @Part MultipartBody.Part condition5_image,
                                              @Part MultipartBody.Part condition_name,
                                              @Header("x-access-token") String mTokenAccess);
//    @Part List<MultipartBody.Part> default_image,

    @FormUrlEncoded
    @PUT("client/password")
    Call<SettingLoginClientEditPassPkg> SETTING_LOGIN_CLIENT_EDIT_PASS_PKG_CALL(@HeaderMap Map<String, String> mTokenAccess,
                                                                                @FieldMap Map<String, String> parameterEditPass);


}
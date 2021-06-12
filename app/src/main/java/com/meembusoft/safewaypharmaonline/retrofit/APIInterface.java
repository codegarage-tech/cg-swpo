package com.meembusoft.safewaypharmaonline.retrofit;

import com.meembusoft.safewaypharmaonline.model.Category;
import com.meembusoft.safewaypharmaonline.model.CommonData;
import com.meembusoft.safewaypharmaonline.model.CommonMedicinesByItem;
import com.meembusoft.safewaypharmaonline.model.FilterSessionLeft;
import com.meembusoft.safewaypharmaonline.model.Home;
import com.meembusoft.safewaypharmaonline.model.LoginResponse;
import com.meembusoft.safewaypharmaonline.model.NearestStore;
import com.meembusoft.safewaypharmaonline.model.Notifications;
import com.meembusoft.safewaypharmaonline.model.ParamsAddReminder;
import com.meembusoft.safewaypharmaonline.model.ParamsAddShippingAddress;
import com.meembusoft.safewaypharmaonline.model.ParamsDeleteNotification;
import com.meembusoft.safewaypharmaonline.model.ParamsEditShippingAddress;
import com.meembusoft.safewaypharmaonline.model.ParamsOrderReceivedByUser;
import com.meembusoft.safewaypharmaonline.model.ParamsOrderTrush;
import com.meembusoft.safewaypharmaonline.model.ParamsStoreAcceptOrder;
import com.meembusoft.safewaypharmaonline.model.ParamsDoOrder;
import com.meembusoft.safewaypharmaonline.model.ParamsFavourite;
import com.meembusoft.safewaypharmaonline.model.ParamsSupplierLogin;
import com.meembusoft.safewaypharmaonline.model.ParamsSupplierProfileUpdate;
import com.meembusoft.safewaypharmaonline.model.ParamsUpdateFcm;
import com.meembusoft.safewaypharmaonline.model.ParamsUpdateStatusReminder;
import com.meembusoft.safewaypharmaonline.model.ParamsUserLogin;
import com.meembusoft.safewaypharmaonline.model.ParamsUserProfileUpdate;
import com.meembusoft.safewaypharmaonline.model.ParamsUserRegistration;
import com.meembusoft.safewaypharmaonline.model.ProductDetails;
import com.meembusoft.safewaypharmaonline.model.Promos;
import com.meembusoft.safewaypharmaonline.model.RelatedMedicine;
import com.meembusoft.safewaypharmaonline.model.Remainder;
import com.meembusoft.safewaypharmaonline.model.ResponseOrderItem;
import com.meembusoft.safewaypharmaonline.model.ShippingAddress;
import com.meembusoft.safewaypharmaonline.model.ShippingCharge;
import com.meembusoft.safewaypharmaonline.model.StaggeredMedicineByItem;
import com.meembusoft.safewaypharmaonline.model.Slider;
import com.meembusoft.safewaypharmaonline.model.Store;
import com.meembusoft.safewaypharmaonline.model.StoreOrder;
import com.meembusoft.safewaypharmaonline.model.Suppliers;
import com.meembusoft.safewaypharmaonline.model.SystemSetting;
import com.meembusoft.safewaypharmaonline.model.UserProfile;
import com.meembusoft.safewaypharmaonline.model.Videos;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public interface APIInterface {

    @GET("api/home/{id}?")
    Call<APIResponse<Home>> apiAllGetHome(@Path("id") String customer_id,@Query("user_type") String user_type);

    @GET("api/home")
    Call<APIResponse<Home>> apiAllGetHome();

    @GET("api/sliders")
    Call<APIResponse<List<Slider>>> apiGetSliders();

    @GET("api/categories")
    Call<APIResponse<List<Category>>> apiGetCategories();

    @GET("api/medicine_by_category/{cat_id}/{offset}/{per_page_item}?")
    Call<APIResponse<CommonMedicinesByItem>> apiGetMedicineByCategoryList(@Path("cat_id") String cat_id,@Path("offset") int offset, @Path("per_page_item") int per_page_item,@Query("user_id") String user_id,@Query("user_type") String user_type);

    @GET("api/medicine/{product_id}/{user_id}?")
    Call<APIResponse<ProductDetails>> apiGetProductDetails(@Path("product_id") String product_id,@Path("user_id") String user_id,@Query("user_type") String user_type);

    @GET("api/similarMedicines/{product_id}?")
    Call<APIResponse<List<RelatedMedicine>>> apiGetRelatedMedicineList(@Path("product_id") String product_id,@Query("user_id") String user_id,@Query("user_type") String user_type);

    @GET("api/latest_product?")
    Call<APIResponse<CommonMedicinesByItem>> apiGetLatestProductList(@Query("user_id") String user_id,@Query("user_type") String user_type);

    @Headers({"Content-Type: application/json"})
    @GET("api/medicineSearch/{barcode_num}?")
    Call<APIResponse<List<StaggeredMedicineByItem>>> apiGetMedicineSearchList(@Path("barcode_num") String barcode_num,@Query("user_id") String user_id,@Query("user_type") String user_type);

    @GET("api/medicines/{offset}/{per_page_item}?")
    Call<APIResponse<CommonMedicinesByItem>> apiGetAllProductMedicinesList(@Path("offset") int offset, @Path("per_page_item") int per_page_item,@Query("user_type") String user_type,@Query("user_id") String user_id);

    @GET("api/suppliers/{page}/{per_page_item}")
    Call<APIResponse<List<Suppliers>>> apiGetSuppliersList(@Path("page") String page, @Path("per_page_item") String per_page_item);

    @GET("api/order_list_by_customer_id/{customer_id}/{offset}/{per_page_item}?")
    Call<APIResponse<StoreOrder>> apiGetPlaceOrderByCustomerList(@Path("customer_id") String customer_id, @Path("offset") int offset, @Path("per_page_item") int per_page_item,@Query("user_type") String user_type);

    @GET("api/prescription_list_by_customer_id/{customer_id}/{offset}/{per_page_item}")
    Call<APIResponse<StoreOrder>> apiGetPrescriptionListByCustomer(@Path("customer_id") String customer_id, @Path("offset") int offset, @Path("per_page_item") int per_page_item);

    @GET("api/collections")
    Call<APIResponse<List<CommonData>>> apiGetCollectionsList();

    @GET("api/best_seller/{offset}/{per_page_item}?")
    Call<APIResponse<CommonMedicinesByItem>> apiGetBestSellerList(@Path("offset") int offset, @Path("per_page_item") int per_page_item,@Query("user_id") String user_id,@Query("user_type") String user_type);

    @GET("api/essential_product?")
    Call<APIResponse<List<StaggeredMedicineByItem>>> apiGetEssentialProductList(@Query("user_id") String user_id,@Query("user_type") String user_type);

    @GET("api/nearest_store_list/{order_id}")
    Call<APIResponse<List<NearestStore>>> apiGetNearestStoreList(@Path("order_id") String order_id);

    @GET("api/emergency_helps")
    Call<APIResponse<CommonMedicinesByItem>> apiGetEmergencyHelpList();

    @GET("api/shiping_charge")
    Call<APIResponse<List<ShippingCharge>>> apiGetShippingChargeList();

    @GET("api/system_setting")
    Call<APIResponse<List<SystemSetting>>> apiGetSystemSettingList();

    @GET("api/videos")
    Call<APIResponse<List<Videos>>> apiGetVideosList();

    @GET("api/store_list")
    Call<APIResponse<List<Store>>> apiGetStoreList();

    @GET("api/favourite/list/{customer_id}")
    Call<APIResponse<List<StaggeredMedicineByItem>>> apiGetAllFavouriteList(@Path("customer_id") String customer_id);

    @GET("api/favourite/remove/{customer_id}/{product_id}")
    Call<APIResponse> apiGetAllFavouriteRemoveList(@Path("customer_id") String customer_id, @Path("product_id") String product_id);

    @Headers({"Content-Type: application/json"})
    @GET("api/shiping_address/{customer_id}?")
    Call<APIResponse<List<ShippingAddress>>> apiGetAllShippingAddressList(@Path("customer_id") String customer_id,@Query("user_type") String user_type);

    @GET("api/customer_profile/{customer_id}")
    Call<APIResponse<UserProfile>> apiGetUserProfile(@Path("customer_id") String customer_id);

    @GET("apiv2/getOrderStatusList?")
    Call<APIResponse<List<Notifications>>> apiGetAllOrderNotifyStatusList(@Query("user_id") String user_id,@Query("user_type") String user_type);

    @GET("api/notifications")
    Call<APIResponse<List<Notifications>>> apiGetAllNotificationsList();

    @GET("apiv2/getPromos")
    Call<APIResponse<List<Promos>>> apiGetAllPromosList();

    @GET("apiv2/filterMedicineReminder?")
    Call<APIResponse<List<FilterSessionLeft>>> apiGetAllFilterSessionLeftList(@Query("date") String date,@Query("customer_id") String customer_id);

    @Headers({"Content-Type: application/json"})
    @GET("apiv2/getReminderListsByCustomer/{customer_id}")
    Call<APIResponse<List<Remainder>>> apiGetReminderListsByCustomer(@Path("customer_id") String customer_id);

    // Store order List
    @GET("api/new_order_list/{store_id}?")
    Call<APIResponse<StoreOrder>> apiGetAllStoreOrder(@Path("store_id") String store_id,@Query("user_type") String user_type);

    @Headers({"Content-Type: application/json"})
    @GET("api/accepted_order_list_by_store_id/{store_id}/{from_date}/{to_date}/{offset}/{per_page_item}?")
    Call<APIResponse<StoreOrder>> apiGetAllStoreAcceptedOrderList(@Path("store_id") String customer_id,@Path("from_date") String from_date,@Path("to_date") String to_date, @Path("offset") int offset, @Path("per_page_item") int per_page_item,@Query("user_type") String user_type);

    @Headers({"Content-Type: application/json"})
    @GET("api/completed_order_list_by_store_id/{store_id}/{from_date}/{to_date}/{offset}/{per_page_item}")
    Call<APIResponse<StoreOrder>> apiGetAllStoreCompletedOrderList(@Path("store_id") String customer_id,@Path("from_date") String from_date,@Path("to_date") String to_date, @Path("offset") int offset, @Path("per_page_item") int per_page_item);

    @GET("api/vendor_profile/{supplier_id}")
    Call<APIResponse<UserProfile>> apiGetSupplierProfile(@Path("supplier_id") String supplier_id);

    @Headers({"Content-Type: application/json"})
    @POST("api/favourite/add")
    Call<APIResponse> apiUserFavouriteAdd(@Body ParamsFavourite paramsFavourite);

    @Headers({"Content-Type: application/json"})
    @POST("api/place_order")
    Call<APIResponse<ResponseOrderItem>> apiUserOrderPlace(@Body ParamsDoOrder paramsDoOrder);

    @Headers({"Content-Type: application/json"})
    @POST("api/new_shiping_address")
    Call<APIResponse<ShippingAddress>> apiUserAddNewShippingAddress(@Body ParamsAddShippingAddress mParamsAddShippingAddress);

    @Headers({"Content-Type: application/json"})
    @POST("api/update_shiping_address")
    Call<APIResponse<ShippingAddress>> apiUserEditShippingAddress(@Body ParamsEditShippingAddress mParamsEditShippingAddress);

    @Headers({"Content-Type: application/json"})
    @POST("api/customer_profile_update/{customer_id}")
    Call<APIResponse<UserProfile>> apiUserProfileUpdate(@Body ParamsUserProfileUpdate mParamsUserProfileUpdate);

    @Headers({"Content-Type: application/json"})
    @POST("api/login")
    Call<APIResponse> apiUserLogin(@Body ParamsUserLogin mParamUserLogin);

    @Headers({"Content-Type: application/json"})
    @POST("api/login")
    Call<LoginResponse> apiUserLogin2(@Body ParamsUserLogin mParamUserLogin);

    @Headers({"Content-Type: application/json"})
    @POST("api/registration")
    Call<APIResponse> apiUserRegistration(@Body ParamsUserRegistration paramsUserRegistration);

    @Headers({"Content-Type: application/json"})
    @POST("api/store_login")
    Call<APIResponse> apiSupplierUserLogin(@Body ParamsSupplierLogin mParamsSupplierLogin);

    @Headers({"Content-Type: application/json"})
    @POST("api/vendor_profile_update/{supplier_id}")
    Call<APIResponse<UserProfile>> apiSupplierProfileUpdate(@Body ParamsSupplierProfileUpdate mParamsSupplierProfileUpdate);

    @Headers({"Content-Type: application/json"})
    @POST("api/accept_order")
    Call<APIResponse> apiStoreAcceptedOrder(@Body ParamsStoreAcceptOrder mParamsStoreAcceptOrder);

    @Headers({"Content-Type: application/json"})
    @POST("api/skip_order")
    Call<APIResponse> apiStoreSkipdOrder(@Body ParamsStoreAcceptOrder mParamsStoreAcceptOrder);

    @Headers({"Content-Type: application/json"})
    @POST("api/delivery_on_the_way_order")
    Call<APIResponse> apiDeliveryOnTheWayOrder(@Body ParamsStoreAcceptOrder mParamsDeliveryOrder);

    @Headers({"Content-Type: application/json"})
    @POST("api/order_receipt_by_user")
    Call<APIResponse> apiOrderReceivedByUser(@Body ParamsOrderReceivedByUser mParamsOrderReceivedByUser);

    @Headers({"Content-Type: application/json"})
    @POST("api/payment_receipt_by_supplier")
    Call<APIResponse> apiPaymentReceivedBySupplier(@Body ParamsStoreAcceptOrder mParamsDeliveryOrder);

    @Headers({"Content-Type: application/json"})
    @POST("api/update_fcm_token")
    Call<APIResponse> apiUpdateFcmToken(@Body ParamsUpdateFcm mParamsUpdateFcm);

    @Headers({"Content-Type: application/json"})
    @POST("api/customer_order_trush")
    Call<APIResponse> apiCustomerOrderTrush(@Body ParamsOrderTrush mParamsOrderTrush);

    @Headers({"Content-Type: application/json"})
    @POST("apiv2/deleteNotification")
    Call<APIResponse> apiDeleteNotification(@Body ParamsDeleteNotification mParamsDeleteNotification);

    @Headers({"Content-Type: application/json"})
    @POST("apiv2/add_medicine_reminder")
    Call<APIResponse> apiAddMedicineReminder(@Body ParamsAddReminder mParamsAddReminder);

    @Headers({"Content-Type: application/json"})
    @POST("apiv2/deleteReminder")
    Call<APIResponse> apiDeleteReminder(@Body ParamsDeleteNotification mParamsDeleteReminder);

    @Headers({"Content-Type: application/json"})
    @POST("apiv2/updateReminderStatus")
    Call<APIResponse> apiUpdateStatusReminder(@Body ParamsUpdateStatusReminder mParamsUpdateStatusReminder);

}
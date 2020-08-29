package com.mahavir_infotech.vidyasthali.Utility;


import com.google.gson.JsonObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface LoadInterface {


    @Headers({
            "version: 1.0",
            "deviceType: Android"})
    @POST("userLogin")
    @FormUrlEncoded
    Call<ResponseBody> userLogin(@Field(value = "email") String email, @Field(value = "password") String password, @Field(value = "user_type") String user_type, @Field(value = "deviceToken") String deviceToken, @Field(value = "deviceType") String deviceType);

    @Headers({
            "version: 1.0",
            "deviceToken: adhflhdfhdjfhah",
            "deviceType: Android"})
    @POST("studentLogin")
    @FormUrlEncoded
    Call<ResponseBody> student_userLogin(@Field(value = "roll_no") String roll_no, @Field(value = "password") String password, @Field(value = "iMEI") String iMEI, @Field(value = "deviceToken") String deviceToken, @Field(value = "deviceType") String deviceType);
    @Headers({
            "version: 1.0",
            "deviceType: Android"})
    @POST("parentsLogin")
    @FormUrlEncoded
    Call<ResponseBody> parentsLogin(@Field(value = "contact") String contact, @Field(value = "iMEI") String iMEI, @Field(value = "deviceType") String deviceType, @Field(value = "deviceToken") String deviceToken);


    @Headers({
            "version: 1.0",
            "deviceType: Android"})
    @POST("verifyOtp")
    @FormUrlEncoded
    Call<ResponseBody> verifyOtp(@Field(value = "contact") String contact, @Field(value = "otp") String otp, @Field(value = "iMEI") String iMEI, @Field(value = "deviceType") String deviceType, @Field(value = "deviceToken") String deviceToken);


    @Headers({
            "version: 1.0",
            "deviceType: android"})
    @POST("studentLogin")
    @FormUrlEncoded
    Call<ResponseBody> studentLogin(@Field(value = "schollar_no") String schollar_no, @Field(value = "password") String password, @Field(value = "user_type") String user_type, @Field(value = "deviceToken") String deviceToken, @Field(value = "iMEI") String iMEI, @Field(value = "deviceType") String deviceType);


    @POST("contactus")
    @FormUrlEncoded
    Call<ResponseBody> contactus(@Field(value = "name") String name, @Field(value = "email") String email, @Field(value = "contact") String contact, @Field(value = "message") String message);


    @Headers({
            "version: 1.0",
            "deviceType: Android"})
    @POST("forgetPassword")
    @FormUrlEncoded
    Call<ResponseBody> forgetPassword(@Field(value = "email") String email, @Field(value = "user_type") String user_type);



    @Multipart
    @POST("updateTeacherProfile")
    Call<ResponseBody> updateProfile(@Part("teacher_id") RequestBody teacher_id, @Part("name") RequestBody fname, @Part("email") RequestBody phone, @Part MultipartBody.Part part);


    @POST("updateTeacherProfile")
    @FormUrlEncoded
    Call<ResponseBody> updateProfile_withoutimage(@Field(value = "teacher_id") String teacher_id, @Field(value = "name") String fname, @Field(value = "email") String phone);


    @Headers({
            "version: 1.0",
            "deviceToken: adhflhdfhdjfhah",
            "deviceType: Android"})
    @Multipart
    @POST("updateStudentProfile")
    Call<ResponseBody> updateStudentProfile(@Part("user_id") RequestBody user_id, @Part("name") RequestBody fname, @Part("email") RequestBody phone, @Part("father_name") RequestBody father_name, @Part("mother_name") RequestBody mother_name, @Part("dob") RequestBody dob, @Part MultipartBody.Part part);

    @Headers({
            "version: 1.0",
            "deviceToken: adhflhdfhdjfhah",
            "deviceType: Android"})
    @POST("updateStudentProfile")
    @FormUrlEncoded
    Call<ResponseBody> updateStudentProfile_withoutimage(@Field(value = "user_id") String user_id, @Field(value = "name") String fname, @Field(value = "email") String phone, @Field(value = "father_name") String father_name, @Field(value = "mother_name") String mother_name, @Field(value = "dob") String dob);


    @Headers({
            "version: 1.0",
            "deviceToken: adhflhdfhdjfhah",
            "deviceType: Android"})
    @POST("teacherChangePassword")
    @FormUrlEncoded
    Call<ResponseBody> teacherChangePassword(@Field(value = "teacher_id") String teacher_id, @Field(value = "new_password") String new_password, @Field(value = "current_password") String current_password);


    @Headers({
            "version: 1.0",
            "deviceToken: adhflhdfhdjfhah",
            "deviceType: Android"})
    @POST("studentChangePassword")
    @FormUrlEncoded
    Call<ResponseBody> studentChangePassword(@Field(value = "user_id") String user_id, @Field(value = "new_password") String new_password, @Field(value = "current_password") String current_password);

    @Headers({
            "version: 1.0",
            "deviceToken: adhflhdfhdjfhah",
            "deviceType: Android"})
    @POST("listStudentAttendance")
    @FormUrlEncoded
    Call<ResponseBody> listStudentAttendance(@Field(value = "teacher_id") String teacher_id);







    @POST("register_user")
    @FormUrlEncoded
    Call<ResponseBody> SignUpCall(@Field(value = "fname") String fnam, @Field(value = "lname") String lname, @Field(value = "email") String email, @Field(value = "phone") String phone, @Field(value = "device_id") String device_id);

    @POST("products_detail")
    @FormUrlEncoded
    Call<ResponseBody> products_detail(@Field(value = "product_id") String product_id);

    @POST("add_to_cart")
    @FormUrlEncoded
    Call<ResponseBody> add_to_cart(@Field(value = "user_id") String user_id, @Field(value = "product_id") String product_id, @Field(value = "unit_id") String unit_id, @Field(value = "quantity") String quantity, @Field(value = "sub_category_id") String sub_category_id, @Field(value = "category_id") String category_id, @Field(value = "price_id") String price_id);

    @POST("add_fav_list")
    @FormUrlEncoded
    Call<ResponseBody> add_fav_list(@Field(value = "user_id") String user_id, @Field(value = "product_id") String product_id);

    @POST("delete_fav")
    @FormUrlEncoded
    Call<ResponseBody> delete_fav(@Field(value = "fav_id") String fav_id);

    @POST("get_add_to_cart")
    @FormUrlEncoded
    Call<ResponseBody> get_add_to_cart(@Field(value = "user_id") String user_id);

    @POST("deleteHomework")
    @FormUrlEncoded
    Call<ResponseBody> deleteHomework(@Field(value = "homework_id") String homework_id);

    @POST("deleteSession")
    @FormUrlEncoded
    Call<ResponseBody> deleteSession(@Field(value = "session_id") String session_id);

    @POST("deleteStudyMaterial")
    @FormUrlEncoded
    Call<ResponseBody> deleteStudyMaterial(@Field(value = "material_id") String material_id);

    @POST("deleteTimetable")
    @FormUrlEncoded
    Call<ResponseBody> deleteTimetable(@Field(value = "timetable_id") String timetable_id);


    @POST("responseLeaveRequest")
    @FormUrlEncoded
    Call<ResponseBody> responseLeaveRequest(@Field(value = "teacher_id") String teacher_id, @Field(value = "leave_id") String leave_id, @Field(value = "status") String status, @Field(value = "cancel_reason") String cancel_reason);


    @POST("Get_address")
    @FormUrlEncoded
    Call<ResponseBody> Get_address(@Field(value = "user_id") String user_id);

    @POST("list_noti")
    @FormUrlEncoded
    Call<ResponseBody> list_noti(@Field(value = "user_id") String user_id);

    @POST("add_address")
    @FormUrlEncoded
    Call<ResponseBody> add_address(@Field(value = "user_id") String user_id, @Field(value = "address") String address, @Field(value = "latitude") String latitude, @Field(value = "longitude") String longitude);



    @POST("confirm_order")
    @FormUrlEncoded
    Call<ResponseBody> confirm_order(@Field(value = "user_id") String user_id, @Field(value = "total_amount") String total_amount, @Field(value = "paid_amount") String paid_amount, @Field(value = "transaction_id") String transaction_id, @Field(value = "payment_method") String payment_method, @Field(value = "address") String address, @Field(value = "latitude") String latitude, @Field(value = "longitude") String longitude, @Field(value = "coupon_id") String coupon_id);


    @POST("product_by_cat_id")
    @FormUrlEncoded
    Call<ResponseBody> product_by_cat_id(@Field(value = "category_id") String category_id);

    @POST("product_cat_sub_cat")
    @FormUrlEncoded
    Call<ResponseBody> product_cat_sub_cat(@Field(value = "category_id") String category_id, @Field(value = "subcategory_id") String subcategory_id);

    @POST("addAttaindance")
    @FormUrlEncoded
    Call<ResponseBody> addAttaindance(@Field(value = "teacher_id") String teacher_id, @Field(value = "student_ids") String student_ids, @Field(value = "present_status") String present_status, @Field(value = "class_id") String class_id);

    @POST("Get_confirm_order")
    @FormUrlEncoded
    Call<ResponseBody> Get_confirm_order(@Field(value = "user_id") String user_id);


    @POST("RecentChatList")
    @FormUrlEncoded
    Call<ResponseBody> RecentChatList(@Field(value = "user_id") String user_id, @Field(value = "sender_role") String sender_role);

    @POST("chatUsers")
    Call<ResponseBody> chatUsers(@Body JsonObject jsonObject);

    @POST("clearChat")
    Call<ResponseBody> clearChat(@Body JsonObject jsonObject);

    @POST("ChatList")
    Call<ResponseBody> ChatList(@Body JsonObject jsonObject);

    @POST("deleteMessagesByIds")
    Call<ResponseBody> deleteMessagesByIds(@Body JsonObject jsonObject);

    @POST("ReportUser")
    Call<ResponseBody> ReportUser(@Body JsonObject jsonObject);

    @POST("blockUser")
    Call<ResponseBody> blockUser(@Body JsonObject jsonObject);

    @POST("fav_list")
    @FormUrlEncoded
    Call<ResponseBody> fav_list(@Field(value = "user_id") String user_id);

    @POST("review_rat_list")
    @FormUrlEncoded
    Call<ResponseBody> review_rat_list(@Field(value = "user_id") String user_id);

    @POST("search_product")
    @FormUrlEncoded
    Call<ResponseBody> search_product(@Field(value = "search_pro") String search_pro);

    @POST("reviewProduct")
    @FormUrlEncoded
    Call<ResponseBody> reviewProduct(@Field(value = "user_id") String user_id, @Field(value = "product_id") String product_id, @Field(value = "review") String review, @Field(value = "rating") String rating);

    @POST("getSubject")
    @FormUrlEncoded
    Call<ResponseBody> getSubject(@Field(value = "teacher_id") String teacher_id, @Field(value = "class_id") String class_id);

    @POST("getSubjectByClassId")
    @FormUrlEncoded
    Call<ResponseBody> getSubjectByClassId(@Field(value = "class_id") String class_id);

    @POST("cnacle_order")
    @FormUrlEncoded
    Call<ResponseBody> cnacle_order(@Field(value = "order_id") String order_id, @Field(value = "status") String status);

    @POST("my_profile_update")
    @FormUrlEncoded
    Call<ResponseBody> UserProfileUpdate_withoutImage(@Field(value = "user_id") String user_id, @Field(value = "fname") String fname, @Field(value = "lname") String lname, @Field(value = "email") String email, @Field(value = "phone") String phone);


    @GET("faq")
    Call<ResponseBody> faq();

    @GET("getClasses")
    Call<ResponseBody> getClasses(@Query("teacher_id") String teacher_id);

    @GET("getAllClasses")
    Call<ResponseBody> getAllClasses(@Query("teacher_id") String teacher_id);


    @GET("ChatSearchTeacher")
    Call<ResponseBody> ChatSearchTeacher(@Query("sender_id") String sender_id);

    @GET("ChatSearchPrincipal")
    Call<ResponseBody> ChatSearchPrincipal(@Query("sender_id") String sender_id);

    @GET("ChatSearchStudents")
    Call<ResponseBody> ChatSearchStudents(@Query("sender_id") String sender_id);

    @GET("listStudyMaterialTypes")
    Call<ResponseBody> getlistStudyMaterialTypes();

    @GET("listStudentAttendance")
    Call<ResponseBody> listStudentAttendance_get(@Query("teacher_id") String teacher_id);

    @GET("viewStudentAttendance")
    Call<ResponseBody> viewStudentAttendance(@Query("teacher_id") String teacher_id,@Query("date") String date);


    @GET("viewStudentAttendanceByMonth")
    Call<ResponseBody> viewStudentAttendanceByMonth(@Query("student_id") String student_id, @Query("date") String date);

    @GET("contact_us")
    Call<ResponseBody> contact_us();

    @GET("listCoupon")
    Call<ResponseBody> listCoupon();

    @GET("social_media")
    Call<ResponseBody> social_media();


    //	http://pixelmarketo.com/vegetabels/index.php/Api/Api/resendOtp?token=90a0ae78cf3dc2b6b712b1778e9c8abb

    @GET("factoryOutlet/index.php/Api/Api/resendOtp")
    Call<ResponseBody> ResendOtp(@Query("token") String token);


    //http://pixelmarketo.com/vegetabels/index.php/Api/Api/ResetPwd?token=90a0ae78cf3dc2b6b712b1778e9c8abb&password=1234

    @GET("factoryOutlet/index.php/Api/Api/ResetPwd")
    Call<ResponseBody> ResetPwd(@Query("token") String token, @Query("password") String password);

    @GET("factoryOutlet/index.php/Api/Api/ChangePassword")
    Call<ResponseBody> ChangePassword(@Query("token") String token, @Query("current_password") String current_password, @Query("new_password") String new_password);

    //http://pixelmarketo.com/vegetabels/index.php/Api/Api/ListCategory
    @GET("factoryOutlet/index.php/Api/Api/addAddress")
    Call<ResponseBody> addAddress(@Query("token") String token, @Query("city") String city, @Query("address") String address, @Query("house_flate") String house_flate, @Query("landmark") String landmark, @Query("pincode") String pincode, @Query("state") String state);


    @GET("factoryOutlet/index.php/Api/Api/UpdateAddress")
    Call<ResponseBody> UpdateAddress(@Query("address_id") String token, @Query("city") String city, @Query("address") String address, @Query("house_flate") String house_flate, @Query("landmark") String landmark, @Query("pincode") String pincode, @Query("state") String state);

    //	http://pixelmarketo.com/vegetabels/index.php/Api/Api/listProducts?categorie_id=1
    @GET("vegetabels/index.php/Api/Api/listProducts")
    Call<ResponseBody> listProducts(@Query("categorie_id") String categorie_id, @Query("user_id") String user_id);

    @GET("vegetabels/index.php/Api/Api/listProducts")
    Call<ResponseBody> listProductsAll(@Query("user_id") String user_id);

    @GET("factoryOutlet/index.php/Api/Api/orderDetails")
    Call<ResponseBody> orderDetails(@Query("order_id") String user_id);

    @GET("vegetabels/index.php/Api/Api/home")
    Call<ResponseBody> home(@Query("user_id") String user_id);


//http://pixelmarketo.com/vegetabels/index.php/Api/Api/GetCountCartItem?user_id=2

    @GET("refund")
    Call<ResponseBody> refund();

    @GET("about")
    Call<ResponseBody> about();

    @GET("privacy_policy")
    Call<ResponseBody> privacy_policy();

    @GET("term_condition")
    Call<ResponseBody> term_condition();

    @GET("factoryOutlet/index.php/Api/Api/AddReviewRating")
    Call<ResponseBody> AddReviewRating(@Query("token") String token, @Query("order_id") String order_id, @Query("rate") String rate, @Query("review") String review);

    @GET("factoryOutlet/index.php/Api/Api/ListCategory")
    Call<ResponseBody> ListCategory(@Query("token") String token);

    @GET("factoryOutlet/index.php/Api/Api/getCartBytoken")
    Call<ResponseBody> getCartBytoken(@Query("token") String token);

    @GET("factoryOutlet/index.php/Api/Api/ListProductByShopToken")
    Call<ResponseBody> ListProductByShopToken(@Query("token") String token);

    @GET("factoryOutlet/index.php/Api/Api/ListAllCategory")
    Call<ResponseBody> ListAllCategory();

    @GET("factoryOutlet/index.php/Api/Api/ListFilterProduct")
    Call<ResponseBody> ListFilterProduct();


    @GET("front_page")
    Call<ResponseBody> Homepage();

    @GET("get_cat")
    Call<ResponseBody> get_cat();


    @GET("factoryOutlet/index.php/Api/Api/listGroup")
    Call<ResponseBody> listGroup(@Query("token") String token);

    @GET("factoryOutlet/index.php/Api/Api/ListProductBySubCategoryId")
    Call<ResponseBody> ListProductBySubCategoryId(@Query("sub_categorie_id") String token);

    //http://pixelmarketo.com/factoryOutlet/index.php/Api/Api/ListSubCategory?category_id=2
    @GET("factoryOutlet/index.php/Api/Api/ListSubCategory")
    Call<ResponseBody> ListSubCategory(@Query("category_id") String category_id);

    @GET("factoryOutlet/index.php/Api/Api/AddCategory")
    Call<ResponseBody> AddCategory(@Query("token") String token, @Query("category_name") String category_name);

    @GET("factoryOutlet/index.php/Api/Api/RemoveToCart")
    Call<ResponseBody> RemoveToCart(@Query("token") String token, @Query("product_id") String product_id);

    @GET("factoryOutlet/index.php/Api/Api/AddGroup")
    Call<ResponseBody> AddGroup(@Query("token") String token, @Query("group_name") String group_name);

    @GET("factoryOutlet/index.php/Api/Api/AddSubCategory")
    Call<ResponseBody> AddSubCategory(@Query("token") String token, @Query("category_id") String category_id, @Query("sub_category_name") String sub_category_name);

    @GET("factoryOutlet/index.php/Api/Api/DeleteAddress")
    Call<ResponseBody> DeleteAddress(@Query("address_id") String token);


    //http://pixelmarketo.com/vegetabels/index.php/Api/Api/userFeedback?
    // user_id=2&subject=gud%20services&message=keep%20it%20on%20going
    @GET("vegetabels/index.php/Api/Api/userFeedback")
    Call<ResponseBody> userFeedback(@Query("user_id") String user_id, @Query("subject") String subject, @Query("message") String message);

    //http://pixelmarketo.com/vegetabels/index.php/Api/Api/add_to_cart?user_id=2&product_id=4&quantity=2
    @GET("vegetabels/index.php/Api/Api/add_to_cart")
    Call<ResponseBody> addCart(@Query("user_id") String user_id, @Query("product_id") String product_id, @Query("quantity") String quantity, @Query("product_type") String product_type, @Query("discount_percentage") String discount_percentage);


    //http://pixelmarketo.com/vegetabels/index.php/Api/Api/getProductById?product_id=2&user_id=2
    @GET("vegetabels/index.php/Api/Api/getProductById")
    Call<ResponseBody> getProductById(@Query("product_id") String product_id, @Query("user_id") String user_id);


    //http://pixelmarketo.com/vegetabels/index.php/Api/Api/GetCountCartItem?user_id=2

    @GET("factoryOutlet/index.php/Api/Api/OrderHistory")
    Call<ResponseBody> OrderHistory(@Query("token") String token);

    @GET("factoryOutlet/index.php/Api/Api/GetReviewByUserToken")
    Call<ResponseBody> GetReviewByUserToken(@Query("token") String token);

    @GET("vegetabels/index.php/Api/Api/GetCountCartItem")
    Call<ResponseBody> GetCountCartItem(@Query("user_id") String user_id);

    @GET("vegetabels/index.php/Api/Api/listCoupon")
    Call<ResponseBody> promoCodeList(@Query("user_id") String user_id);


    //	http://pixelmarketo.com/vegetabels/index.php/Api/Api/getCartByUserId?user_id=15

    //http://pixelmarketo.com/vegetabels/index.php/Api/Api/listCoupon
    @GET("vegetabels/index.php/Api/Api/getCartByUserId")
    Call<ResponseBody> getCartByUserId(@Query("user_id") String user_id);

    //	http://pixelmarketo.com/vegetabels/index.php/Api/Api/RemoveToCart?user_id=2&product_id=4
//	http://pixelmarketo.com/vegetabels/index.php/Api/Api/GetCouponById?coupon_id=2
    @GET("vegetabels/index.php/Api/Api/GetCouponById")
    Call<ResponseBody> GetCouponById(@Query("coupon_id") String coupon_id);

    //http://pixelmarketo.com/vegetabels/index.php/Api/Api/checkCoupen?user_id=2&coupon_code=new5
    @GET("vegetabels/index.php/Api/Api/checkCoupen")
    Call<ResponseBody> checkCoupen(@Query("user_id") String user_id, @Query("coupon_code") String coupon_code);


    @GET("factoryOutlet/index.php/Api/Api/listAddress")
    Call<ResponseBody> listAddress(@Query("token") String user_id);


    @GET("factoryOutlet/index.php/Api/Api/CheckOut")
    Call<ResponseBody> afterCheckout(@Query("token") String token);

    @GET("listHomework")
    Call<ResponseBody> listHomework(@Query("teacher_id") String teacher_id);

    @GET("listHomeWorkByClassid")
    Call<ResponseBody> listHomeWorkByClassid(@Query("class_id") String class_id);

    @GET("listGallery")
    Call<ResponseBody> listGallery();

    @GET("listEvents")
    Call<ResponseBody> listEvents();

    @GET("listStudyMaterial")
    Call<ResponseBody> listStudyMaterial(@Query("teacher_id") String teacher_id, @Query("material_type_id") String material_type_id);

    @GET("listNoticeBoard")
    Call<ResponseBody> listNoticeBoard();


    @GET("listSessionByTeacherId")
    Call<ResponseBody> listSessionByTeacherId(@Query("teacher_id") String teacher_id, @Query("short_by") String short_by);

    @GET("listSessionByClassid")
    Call<ResponseBody> listSessionByClassid(@Query("class_id") String class_id, @Query("short_by") String short_by);

    @GET("listStudyMaterialByClassId")
    Call<ResponseBody> listStudyMaterialByClassId(@Query("class_id") String teacher_id);

    @GET("listStudyMaterialBySubjectId")
    Call<ResponseBody> listStudyMaterialBySubjectId(@Query("subject_id") String subject_id, @Query("material_type_id") String material_type_id);

    @GET("leaveRequests")
    Call<ResponseBody> leaveRequests(@Query("teacher_id") String teacher_id);


    @POST("studentListLeave")
    @FormUrlEncoded
    Call<ResponseBody> studentListLeave(@Field(value = "student_id") String student_id);



    @POST("addStudentPerformance")
    @FormUrlEncoded
    Call<ResponseBody> addStudentPerformance(@Field(value = "teacher_id") String teacher_id, @Field(value = "student_id") String student_id, @Field(value = "grade_ids") String grade_ids, @Field(value = "remark") String remark, @Field(value = "description") String description);



    @POST("dashboard_counts")
    @FormUrlEncoded
    Call<ResponseBody> dashboard_counts(@Field(value = "user_id") String user_id, @Field(value = "role") String role);

    @POST("listNotification")
    @FormUrlEncoded
    Call<ResponseBody> listNotification(@Field(value = "user_id") String user_id, @Field(value = "role") String role);


    @GET("listTimeTable")
    Call<ResponseBody> listTimeTable(@Query("teacher_id") String teacher_id);

    @GET("listResult")
    Call<ResponseBody> listResult(@Query("user_id") String user_id);

    @GET("listStudentPerformance")
    Call<ResponseBody> listStudentPerformance(@Query("teacher_id") String teacher_id);

    @GET("getStudentPerformance")
    Call<ResponseBody> getStudentPerformance(@Query("student_id") String teacher_id);

    @GET("getTeacherProfile")
    Call<ResponseBody> getTeacherProfile(@Query("teacher_id") String user_id);

    @GET("getStudentProfile")
    Call<ResponseBody> getStudentProfile(@Query("user_id") String user_id);


    @GET("listTimeTableByClassId")
    Call<ResponseBody> student_listTimeTable(@Query("class_id") String class_id);

    @GET("getSupport")
    Call<ResponseBody> getSupport();

    @GET("getStaticPages")
    Call<ResponseBody> getStaticPages(@Query("slug_type") String slug_type);


    @GET("vegetabels/index.php/Api/Api/addAddress")
    Call<ResponseBody> addAddress1(@Query("user_id") String user_id, @Query("city") String city, @Query("address") String address, @Query("house_flate") String house_flate, @Query("landmark") String landmark, @Query("pincode") String pincode);


    //http://pixelmarketo.com/vegetabels/index.php/Api/Api/UpdateProfile?
    // token=90a0ae78cf3dc2b6b712b1778e9c8abb&fullname=testuser&email=example@com&profile_image=INPUT_FILE
    @Multipart
    @POST("vegetabels/index.php/Api/Api/UpdateProfile")
    Call<ResponseBody> UpdateProfile(@Query("token") String token, @Query("fullname") String fullname, @Query("email") String email, @Part MultipartBody.Part file);

    @Multipart
    @POST("factoryOutlet/index.php/Api/Api/UpdateShopDetails")
    Call<ResponseBody> UpdateShopDetails(@Query("token") String token, @Query("shopname") String shopname, @Query("description") String description, @Query("address") String address, @Query("mobile") String mobile, @Query("shopLiceseNum") String shopLiceseNum, @Query("latitude") String latitude, @Query("longitude") String longitude, @Part MultipartBody.Part file);



    @Multipart
    @POST("addHomework")
    Call<ResponseBody> addHomework(@Part("teacher_id") RequestBody teacher_id, @Part("class_id") RequestBody class_id, @Part("homework_date") RequestBody homework_date, @Part("subject_id") RequestBody subject_id, @Part("submission_date") RequestBody submission_date, @Part("description") RequestBody description, @Part MultipartBody.Part file);

    @POST("addHomework")
    @FormUrlEncoded
    Call<ResponseBody> addHomework_without_file(@Field(value = "teacher_id") String teacher_id, @Field(value = "class_id") String class_id, @Field(value = "homework_date") String homework_date, @Field(value = "subject_id") String subject_id, @Field(value = "submission_date") String submission_date, @Field(value = "description") String description);

    @POST("studentAddLeave")
    @FormUrlEncoded
    Call<ResponseBody> studentAddLeave(@Field(value = "student_id") String student_id, @Field(value = "from_date") String from_date, @Field(value = "to_date") String to_date, @Field(value = "reason") String reason);



   @POST("studentUpdateLeave")
    @FormUrlEncoded
    Call<ResponseBody> studentUpdateLeave(@Field(value = "leave_id") String leave_id, @Field(value = "from_date") String from_date, @Field(value = "to_date") String to_date, @Field(value = "reason") String reason);


    @Multipart
    @POST("updateHomework")
    Call<ResponseBody> updateHomework(@Part("homework_id") RequestBody homework_id, @Part("submission_date") RequestBody submission_date, @Part("description") RequestBody description, @Part MultipartBody.Part file);

    @POST("updateHomework")
    @FormUrlEncoded
    Call<ResponseBody> updateHomework_without_file(@Field(value = "homework_id") String homework_id, @Field(value = "submission_date") String submission_date, @Field(value = "description") String description);



    @Multipart
    @POST("addStudyMaterial")
    Call<ResponseBody> addStudyMaterial(@Part("teacher_id") RequestBody teacher_id, @Part("class_id") RequestBody class_id, @Part("title") RequestBody title, @Part("subject_id") RequestBody subject_id, @Part("attach_link") RequestBody attach_link, @Part("link_type") RequestBody link_type, @Part("material_type_id") RequestBody material_type_id, @Part("description") RequestBody description, @Part MultipartBody.Part file, @Part MultipartBody.Part file1);

    @POST("addStudyMaterial")
    @FormUrlEncoded
    Call<ResponseBody> addStudyMaterial_without_file(@Field(value = "teacher_id") String teacher_id, @Field(value = "class_id") String class_id, @Field(value = "title") String title, @Field(value = "subject_id") String subject_id, @Field(value = "attach_link") String attach_link, @Field(value = "link_type") String link_type, @Field(value = "material_type_id") String material_type_id, @Field(value = "description") String description);



    @Multipart
    @POST("updateStudyMaterial")
    Call<ResponseBody> updateStudyMaterial(@Part("material_id") RequestBody material_id, @Part("title") RequestBody title, @Part("attach_link") RequestBody attach_link, @Part("link_type") RequestBody link_type, @Part("description") RequestBody description, @Part MultipartBody.Part file, @Part MultipartBody.Part file1);

    @POST("updateStudyMaterial")
    @FormUrlEncoded
    Call<ResponseBody> updateStudyMaterial_without_file(@Field(value = "material_id") String material_id, @Field(value = "title") String title, @Field(value = "attach_link") String attach_link, @Field(value = "link_type") String link_type, @Field(value = "description") String description);



    @Multipart
    @POST("addTimeTable")
    Call<ResponseBody> addTimeTable(@Part("teacher_id") RequestBody teacher_id, @Part("class_id") RequestBody class_id, @Part("title") RequestBody title, @Part("subject_id") RequestBody subject_id, @Part MultipartBody.Part file);

    @POST("addTimeTable")
    @FormUrlEncoded
    Call<ResponseBody> addTimeTable_without_file(@Field(value = "teacher_id") String teacher_id, @Field(value = "class_id") String class_id, @Field(value = "title") String title, @Field(value = "subject_id") String subject_id);

    @POST("addLiveSession")
    @FormUrlEncoded
    Call<ResponseBody> addLiveSession(@Field(value = "teacher_id") String teacher_id, @Field(value = "class_id") String class_id, @Field(value = "title") String title, @Field(value = "subject_id") String subject_id, @Field(value = "live_url") String live_url, @Field(value = "session_date") String session_date, @Field(value = "session_time") String session_time, @Field(value = "description") String description, @Field(value = "session_end_time") String session_end_time);



    @POST("updateLiveSession")
    @FormUrlEncoded
    Call<ResponseBody> updateLiveSession(@Field(value = "session_id") String session_id, @Field(value = "title") String title, @Field(value = "live_url") String live_url, @Field(value = "session_date") String session_date, @Field(value = "session_time") String session_time, @Field(value = "description") String description, @Field(value = "session_end_time") String session_end_time);


    @Multipart
    @POST("updateTimeTable")
    Call<ResponseBody> updateTimeTable(@Part("teacher_id") RequestBody teacher_id, @Part("timetable_id") RequestBody timetable_id, @Part("title") RequestBody title, @Part MultipartBody.Part file);

    @POST("updateTimeTable")
    @FormUrlEncoded
    Call<ResponseBody> updateTimeTable_without_file(@Field(value = "teacher_id") String teacher_id, @Field(value = "timetable_id") String timetable_id, @Field(value = "title") String title);




    @Multipart
    @POST("factoryOutlet/index.php/Api/Api/AddNewProduct")
    Call<ResponseBody> AddNewProduct(@Query("token") String token, @Query("product_name") String product_name, @Query("description") String description, @Query("product_price") String product_price, @Query("discount_per") String discount_per, @Query("product_category") String product_category, @Query("product_group") String product_group, @Query("sub_categorie_id") String sub_categorie_id, @Part MultipartBody.Part file1, @Part MultipartBody.Part file2, @Part MultipartBody.Part file3, @Part MultipartBody.Part file4, @Part MultipartBody.Part file5);


    //http://pixelmarketo.com/vegetabels/index.php/Api/Api/getProfile?token=90a0ae78cf3dc2b6b712b1778e9c8abb

    @GET("vegetabels/index.php/Api/Api/getProfile")
    Call<ResponseBody> getProfile(@Query("token") String token);

    //http://pixelmarketo.com/vegetabels/index.php/Api/Api/confirmOrder?user_id=2&payment_method=cod

    @GET("factoryOutlet/index.php/Api/Api/confirmOrder")
    Call<ResponseBody> confirmOrder(@Query("token") String token, @Query("payment_method") String payment_method, @Query("payable_amount") String payable_amount, @Query("transaction_id") String transaction_id, @Query("address") String address, @Query("delivery_time") String delivery_time, @Query("total_amount") String total_amount);

    //http://pixelmarketo.com/vegetabels/index.php/Api/Api/confirmOrder?user_id=2&payment_method=cod&payable_amount=200




/*    @Headers({"x-api-key: TEST@123"})
    @Multipart
    @POST("Api/UserApi/UserSignup")
    Call<ResponseBody> user_signup(@Part("fullName") RequestBody fullName, @Part("email") RequestBody email,
                                   @Part("contact") RequestBody contact,
                                   @Part("address") RequestBody address, @Part("password") RequestBody password,
                                   @Part("deviceId") RequestBody deviceId, @Part("deviceType") RequestBody deviceType,
                                   @Part MultipartBody.Part part);



    @Headers({"x-api-key: TEST@123"})
    @POST("Api/UserApi/UserSignup")
    Call<JsonObject> user_signup_no_image(@Body JsonObject jsonObject);*/


}

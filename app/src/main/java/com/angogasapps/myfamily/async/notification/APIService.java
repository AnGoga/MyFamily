package com.angogasapps.myfamily.async.notification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "content-type:application/json",
                    "Authorization:key=AAAA9csGIhU:APA91bF0U8mRa0KiVjbs3WEURjHb2A8SAUTO3XqegTfTA3egEh1boKbWiuGriAfTnPMQ_qu5EfoulSuQdrrEidUAgBAMh8Sj4ZNjF76-oPUHxm2GQCK16lqcpjSlpCvCgfcy-YzPaHR7"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body NotificationSender body);

    class MyResponse{
        int success;
    }
}

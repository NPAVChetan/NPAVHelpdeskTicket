package com.npav.npavhelpdeskticket.api;

import com.npav.npavhelpdeskticket.pojo.PostMessage;
import com.npav.npavhelpdeskticket.pojo.PostNote;
import com.npav.npavhelpdeskticket.pojo.Ticket;
import com.npav.npavhelpdeskticket.pojo.Tickets;
import com.npav.npavhelpdeskticket.pojo.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Url;

public interface APIInterface {
    //    String BASE_URL = "https://www.support.test.netprotector.net/";
    String BASE_URL = "https://www.support.test.netprotector.net/";

    // Authentication URL ================================================================================
    @POST("api/agent/auth/login")
    Call<User> login(@Body User user);

    @POST("api/customer/bank/list")
    Call<Ticket> ticketlist(@Header("Authorization") String token, @Body Ticket ticket);

    @GET
    Call<Ticket> getticketlist(@Header("Authorization") String token, @Url String url);

    @GET
    Call<Tickets> getticketlist1(@Header("Authorization") String token, @Url String url);

    @GET
    Call<Ticket> getticketdetails(@Header("Authorization") String token, @Url String url);

    @POST("api/ticket/web/note")
    Call<PostNote> post_note(@Header("Authorization") String token, @Body PostNote note);

    @POST("api/ticket/web/message")
    Call<PostMessage> post_message(@Header("Authorization") String token, @Body PostMessage postMessage);

    @PUT
    Call<Tickets> updateTicketStatus(@Header("Authorization") String token, @Body Tickets tickets, @Url String url);
/*
    @POST("authentication/verifyotpwithflag")
    Call<User> verifyotpwithflag(@Header("Authorization") String token, @Body User user);



    @GET
    Call<Healthcares> getHelthcare(@Header("Authorization") String token, @Url String url);*/


}

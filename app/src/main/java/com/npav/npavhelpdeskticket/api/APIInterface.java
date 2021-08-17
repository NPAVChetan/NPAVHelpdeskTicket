package com.npav.npavhelpdeskticket.api;

import com.npav.npavhelpdeskticket.pojo.Assign;
import com.npav.npavhelpdeskticket.pojo.PostMessage;
import com.npav.npavhelpdeskticket.pojo.PostNote;
import com.npav.npavhelpdeskticket.pojo.Tag;
import com.npav.npavhelpdeskticket.pojo.Ticket;
import com.npav.npavhelpdeskticket.pojo.Tickets;
import com.npav.npavhelpdeskticket.pojo.User;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Url;

public interface APIInterface {
    String BASE_URL = "https://www.support.test.netprotector.net/";
//    String BASE_URL = "https://www.support.netprotector.net/";

    @POST("api/agent/auth/login")
    Call<User> login(@Body User user);

    @GET
    Call<Tickets> getticketlist(@Header("Authorization") String token, @Url String url);

    @GET
    Call<Ticket> getticketdetails(@Header("Authorization") String token, @Url String url);

    @Multipart
    @POST("api/ticket/web/note")
    Call<PostNote> post_note(@Header("Authorization") String token, @Part MultipartBody.Part attachment, @Part("ticketID") RequestBody ticketID, @Part("agentID") RequestBody agentID, @Part("internalNote") RequestBody internalNote);

    @Multipart
    @POST("api/ticket/web/message")
    Call<PostMessage> post_message(@Header("Authorization") String token, @Part MultipartBody.Part attachment, @Part("ticketID") RequestBody ticketID, @Part("agentID") RequestBody agentID, @Part("ticketType") RequestBody ticketType, @Part("messageText") RequestBody messageText);

    @PUT
    Call<Tickets> updateTicketStatus(@Header("Authorization") String token, @Body Tickets tickets, @Url String url);

    @POST("api/ticket/web/tags")
    Call<Tag> add_tags(@Header("Authorization") String token, @Body Tag tag);

    @PUT
    Call<Assign> assign_ticket(@Header("Authorization") String token, @Body Assign assign, @Url String url);

}

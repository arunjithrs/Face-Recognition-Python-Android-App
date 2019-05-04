package com.example.arunjith.homeprosecurity.api;

import com.example.arunjith.homeprosecurity.model.Image;
import com.example.arunjith.homeprosecurity.model.Users;
import com.example.arunjith.homeprosecurity.model.VisitorsModel;

import java.sql.Blob;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface Api {

    @FormUrlEncoded
    @POST("user")
    Call<Image> sendImage(
            @Field("name") String name,
            @Field("image") String image,
            @Field("access") Boolean access
    );

    @FormUrlEncoded
    @POST("access")
    Call<Image> access(
            @Field("name") String name,
            @Field("access") Boolean access
    );

    @FormUrlEncoded
    @POST("delete")
    Call<Image> deleteUser(
            @Field("name") String name
    );

    @FormUrlEncoded
    @POST("private/update")
    Call<Image> updateSettingsMode(
            @Field("status") Boolean status
    );

    @GET("private")
    Call<Image> getSettingsMode();

    @GET("reboot")
    Call<Image> rebootServer();

    @GET("users")
    Call<List<Users>> getUsersList();

    @GET("visitors")
    Call<List<VisitorsModel>> getVisitors();

    @GET("allow")
    Call<Image> allowPermission();

    @GET("deny")
    Call<Image> denyPermission();

}

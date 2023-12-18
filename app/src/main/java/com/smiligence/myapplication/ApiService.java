package com.smiligence.myapplication;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ApiService {

    @GET("users")
    Call<List<User>> getemployeeList();

    @GET("users")
    Call<List<Usetdetails>> getuserDetailList(@Query("id") int param1);

}

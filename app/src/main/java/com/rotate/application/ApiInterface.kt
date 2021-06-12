package com.rotate.application

import retrofit2.Call
import retrofit2.Callback

import retrofit2.http.GET
import retrofit2.http.Query


interface ApiInterface {
    @GET("tobtc")
    fun getUser(
        @Query("currency") string: String, @Query("value") value: String): Call<String>

}
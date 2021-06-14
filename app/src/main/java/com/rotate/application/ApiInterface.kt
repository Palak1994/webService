package com.rotate.application

import com.rotate.application.data.model.Convert
import retrofit2.Call

import retrofit2.http.GET
import retrofit2.http.Query


interface ApiInterface {
    @GET("data/price")
     fun getUser(
        @Query("fsym") string: String, @Query("tsyms") value: String
    ): Call<Convert>

}
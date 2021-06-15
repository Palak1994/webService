package com.rotate.application.Repository

import android.content.Context
import com.rotate.application.ApiInterface
import com.rotate.application.AppConstant
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiFactory {

    private val client = OkHttpClient.Builder().build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(AppConstant.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    fun<T> buildService(service: Class<T>): T{
        return retrofit.create(service)
    }
    fun getUsdService(context: Context): ApiInterface =
        buildRetrofitClient(context).create(ApiInterface::class.java)

    private fun buildRetrofitClient(context: Context): Retrofit {
        val okHttpClient =
            getOkHttpClient(context, makeHttpLoggingInterceptor())
        return makeErosNowApiService(
            okHttpClient
        )
    }

    private fun getOkHttpClient(
        context: Context,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()

        return okHttpClientBuilder
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()

    }

    private fun makeErosNowApiService(okHttpClient: OkHttpClient):
            Retrofit = Retrofit.Builder()
        .baseUrl(AppConstant.BASE_URL)
        .client(okHttpClient)
        .build()

    private fun makeHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return logging
    }

}
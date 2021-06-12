package com.rotate.application.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rotate.application.ApiGenerator
import com.rotate.application.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiRepository {
    private var apiInterface: ApiInterface? = null

    init {
        apiInterface = ApiGenerator.getApiClient().create(ApiInterface::class.java)
    }

    fun fetchData(value: String, type: String): LiveData<String> {
        val data = MutableLiveData<String>()

        apiInterface?.getUser(value, type)?.enqueue(object : Callback<String> {

            override fun onFailure(call: Call<String>, t: Throwable) {
                data.value = null
            }

            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {

                val res = response.body()
                if (response.code() == 200 && res != null) {
                    data.value = res
                } else {
                    data.value = null
                }

            }
        })

        return data

    }

}
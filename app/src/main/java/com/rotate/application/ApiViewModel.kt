package com.rotate.application

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rotate.application.data.ApiRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiViewModel(application: Application):AndroidViewModel(application) {
    private var homeRepository:ApiRepository?=null
    var postModelListLiveData : LiveData<String>?=null
    init {
        homeRepository = ApiRepository()
        postModelListLiveData = MutableLiveData()
    }
    fun fetchData(value:String,type:String){
        postModelListLiveData = homeRepository?.fetchData(value,type)
    }

}
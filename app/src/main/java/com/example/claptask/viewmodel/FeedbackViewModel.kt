package com.example.claptask.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.claptask.model.ResponseData
import com.example.claptask.networkServices.ApiService
import com.example.claptask.networkServices.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FeedbackViewModel : ViewModel() {

    private val _feedbackData = MutableLiveData<ResponseData>()
    val feedbackData: LiveData<ResponseData> get() = _feedbackData

    private val apiService: ApiService by lazy {
        RetrofitInstance.retrofit.create(ApiService::class.java)
    }

    //making network call using retrofit
    fun fetchFeedbackData() {
        apiService.getFeedbackData().enqueue(object : Callback<ResponseData> {
            override fun onResponse(call: Call<ResponseData>, response: Response<ResponseData>) {
                if (response.isSuccessful) {
                    _feedbackData.value = response.body()
                    response.body()?.let { responseData ->
                        responseData.feedbackCategories.forEach { category ->
                            category.feedbackItems.forEach { item ->
                            }
                        }
                    }
                } else {
                    Log.e("FeedbackViewModel", "Response error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ResponseData>, t: Throwable) {
                Log.e("FeedbackViewModel", "Network call failed: ${t.message}")
            }
        })
    }
}

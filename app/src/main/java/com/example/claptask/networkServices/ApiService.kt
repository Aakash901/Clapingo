package com.example.claptask.networkServices

import com.example.claptask.model.ResponseData
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    //end point of url for getting feedback data
    @GET("rating/getFeedbackData")
    fun getFeedbackData(): Call<ResponseData>
}

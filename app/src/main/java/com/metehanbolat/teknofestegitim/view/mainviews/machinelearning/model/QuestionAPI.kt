package com.metehanbolat.teknofestegitim.view.mainviews.machinelearning.model

import retrofit2.Call
import retrofit2.http.GET

interface QuestionAPI {
    @GET("WangShuKC/teknofestFiles/main/questions.json")
    fun getData(): Call<List<QuestionModel>>
}
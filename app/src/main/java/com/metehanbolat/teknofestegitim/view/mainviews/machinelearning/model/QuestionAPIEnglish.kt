package com.metehanbolat.teknofestegitim.view.mainviews.machinelearning.model

import retrofit2.Call
import retrofit2.http.GET

interface QuestionAPIEnglish {

    @GET("WangShuKC/teknofestFiles/main/english.json")
    fun getData(): Call<List<QuestionModel>>
}
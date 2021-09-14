package com.metehanbolat.teknofestegitim.view.mainviews.artificialintelligence.getdata

import retrofit2.http.GET

interface RetrofitInterface {

    @get:GET("bedelll/teknofestegitim/main/teknofestyapayzeka.gson")
    val posts : retrofit2.Call<List<PostModel>>

    companion object {
        const val BASE_URL ="https://raw.githubusercontent.com"
    }

}
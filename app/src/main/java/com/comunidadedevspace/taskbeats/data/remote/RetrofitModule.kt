package com.comunidadedevspace.taskbeats.data.remote

import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitModule {

    fun createNewsService(): NewsService {

        val retrofit = Retrofit
            .Builder()
            .baseUrl("https://inshorts.deta.dev/")
            .addConverterFactory(GsonConverterFactory.create(Gson()))

        return retrofit
            .build()
            .create(NewsService::class.java)
    }
}
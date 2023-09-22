package com.emilabdurahmanli.githubtask.network

import com.emilabdurahmanli.githubtask.Constant
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClient {
     fun api() : Api {
        val retrofit: Retrofit = Retrofit.Builder().baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create<Api>(Api::class.java)
    }
}
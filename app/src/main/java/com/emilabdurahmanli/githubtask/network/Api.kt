package com.emilabdurahmanli.githubtask.network

import com.emilabdurahmanli.githubtask.network.model.Response
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface Api {

    @GET("search/repositories")
    fun getRepositories(
        @Query("q") date: String,
        @Query("sort") sort: String,
        @Query("order") order: String,
        @Query("page") page: Int
    ): Call<Response>
}
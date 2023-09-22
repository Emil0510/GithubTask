package com.emilabdurahmanli.githubtask.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface Api {

    @GET("search/repositories")
    fun getRepositories(
        @Query("q") date: String,
        @Query("sort") sort: String,
        @Query("order") order: String
    ): Call<Response>
}
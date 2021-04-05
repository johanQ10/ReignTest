package com.johan.reigntest.api

import com.johan.reigntest.model.Hits
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface HomeApi {
    @GET("search_by_date")
    fun getHits(@Query("query") query: String?,
                @Query("page") page: Int?,
                @Query("hitsPerPage") hitsPerPage: Int?): Call<Hits?>
}
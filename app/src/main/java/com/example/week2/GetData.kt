package com.example.week2

import com.example.week2.data.AllNews
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface GetData {
    @GET("articlesearch.json?api-key=AjhPWDzU3GBWAP07PxuhKqEp80HqP2cx")
    fun getDataSearch(@QueryMap query : Map<String , String>,
                      @Query("fq[]") news_desk: ArrayList<String>  ) : Call<AllNews>
}
package com.hera.coinlistapp.api

import com.hera.coinlistapp.response.ResponseCoinsList
import com.hera.coinlistapp.response.ResponseDetails
import com.hera.coinlistapp.utils.Constants.END_POINT
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("coins/markets?sparkline=true")
    suspend fun getCoinsList(@Query("vs_currency") vs_currency: String):Response<ResponseCoinsList>

    @GET("coins/{id}?sparkline=true")
    suspend fun getDetailsCoin(@Path("id") id :String):Response<ResponseDetails>
}
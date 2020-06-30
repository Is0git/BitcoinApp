package com.is0git.bitcoin.network.services

import com.is0git.bitcoin.network.models.BpiResponse
import retrofit2.Response
import retrofit2.http.GET

interface BpiService {

    @GET("v1/bpi/currentprice.json")
    suspend fun getBpi() : Response<BpiResponse>
}
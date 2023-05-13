package com.chathil.rocketes.data.service

import com.chathil.rocketes.data.model.dto.RocketResponse
import retrofit2.http.GET

interface RocketService {

    @GET("rockets")
    suspend fun loadRockets(): List<RocketResponse>
}
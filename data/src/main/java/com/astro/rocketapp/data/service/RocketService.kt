package com.astro.rocketapp.data.service

import com.astro.rocketapp.data.model.dto.RocketResponse
import retrofit2.http.GET

interface RocketService {

    @GET("rockets")
    suspend fun loadRockets(): List<RocketResponse>
}
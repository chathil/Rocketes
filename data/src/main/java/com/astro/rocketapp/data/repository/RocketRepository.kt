package com.astro.rocketapp.data.repository

import com.astro.rocketapp.data.model.domain.RocketDetailModel
import com.astro.rocketapp.data.model.domain.RocketModel
import com.astro.rocketapp.data.vo.Resource
import kotlinx.coroutines.flow.Flow

interface RocketRepository {

    fun fetchRockets(): Flow<Resource<List<RocketModel>>>

    fun fetchRocket(id: String): Flow<Resource<RocketDetailModel>>
}

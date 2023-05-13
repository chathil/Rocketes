package com.chathil.rocketes.data.repository

import com.chathil.rocketes.data.model.domain.RocketDetailModel
import com.chathil.rocketes.data.model.domain.RocketModel
import com.chathil.rocketes.data.vo.Resource
import kotlinx.coroutines.flow.Flow

interface RocketRepository {

    fun fetchRockets(): Flow<Resource<List<RocketModel>>>

    fun fetchRocket(id: String): Flow<Resource<RocketDetailModel>>
}

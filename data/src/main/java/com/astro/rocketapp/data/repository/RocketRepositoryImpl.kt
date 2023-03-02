package com.astro.rocketapp.data.repository

import com.astro.rocketapp.data.cache.InMemoryCache
import com.astro.rocketapp.data.model.domain.RocketDetailModel
import com.astro.rocketapp.data.model.domain.RocketModel
import com.astro.rocketapp.data.model.domain.asRocketModels
import com.astro.rocketapp.data.model.dto.asDomainModel
import com.astro.rocketapp.data.service.RocketService
import com.astro.rocketapp.data.vo.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class RocketRepositoryImpl(
    private val cache: InMemoryCache<String, RocketDetailModel>,
    private val service: RocketService
) : RocketRepository {

    override fun fetchRockets(): Flow<Resource<List<RocketModel>>> {
        return flow {
            emit(Resource.Loading())
            try {
                val result = service.loadRockets().map { response ->
                    val rocket = response.asDomainModel()
                    cache.store(rocket.id, rocket)
                }.asRocketModels()

                emit(Resource.Success(result))
            } catch (e: Exception) {
                emit(Resource.Error(e))
            }
        }
    }

    override fun fetchRocket(id: String): Flow<Resource<RocketDetailModel>> {
        return if (cache.exist(id)) {
            flowOf(
                cache.fetch(id)?.let { rocket -> Resource.Success(rocket) }
                    ?: Resource.Error(error = Throwable("Something went wrong"))
            )
        } else {
            flow {
                emit(Resource.Loading())
                try {
                    service.loadRockets().forEach { response ->
                        val rocket = response.asDomainModel()
                        cache.store(rocket.id, rocket)
                    }
                    emit(cache.fetch(id)?.let { rocket -> Resource.Success(rocket) }
                        ?: Resource.Error(error = Throwable("No rocket with id: $id")))
                } catch (e: Exception) {
                    emit(Resource.Error(e))
                }
            }
        }
    }
}

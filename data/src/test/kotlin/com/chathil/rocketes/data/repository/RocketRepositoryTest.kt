package com.chathil.rocketes.data.repository

import com.chathil.rocketes.data.cache.InMemoryCache
import com.chathil.rocketes.data.model.domain.RocketDetailModel
import com.chathil.rocketes.data.model.domain.RocketModel
import com.chathil.rocketes.data.model.domain.asRocketModels
import com.chathil.rocketes.data.model.dto.RocketResponse
import com.chathil.rocketes.data.model.dto.asDomainModel
import com.chathil.rocketes.data.service.RocketService
import com.chathil.rocketes.data.vo.Resource
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class RocketRepositoryTest {

    private val cache: InMemoryCache<String, RocketDetailModel> = mock()
    private val rocketService: RocketService = mock()

    private val repository = RocketRepositoryImpl(cache, rocketService)

    private val rocketResponse = listOf(
        RocketResponse(
            id = "id",
            name = "name",
            description = "desc",
            flickrImages = listOf("img"),
            country = "country",
            costPerLaunch = 1,
            firstFlight = "2006-03-24"
        )
    )

    @Test
    fun `successfully fetched rockets`() = runTest {
        val rocketDetail = rocketResponse.map(RocketResponse::asDomainModel)
        whenever(rocketService.loadRockets()).thenReturn(rocketResponse)
        whenever(
            cache.store(
                rocketDetail.first().id,
                rocketDetail.first()
            )
        ).thenReturn(rocketDetail.first())

        repository.fetchRockets().toList().apply {
            assertEquals(2, count())
            assertTrue(first() is Resource.Loading<List<RocketModel>>)
            assertEquals(Resource.Success(rocketDetail.asRocketModels()), get(1))
        }

        verify(rocketService).loadRockets()
        verify(cache).store(rocketDetail.first().id, rocketDetail.first())
    }

    @Test
    fun `failed to to fetch rockets`() = runTest {
        val throwable = Exception("error")
        val rocketDetail = rocketResponse.map(RocketResponse::asDomainModel)
        whenever(rocketService.loadRockets()).thenAnswer { throw throwable }

        repository.fetchRockets().toList().apply {
            assertEquals(2, count())
            assertTrue(first() is Resource.Loading<List<RocketModel>>)
            assertEquals(Resource.Error<List<RocketModel>>(throwable), get(1))
        }

        verify(rocketService).loadRockets()
        verify(cache, never()).store(rocketDetail.first().id, rocketDetail.first())
    }

    @Test
    fun `Successfully fetch rocket detail from cache`() = runTest {
        val id = "id"
        val rocketDetail = rocketResponse.map(RocketResponse::asDomainModel)

        whenever(cache.exist(id)).thenReturn(true)
        whenever(cache.fetch(id)).thenReturn(rocketDetail.first())

        repository.fetchRocket(id).toList().apply {
            assertEquals(1, count())
            assertEquals(Resource.Success(rocketDetail.first()), first())
        }

        verify(rocketService, never()).loadRockets()
        verify(cache).exist(id)
    }
}

package com.astro.rocketapp.data.model.dto

import com.astro.rocketapp.data.model.domain.RocketDetailModel
import com.astro.rocketapp.data.util.toDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RocketResponse(
    val id: String,
    val name: String,
    val description: String,
    val flickrImages: List<String>,
    val country: String,
    val costPerLaunch: Long,
    val firstFlight: String
)

fun RocketResponse.asDomainModel(): RocketDetailModel {
    return RocketDetailModel(
        id = id,
        name = name,
        desc = description,
        img = flickrImages.firstOrNull() ?: "",
        country = country,
        costPerLaunch = costPerLaunch,
        firstFlight = firstFlight.toDate()
    )
}

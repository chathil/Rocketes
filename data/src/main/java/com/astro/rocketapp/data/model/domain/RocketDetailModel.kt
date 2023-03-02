package com.astro.rocketapp.data.model.domain

import java.util.Date

data class RocketDetailModel(
    val id: String,
    val name: String,
    val desc: String,
    val img: String,
    val country: String,
    val costPerLaunch: Long,
    val firstFlight: Date
) {
    companion object {
        val empty = RocketDetailModel(
            id = "",
            name = "",
            desc = "",
            img = "",
            country = "",
            costPerLaunch = 0,
            firstFlight = Date()
        )
    }
}

fun RocketDetailModel.asRocketModel(): RocketModel {
    return RocketModel(id = id, name = name, desc = desc, img = img)
}

fun List<RocketDetailModel>.asRocketModels() = map(RocketDetailModel::asRocketModel)

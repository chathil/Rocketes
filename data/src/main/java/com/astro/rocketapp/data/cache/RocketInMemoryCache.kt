package com.astro.rocketapp.data.cache

import com.astro.rocketapp.data.model.domain.RocketDetailModel

class RocketInMemoryCache : InMemoryCache<String, RocketDetailModel> {
    private val cache: MutableMap<String, RocketDetailModel> = mutableMapOf()

    override fun exist(key: String): Boolean = cache[key] != null

    override fun fetch(key: String): RocketDetailModel? = cache[key]

    override fun store(key: String, value: RocketDetailModel): RocketDetailModel =
        value.also { rocket -> cache[key] = rocket }

    override fun clear(key: String) = cache.clear()
}

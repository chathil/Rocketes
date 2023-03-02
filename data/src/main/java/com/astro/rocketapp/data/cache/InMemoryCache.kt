package com.astro.rocketapp.data.cache

interface InMemoryCache<T : Any, U : Any> {
    fun exist(key: T): Boolean
    fun fetch(key: T): U?
    fun store(key: T, value: U): U
    fun clear(key: T)
}

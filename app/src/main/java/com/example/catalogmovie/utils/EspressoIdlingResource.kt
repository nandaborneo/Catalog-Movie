package com.example.catalogmovie.utils

import androidx.test.espresso.IdlingResource
import androidx.test.espresso.idling.CountingIdlingResource

object EspressoIdlingResource {

    private const val RESOURCE = "GLOBAL"
    private val espressoTestIdlingResource = CountingIdlingResource(RESOURCE)

    fun increment() {
        espressoTestIdlingResource.increment()
    }

    fun decrement() {
        if (!espressoTestIdlingResource.isIdleNow){
            espressoTestIdlingResource.decrement()
        }
    }

    fun getEspressoIdlingResourceForMainActivity(): IdlingResource = espressoTestIdlingResource
}
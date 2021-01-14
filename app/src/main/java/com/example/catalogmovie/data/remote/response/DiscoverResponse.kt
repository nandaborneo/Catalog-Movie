package com.example.catalogmovie.data.remote.response

data class DiscoverResponse(
    val page: Int? = 0,
    var results: List<MovieItemReponse>? = mutableListOf(),
    val total_pages: Int? = 0,
    val total_results: Int? = 0
)
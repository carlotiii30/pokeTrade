package com.example.pokemontrade.utils

fun resolveImageUrl(path: String?): String? {
    if (path.isNullOrBlank()) return null

    return if (path.startsWith("/")) {
        val baseUrl = "http://10.0.2.2:8000"
        "$baseUrl$path"
    } else {
        path.replace("localhost", "10.0.2.2")
    }
}

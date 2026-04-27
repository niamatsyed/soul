package com.kindfind

data class LostFoundPost(
    val title: String,
    val uniqueIdentifier: String,
    val details: String,
    val location: String,
    val contact: String,
    val imageUrl: String = ""
)

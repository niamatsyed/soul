package com.kindfind

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class KindFindViewModel : ViewModel() {
    private val _posts = MutableStateFlow(
        listOf(
            LostFoundPost(
                title = "Found Wallet",
                uniqueIdentifier = "Driving License ending 7481",
                details = "Brown leather wallet with bank cards.",
                location = "Dubai Metro Station",
                contact = "finderA@kindfind.app"
            ),
            LostFoundPost(
                title = "Found Kid (Safe with Police)",
                uniqueIdentifier = "Blue shirt, name Arjun, school badge ID 992",
                details = "Child was found alone and handed to local police help desk.",
                location = "Mumbai Central",
                contact = "+91-90000-11111"
            )
        )
    )
    val posts = _posts.asStateFlow()

    fun addPost(post: LostFoundPost) {
        _posts.update { listOf(post) + it }
    }

    fun searchPosts(query: String): List<LostFoundPost> {
        if (query.isBlank()) return posts.value

        val q = query.trim().lowercase()
        return posts.value.filter {
            it.title.lowercase().contains(q) ||
                it.uniqueIdentifier.lowercase().contains(q) ||
                it.details.lowercase().contains(q) ||
                it.location.lowercase().contains(q)
        }
    }
}

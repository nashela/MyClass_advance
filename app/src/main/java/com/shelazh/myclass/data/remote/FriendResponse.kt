package com.shelazh.myclass.data.remote

import com.crocodic.core.api.ModelResponse
import com.shelazh.myclass.data.local.User

data class FriendResponse (
    val data: List<User>
): ModelResponse()
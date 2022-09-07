package com.sehun.android_pet_community.Model

data class ChatModel (
    var user :MutableMap<String, Boolean>? = null,
    var comments : MutableMap<String, Comment>? = null, ) {
    data class Comment(
        var uid : String? = null,
        var message : String? = null,
    )
}


package com.sehun.android_pet_community.Model

data class ChatModel (
    var usersList : List<String> = listOf(),
    var users :MutableMap<String, Boolean>? = mutableMapOf(),
    var comments : MutableMap<String, String>? = mutableMapOf(), )

{
    data class Comment(
        var uid : String = "",
        var message : String = "",
    )
}


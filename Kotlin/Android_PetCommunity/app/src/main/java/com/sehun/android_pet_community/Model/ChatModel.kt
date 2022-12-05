package com.sehun.android_pet_community.Model

data class ChatModel (
    var usersList : List<String> = listOf(),
    var users :List<Map<String, Boolean>> = listOf(),
    var comments : List<Map<String, String>> = listOf() )



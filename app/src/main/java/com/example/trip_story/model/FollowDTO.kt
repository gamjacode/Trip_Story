package com.example.trip_story.model

data class FollowDTO(
    var followerCount : Int = 0,
    var followers : MutableMap<String,Boolean> = HashMap(), //중복을 방지하기 위해 map으로
    var followingCount : Int = 0,
    var followings : MutableMap<String,Boolean> = HashMap()
)
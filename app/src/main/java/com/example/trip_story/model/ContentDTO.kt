package com.example.trip_story.model

import android.net.Uri

//컨텐츠의 설명을 관리하는 explain, 이미지 주소를 관리하는 imageUrl, 어느 유저가 올렸는지 id를 관리해주는 uid,
//올린 유저의 이미지를 관리해주는 userId, 언제 올렸는지 관리해주는 timestamp, 좋아요를 몇개 눌렀는지 관리해주는 favorite,
//중복 좋아요를 방지하도록 좋아요를 눌른 유저를 관리해는 favorites
data class ContentDTO(var explain : String? = null,
                      var imageUrl : String? = null,
                      var uid : String? = null,
                      var userId : String? = null,
                      var timestamp : Long? = null,
                      var favoriteCount : Int = 0,
                      var favorities : MutableMap<String,Boolean> = HashMap(),
                      var trip_name : String? = null
)
{
    //나중에 댓글을 관리해주는 data class
    data class Comment(var uid : String? = null,
                       var userId : String? = null,
                       var comment : String? = null,
                       var timestamp : Long? = null)
}


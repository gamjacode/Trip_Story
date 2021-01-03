package com.example.trip_story.model

import java.io.Serializable

data class Data(
    var name:String,var time:String,var image: String
): Serializable{
    constructor() : this(
    "","",""
    )
}
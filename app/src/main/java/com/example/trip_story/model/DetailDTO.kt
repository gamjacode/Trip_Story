package com.example.trip_story.model

import android.net.Uri

data class DetailDTO(var multiimages : ArrayList<Uri>? = null,
                     var trip_name : String? = null,
                     var timestamp : Long? = null,
                     var imageUrl : String? = null)
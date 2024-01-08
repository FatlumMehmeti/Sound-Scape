package com.example.sound_scape

data class Music(
    var songTitle: String = "",
    var artistname: String = "",
    var albumName: String = "",
    val isRemote: Boolean, // true if the image is a remote URL, false if it's a drawable resource
    val imageUrl: String? = null , // URL for remote image
    val imageResourceId: Int? = null, // Drawable resource ID
    var releaseyear: String = "",
    var gener: String = "",
    var audioUrl: String = ""

    ) {
    // Add a no-argument constructor
}
//data class Music(val id:String? = null,val title: String? = null,val album:String? = null,val artist:String? = null,val path:String? = null,
//                 val artUri:String? = null){}
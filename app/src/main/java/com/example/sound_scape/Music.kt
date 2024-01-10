package com.example.sound_scape

data class Music(
    var songtitle: String = "",
    var artistname: String = "",
    var albumname: String = "",
    var releaseyear: String = "",
    var gener: String = "",
    val imageUrl: String? = null ,
    var favorite:Boolean =false,
    val songPlayed:Long =0,
    var audioUrl: String = ""

    ) {}
//data class Music(val id:String? = null,val title: String? = null,val album:String? = null,val artist:String? = null,val path:String? = null,
//                 val artUri:String? = null){}
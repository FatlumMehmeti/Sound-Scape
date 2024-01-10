package com.example.teste_per_app.settings.reporitoris_for_settings

data class AddSong(val songtitle : String? = null,val artistname : String? = null,val albumname:String?=null,val releaseyear : String? = null,
                   val gener : String? = null,
                   val imageUrl: String?=null,  // Reference or path to the image file
                   val audioUrl: String? =null){}


package com.example.sound_scape.activity.recommendation.repository

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Genre(
    val name: String,
    val songs: Map<String, String>
) : Parcelable
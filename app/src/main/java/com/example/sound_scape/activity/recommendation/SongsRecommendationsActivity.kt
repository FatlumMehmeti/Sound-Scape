package com.example.sound_scape.activity.recommendation

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.sound_scape.R

class SongsRecommendationsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_songs_recommendations)

        val titleTextView = findViewById<TextView>(R.id.titleTextView)
        val songsListView = findViewById<ListView>(R.id.songsListView)

        val selectedGenres = intent.getStringArrayListExtra("selectedGenres")

        val title = selectedGenres?.let {
            "Recommended Songs for ${it.joinToString(", ")}"
        } ?: "No genres selected"
        titleTextView.text = title

        val recommendedSongs = generateRecommendedSongs(selectedGenres)

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, recommendedSongs)
        songsListView.adapter = adapter
    }

    private fun generateRecommendedSongs(selectedGenres: ArrayList<String>?): List<String> {
        val suggestedSongsPerGenre = 2
        val totalSuggestedSongs = selectedGenres?.size?.times(suggestedSongsPerGenre) ?: 0

        return List(totalSuggestedSongs) { index ->
            "Suggested Song ${index + 1}"
        }
    }
}

package com.example.teste_per_app.activity.recommendation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import com.example.sound_scape.R

class SongsRecommendationsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_songs_recommendations)

        val titleTextView = findViewById<TextView>(R.id.titleTextView)
        val songsListView = findViewById<ListView>(R.id.songsListView)

        // Retrieve the selected genres from the intent
        val selectedGenres = intent.getStringArrayListExtra("selectedGenres")

        // Update the title based on the selected genres
        "Recommended Songs for You".also { titleTextView.text = it }


        val recommendedSongs = generateRecommendedSongs(selectedGenres)


        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, recommendedSongs)
        songsListView.adapter = adapter

        if (selectedGenres != null) {
            "Recommended Songs for You".also { titleTextView.text = it }

        } else {
            "No genres selected".also { titleTextView.text = it } // Handle the case when no genres are selected
        }
    }

    private fun generateRecommendedSongs(selectedGenres: ArrayList<String>?): Array<String> {
        val suggestedSongsPerGenre = 3
        val totalSuggestedSongs = selectedGenres?.size?.times(suggestedSongsPerGenre) ?: 0

        return Array(totalSuggestedSongs) { index ->
            "Suggested Song ${index + 1}"
        }
    }
}
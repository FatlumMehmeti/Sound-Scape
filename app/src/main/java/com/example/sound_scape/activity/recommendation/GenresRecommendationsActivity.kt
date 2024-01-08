package com.example.teste_per_app.activity.recommendation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.example.sound_scape.R

class GenresRecommendationsActivity : AppCompatActivity() {

    private var selectedGenres = mutableSetOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_genres_recommendations)

        val rockCardView = findViewById<CardView>(R.id.rockCardView)
        val popCardView = findViewById<CardView>(R.id.popCardView)
        val hipPopCardView = findViewById<CardView>(R.id.hipPopCardView)
        val rbCardView = findViewById<CardView>(R.id.rbcardView)
        val soulCardView = findViewById<CardView>(R.id.soulCardView)
        val nasheedCardView = findViewById<CardView>(R.id.nasheedCardView)
        val classicalCardView = findViewById<CardView>(R.id.classicalCardView)
        val kpopCardView = findViewById<CardView>(R.id.kpopCardView)
        val latinCardView = findViewById<CardView>(R.id.latinCardView)
        val edmCardView = findViewById<CardView>(R.id.edmCardView)

        rockCardView.setOnClickListener{toggleGenreSelection("Rock")
            rockCardView.setBackgroundResource(if (rockCardView.isSelected) R.drawable.cardview_bg else R.drawable.white_bt)}
        popCardView.setOnClickListener{toggleGenreSelection("Pop")
            popCardView.setBackgroundResource(if (popCardView.isSelected) R.drawable.cardview_bg else R.drawable.white_bt)}
        hipPopCardView.setOnClickListener{toggleGenreSelection("Hip Pop")
            hipPopCardView.setBackgroundResource(if (hipPopCardView.isSelected) R.drawable.cardview_bg else R.drawable.white_bt)}
        rbCardView.setOnClickListener{toggleGenreSelection("R&B")
            rbCardView.setBackgroundResource(if (rbCardView.isSelected) R.drawable.cardview_bg else R.drawable.white_bt)}
        soulCardView.setOnClickListener{toggleGenreSelection("Soul")
            soulCardView.setBackgroundResource(if (soulCardView.isSelected) R.drawable.cardview_bg else R.drawable.white_bt)}
        nasheedCardView.setOnClickListener{toggleGenreSelection("Nasheed")
            nasheedCardView.setBackgroundResource(if (nasheedCardView.isSelected) R.drawable.cardview_bg else R.drawable.white_bt)}
        classicalCardView.setOnClickListener{toggleGenreSelection("Classical")
            classicalCardView.setBackgroundResource(if (classicalCardView.isSelected) R.drawable.cardview_bg else R.drawable.white_bt)}
        kpopCardView.setOnClickListener{toggleGenreSelection("K-Pop")
            kpopCardView.setBackgroundResource(if (kpopCardView.isSelected) R.drawable.cardview_bg else R.drawable.white_bt)}
        latinCardView.setOnClickListener{toggleGenreSelection("Latin")
            latinCardView.setBackgroundResource(if (latinCardView.isSelected) R.drawable.cardview_bg else R.drawable.white_bt)}
        edmCardView.setOnClickListener{toggleGenreSelection("EDM")
            edmCardView.setBackgroundResource(if (edmCardView.isSelected) R.drawable.cardview_bg else R.drawable.white_bt)}



        val finishButton = findViewById<Button>(R.id.finishbutton)
        finishButton.setOnClickListener {
            // Launch another activity or fragment to display recommended songs
            if (selectedGenres.isNotEmpty()) {
                val intent = Intent(this, SongsRecommendationsActivity::class.java)
                intent.putStringArrayListExtra("selectedGenres", ArrayList(selectedGenres))
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please select at least one genre.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun toggleGenreSelection(genre: String) {
        if (selectedGenres.contains(genre)) {
            selectedGenres.remove(genre)
        } else {
            selectedGenres.add(genre)
        }
        updateCardViewSelection()
    }


    private fun updateCardViewSelection() {
        val rockCardView = findViewById<CardView>(R.id.rockCardView)
        val popCardView = findViewById<CardView>(R.id.popCardView)
        val hipPopCardView = findViewById<CardView>(R.id.hipPopCardView)
        val rbCardView = findViewById<CardView>(R.id.rbcardView)
        val soulCardView = findViewById<CardView>(R.id.soulCardView)
        val nasheedCardView = findViewById<CardView>(R.id.nasheedCardView)
        val classicalCardView = findViewById<CardView>(R.id.classicalCardView)
        val kpopCardView = findViewById<CardView>(R.id.kpopCardView)
        val latinCardView = findViewById<CardView>(R.id.latinCardView)
        val edmCardView = findViewById<CardView>(R.id.edmCardView)

        rockCardView.isSelected=selectedGenres.contains("Rock")
        popCardView.isSelected=selectedGenres.contains("Pop")
        hipPopCardView.isSelected=selectedGenres.contains("Hip Pop")
        rbCardView.isSelected=selectedGenres.contains("R&B")
        soulCardView.isSelected=selectedGenres.contains("Soul")
        nasheedCardView.isSelected=selectedGenres.contains("Nasheed")
        classicalCardView.isSelected=selectedGenres.contains("Classical")
        kpopCardView.isSelected=selectedGenres.contains("K-Pop")
        latinCardView.isSelected=selectedGenres.contains("Latin")
        edmCardView.isSelected=selectedGenres.contains("EDM")

    }

    private fun showRecommendations() {
        if (selectedGenres.isNotEmpty()) {
            val intent = Intent(this, SongsRecommendationsActivity::class.java)
            intent.putStringArrayListExtra("selectedGenres", ArrayList(selectedGenres))
            startActivity(intent)
        } else {
            Toast.makeText(this, "Please select at least one genre.", Toast.LENGTH_SHORT).show()
        }
    }
}
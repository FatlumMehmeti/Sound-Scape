//package com.example.sound_scape.activity.recommendation
//
//import android.content.Context
//import android.content.Intent
//import android.os.Bundle
//import android.widget.Button
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.cardview.widget.CardView
//import com.example.sound_scape.HomeFragment
//import com.example.sound_scape.MainActivity
//import com.example.sound_scape.Music
//import com.example.sound_scape.R
//import com.example.sound_scape.activity.authentication.LoginActivity
//import com.example.sound_scape.activity.recommendation.repository.Genre
//import com.google.firebase.FirebaseApp
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.database.DataSnapshot
//import com.google.firebase.database.DatabaseError
//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.FirebaseDatabase
//import com.google.firebase.database.ValueEventListener
//
//class GenresRecommendationsActivity : AppCompatActivity() {
//
//    private var selectedGenres = mutableSetOf<Genre>()
//    private lateinit var databaseReference: DatabaseReference
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_genres_recommendations)
//
//        FirebaseApp.initializeApp(this)
//
//        databaseReference = FirebaseDatabase.getInstance().reference.child("genres")
//
//        val auth = FirebaseAuth.getInstance()
//        val currentUser = auth.currentUser
//
//        if (currentUser != null) {
//            val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
//            val isNewUser = sharedPreferences.getBoolean("isNewUser", false)
//            val isInitialSetupComplete = sharedPreferences.getBoolean("isInitialSetupComplete", false)
//
//            if (isNewUser) {
//                val editor = sharedPreferences.edit()
//                editor.putBoolean("isNewUser", false)
//                editor.apply()
//
//                initializeGenreCardViews()
//                initializeFinishButton()
//
//            } else if (!isInitialSetupComplete) {
//                initializeGenreCardViews()
//                initializeFinishButton()
//
//                val editor = sharedPreferences.edit()
//                editor.putBoolean("isInitialSetupComplete", true)
//                editor.apply()
//
//            } else {
//                startActivity(Intent(this, MainActivity::class.java))
//                finish()
//                Toast.makeText(this, "Welcome back!", Toast.LENGTH_SHORT).show()
//                return
//            }
//        } else {
//            startActivity(Intent(this, LoginActivity::class.java))
//            finish()
//            Toast.makeText(this, "Access denied. User not authenticated.", Toast.LENGTH_SHORT).show()
//            return
//        }
//    }
//
//    private fun addGenreToDatabase(genreName: String, songs: Map<String, String>) {
//        val databaseReference = FirebaseDatabase.getInstance().reference.child("genres")
//        val genre = Genre(name = genreName, songs = songs)
//        databaseReference.child(genreName.toLowerCase()).setValue(genre)
//    }
//
//
//
//    private fun initializeGenreCardViews() {
//        val rockCardView = findViewById<CardView>(R.id.rockCardView)
//        val popCardView = findViewById<CardView>(R.id.popCardView)
//        val hipHopCardView = findViewById<CardView>(R.id.hipHopCardView)
//        val edmCardView = findViewById<CardView>(R.id.edmCardView)
//        val rbcardView = findViewById<CardView>(R.id.rbcardView)
//        val kpopCardView = findViewById<CardView>(R.id.kpopCardView)
//        val soulCardView = findViewById<CardView>(R.id.soulCardView)
//        val classicalCardView = findViewById<CardView>(R.id.classicalCardView)
//        val latinCardView = findViewById<CardView>(R.id.latinCardView)
//        val nasheedCardView = findViewById<CardView>(R.id.nasheedCardView)
//
//
//        rockCardView.setOnClickListener {
//            fetchGenreData("rock", rockCardView)
//
//            val rockSong = mapOf(
//                "song1" to "Song 1 Title",
//                "song2" to "Song 2 Ttile"
//            )
//            addGenreToDatabase("Rock",rockSong)
//        }
//        popCardView.setOnClickListener {
//            fetchGenreData("pop", popCardView)
//            val popSong = mapOf(
//                "song1" to "Song 1 Title",
//                "song2" to "Song 2 Ttile"
//            )
//            addGenreToDatabase("Pop",popSong)
//        }
//        hipHopCardView.setOnClickListener {
//            fetchGenreData("hipHop", hipHopCardView)
//            val hipHopSong = mapOf(
//                "song1" to "Song 1 Title",
//                "song2" to "Song 2 Ttile"
//            )
//            addGenreToDatabase("hipHop",hipHopSong)
//        }
//        edmCardView.setOnClickListener {
//            fetchGenreData("edm", edmCardView)
//            val edmSong = mapOf(
//                "song1" to "Song 1 Title",
//                "song2" to "Song 2 Ttile"
//            )
//            addGenreToDatabase("EDM",edmSong)
//        }
//        rbcardView.setOnClickListener {
//            fetchGenreData("rb", rbcardView)
//            val rbSong = mapOf(
//                "song1" to "Song 1 Title",
//                "song2" to "Song 2 Ttile"
//            )
//            addGenreToDatabase("R&B",rbSong)
//        }
//        kpopCardView.setOnClickListener {
//            fetchGenreData("kpop", kpopCardView)
//            val kpopSong = mapOf(
//                "song1" to "Song 1 Title",
//                "song2" to "Song 2 Ttile"
//            )
//            addGenreToDatabase("Kpop",kpopSong)
//        }
//        soulCardView.setOnClickListener {
//            fetchGenreData("soul",soulCardView)
//            val soulSong = mapOf(
//                "song1" to "Song 1 Title",
//                "song2" to "Song 2 Ttile"
//            )
//            addGenreToDatabase("Soul",soulSong)
//        }
//        classicalCardView.setOnClickListener {
//            fetchGenreData("classical", classicalCardView)
//            val classicalSong = mapOf(
//                "song1" to "Song 1 Title",
//                "song2" to "Song 2 Ttile"
//            )
//            addGenreToDatabase("Classical",classicalSong)
//        }
//        latinCardView.setOnClickListener {
//            fetchGenreData("latin", latinCardView)
//            val latinSong = mapOf(
//                "song1" to "Song 1 Title",
//                "url" to "Song 2 Ttile"
//            )
//            addGenreToDatabase("Latin",latinSong)
//        }
//        nasheedCardView.setOnClickListener {
//            fetchGenreData("nasheed", nasheedCardView)
//            val nasheedSong = mapOf(
//                "song1" to "Song 1 Title",
//                "song2" to "Song 2 Ttile"
//            )
//            addGenreToDatabase("Nasheed",nasheedSong)
//        }
//    }
//
//    private fun fetchGenreData(genreKey: String, cardView: CardView) {
//        val genreReference = databaseReference.child(genreKey)
//        genreReference.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val genre = snapshot.getValue(Genre::class.java)
//                if (genre != null) {
//                    // Assuming 'songs' in Genre class is a Map<String, Music>
//                    val songs = genre.songs as Map<String, Music>
//
//                    // Now, you can use 'songs' to update your data or pass it to HomeFragment
//                    updateMusicDataForGenre(songs)
//
//                    toggleGenreSelection(genre)
//                    updateCardViewSelection(cardView)
//                } else {
//                    Toast.makeText(
//                        this@GenresRecommendationsActivity,
//                        "Failed to fetch genre data.",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Toast.makeText(
//                    this@GenresRecommendationsActivity,
//                    "Database error: ${error.message}",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        })
//    }
//
//    private fun initializeFinishButton() {
//        val finishButton = findViewById<Button>(R.id.finishbutton)
//        finishButton.setOnClickListener {
//            showRecommendations()
//        }
//    }
//
//    private fun toggleGenreSelection(genre: Genre) {
//        if (selectedGenres.contains(genre)) {
//            selectedGenres.remove(genre)
//        } else {
//            selectedGenres.add(genre)
//        }
//    }
//
//    private fun updateCardViewSelection(cardView: CardView) {
//        cardView.isSelected = selectedGenres.map { it.name }.contains(getGenreFromCardView(cardView))
//        updateCardViewBackground(cardView)
//    }
//
//    private fun getGenreFromCardView(cardView: CardView): String {
//        return when (cardView.id) {
//            R.id.rockCardView -> "Rock"
//            R.id.popCardView -> "Pop"
//            R.id.hipHopCardView -> "HipHop"
//            R.id.edmCardView -> "EDM"
//            R.id.kpopCardView -> "Kpop"
//            R.id.soulCardView -> "Soul"
//            R.id.nasheedCardView -> "Nasheed"
//            R.id.latinCardView -> "Latin"
//            R.id.classicalCardView -> "Classical"
//            R.id.rbcardView -> "R&B"
//
//            else -> ""
//        }
//    }
//
//    private fun updateCardViewBackground(cardView: CardView) {
//        cardView.setBackgroundResource(
//            if (cardView.isSelected) R.drawable.cardview_bg else R.drawable.white_bt
//        )
//    }
//
//    private fun showRecommendations() {
//        if (selectedGenres.isNotEmpty()) {
//            val intent = Intent(this, MainActivity::class.java)
//            intent.putParcelableArrayListExtra("selectedGenres", ArrayList(selectedGenres))
//            startActivity(intent)
//            finish()
//        } else {
//            Toast.makeText(this, "Please select at least one genre.", Toast.LENGTH_SHORT).show()
//        }
//    }
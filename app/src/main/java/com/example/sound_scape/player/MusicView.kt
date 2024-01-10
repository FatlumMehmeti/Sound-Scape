package com.example.sound_scape.player

import android.content.Intent
import android.media.MediaPlayer
import android.media.MediaPlayer.OnPreparedListener
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.sound_scape.HomeFragment
import com.example.sound_scape.MainActivity
import com.example.sound_scape.Music
import com.example.sound_scape.MusicAdapter
import com.example.sound_scape.R
import com.google.android.material.slider.Slider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class MusicView(musicList : ArrayList<Music>) : Fragment(R.layout.music_player), MusicAdapter.OnItemClickListener {

    private lateinit var mediaPlayer: MediaPlayer
    var songList =  musicList
    private var currentSongIndex: Int = 0
    private var isShuffleEnabled: Boolean = false
    private var isRepeatEnabled: Boolean = false
    private lateinit var playPauseButton: ImageView
    private lateinit var songTitle: TextView
    private lateinit var totalTime: TextView
    private lateinit var currentTime: TextView
    private lateinit var seekBar: Slider
    private val handler = Handler()
    private var originalSongList: List<Music> = emptyList()
    lateinit var view2 : View

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view2 = view
        // Initialize mediaPlayer
        mediaPlayer = MediaPlayer()

        songTitle = view.findViewById(R.id.song_title)
        totalTime =  view.findViewById(R.id.total_time)
        seekBar =  view.findViewById(R.id.seek_bar)
        currentTime =  view.findViewById(R.id.current_time)
        val mainactivity = activity as MainActivity


        view.findViewById<ImageView>(R.id.back_button).setOnClickListener(){
            mainactivity.replaceFragment(HomeFragment())
        }


        val musicAdapter = MusicAdapter(requireContext(),mainactivity,songList)
        musicAdapter.setOnItemClickListener(this)
        val intent = Intent(requireContext(), BackgroundMusicService::class.java)
        mainactivity.startService(intent)

       // var originalSongList = songList.toList()
        playPauseButton = view.findViewById(R.id.pause_play)
        val nextButton: ImageView = view.findViewById(R.id.next)
        val previousButton: ImageView = view.findViewById(R.id.previous)
        val shuffleButton: ImageView = view.findViewById(R.id.shuffle)
        val repeatButton: ImageView = view.findViewById(R.id.repeat)

        playPauseButton.setOnClickListener {
            togglePlayback()
        }

        nextButton.setOnClickListener {
            playNextSong()
        }

        previousButton.setOnClickListener {
            playPreviousSong()
        }

        shuffleButton.setOnClickListener {
            // Implement shuffle functionality
            toggleShuffle()
        }

        repeatButton.setOnClickListener {
            // Implement repeat functionality
            toggleRepeat()
        }

        setupMediaPlayer()
        setupSeekBar()

        // Check if songList is not empty before attempting to play the first song
        if (songList.isNotEmpty()) {
            val firstSong = songList[0]
            playSong(firstSong)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        handler.removeCallbacksAndMessages(null)
    }

    private fun formatTime(duration: Int): String {
        val minutes = duration / 1000 / 60
        val seconds = duration / 1000 % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    private fun setupMediaPlayer() {
        mediaPlayer.setOnCompletionListener {
            if (isRepeatEnabled) {
                mediaPlayer.seekTo(0)
                mediaPlayer.start()
            } else {
                if (isShuffleEnabled) {
                    playRandomSong()
                } else {
//                    playNextSong()
                }

            }
        }
    }

    private fun setupSeekBar() {
        seekBar.addOnChangeListener { _, value, _ ->
            val progress = (mediaPlayer.duration * value / 100).toInt()
            mediaPlayer.seekTo(progress)
            currentTime.text = formatTime(progress)
        }

        handler.post(object : Runnable {
            override fun run() {
                if (mediaPlayer.isPlaying) {
                    val currentPosition = mediaPlayer.currentPosition
                    seekBar.value = (currentPosition.toFloat() / mediaPlayer.duration.toFloat()) * 100
                    currentTime.text = formatTime(currentPosition)
                }
                handler.postDelayed(this, 1000) // Update every second
            }
        })
    }

    private fun togglePlayback() {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause()
                    playPauseButton.setImageResource(R.drawable.play_icon)
                } else {
                    mediaPlayer.start()
                    playPauseButton.setImageResource(R.drawable.pause_button)
                }
    }

    private fun playNextSong() {
        Log.d("songListSie:" , songList.size.toString())
        Log.d("songListSie:" , currentSongIndex.toString())

        currentSongIndex++

        if(currentSongIndex==songList.size){
            currentSongIndex = 0
        }

        if(currentSongIndex<=songList.size-1) {
            val nextSong = songList[currentSongIndex]
            playSong(nextSong)
        }


    }

    private fun playPreviousSong() {
        currentSongIndex--
        if (currentSongIndex < 0) {
            currentSongIndex = songList.size - 1
        }
        val previousSong = songList[currentSongIndex]
        playSong(previousSong)
    }

    private fun playRandomSong() {
        val randomIndex = (0 until songList.size).random()
        val randomSong = songList[randomIndex]
        playSong(randomSong)
    }

    private fun playSong(selectedMusic: Music) {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop()
            mediaPlayer.reset()
        }

        songTitle.text = selectedMusic.songtitle

        try {
            mediaPlayer.setDataSource(requireContext(), Uri.parse(selectedMusic.audioUrl))
            mediaPlayer.prepare()
        }catch(e:Exception){

        }
        mediaPlayer.start()

        playPauseButton.setImageResource(R.drawable.pause_button)
        totalTime.text = formatTime(mediaPlayer.duration)
    }

    private fun toggleShuffle() {
        val shuffleButton: ImageView =  view2.findViewById(R.id.shuffle)
        isShuffleEnabled = !isShuffleEnabled // Toggle shuffle status

        if (isShuffleEnabled) {
            // Change the shuffle button icon to "enabled"
            shuffleButton.setImageResource(R.drawable.shuffle_on)

            // Shuffle the song list
            songList.shuffle()
        } else {
            // Change the shuffle button icon to "disabled"
            shuffleButton.setImageResource(R.drawable.shuffle_icon)

            // Revert to the original song list order
            songList = ArrayList(originalSongList)
        }
    }

    private fun toggleRepeat() {
        // Toggle repeat status and update UI accordingly
        val repeatButton: ImageView =  view2.findViewById(R.id.repeat)
        isRepeatEnabled = !isRepeatEnabled

        // Update UI based on repeat status
        if (isRepeatEnabled) {
            // Change the repeat button icon to "enabled"
            repeatButton.setImageResource(R.drawable.repeat_on)
        } else {
            // Change the repeat button icon to "disabled"
            repeatButton.setImageResource(R.drawable.repeat_icon)
        }
    }

    // Implement the onItemClick method from MusicAdapter.OnItemClickListener
    override fun onItemClick(position: Int, music: Music) {
        // Handle the logic for playing the selected music directly in this activity
        mediaPlayer.reset()
        mediaPlayer = MediaPlayer.create(requireContext(), Uri.parse(music.audioUrl))
        mediaPlayer.start()

        playPauseButton.setImageResource(R.drawable.pause_button)
        totalTime.text = formatTime(mediaPlayer.duration)    }
}

//
//import android.content.Intent
//import android.media.MediaPlayer
//import android.net.Uri
//import android.os.Bundle
//import android.os.Handler
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.appcompat.app.AppCompatActivity
//import com.example.sound_scape.Music
//import com.example.sound_scape.MusicAdapter
//import com.example.sound_scape.player.BackgroundMusicService
//import com.example.sound_scape.R
//import com.google.android.material.slider.Slider
//
//class MusicView : AppCompatActivity(),MusicAdapter.OnItemClickListener {
//
//    private lateinit var mediaPlayer: MediaPlayer
//    private lateinit var songList: ArrayList<Music>
//    private var currentSongIndex: Int = 0
//    private var isShuffleEnabled: Boolean = false
//    private var isRepeatEnabled: Boolean = false
//    private lateinit var playPauseButton: ImageView
//    private lateinit var songTitle: TextView
//    private lateinit var totalTime: TextView
//    private lateinit var currentTime: TextView
//    private lateinit var seekBar: Slider
//    private val handler = Handler()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.music_player)
//
//        // Initialize mediaPlayer
//        mediaPlayer = MediaPlayer()
//
//        songTitle = findViewById(R.id.song_title)
//        totalTime = findViewById(R.id.total_time)
//        seekBar = findViewById(R.id.seek_bar)
//        currentTime = findViewById(R.id.current_time)
//
//        val musicAdapter = MusicAdapter(songList)
//        musicAdapter.setOnItemClickListener(this)
//        val intent = Intent(this, BackgroundMusicService::class.java)
//        startService(intent)
//
//        var originalSongList = songList.toList()
//        playPauseButton = findViewById(R.id.pause_play)
//        val nextButton: ImageView = findViewById(R.id.next)
//        val previousButton: ImageView = findViewById(R.id.previous)
//        val shuffleButton: ImageView = findViewById(R.id.shuffle)
//        val repeatButton: ImageView = findViewById(R.id.repeat)
//
//        playPauseButton.setOnClickListener {
//            togglePlayback()
//        }
//
//        nextButton.setOnClickListener {
//            playNextSong()
//        }
//
//        previousButton.setOnClickListener {
//            playPreviousSong()
//        }
//
//        shuffleButton.setOnClickListener {
//            // Implement shuffle functionality
//            toggleShuffle()
//        }
//
//        repeatButton.setOnClickListener {
//            // Implement repeat functionality
//            toggleRepeat()
//        }
//
//        setupMediaPlayer()
//        setupSeekBar()
//
//        // Check if songList is not empty before attempting to play the first song
//        if (songList.isNotEmpty()) {
//            val firstSong = songList[0]
//            playSong(firstSong)
//        }}
//    private fun playSong(selectedMusic: Music) {
//        if (mediaPlayer.isPlaying) {
//            mediaPlayer.stop()
//            mediaPlayer.reset()
//        }
//
//        mediaPlayer.setDataSource(this, Uri.parse(selectedMusic.audioUrl))
//        mediaPlayer.prepare()
//        mediaPlayer.start()
//
//        playPauseButton.setImageResource(R.drawable.pause_button)
//        totalTime.text = formatTime(mediaPlayer.duration)
//    }
//
//    private fun toggleRepeat() {
//        // Toggle repeat status and update UI accordingly
//        val repeatButton: ImageView = findViewById(R.id.repeat)
//        isRepeatEnabled = !isRepeatEnabled
//
//        // Update UI based on repeat status
//        if (isRepeatEnabled) {
//            // Change the repeat button icon to "enabled"
//            repeatButton.setImageResource(R.drawable.repeat_on)
//        } else {
//            // Change the repeat button icon to "disabled"
//            repeatButton.setImageResource(R.drawable.repeat_icon)
//        }
//    }
//
//        private fun toggleShuffle() {
//        val shuffleButton: ImageView = findViewById(R.id.shuffle)
//        isShuffleEnabled = !isShuffleEnabled // Toggle shuffle status
//
//        if (isShuffleEnabled) {
//            // Change the shuffle button icon to "enabled"
//            shuffleButton.setImageResource(R.drawable.shuffle_on)
//
//            // Shuffle the song list
//        } else {
//            // Change the shuffle button icon to "disabled"
//            shuffleButton.setImageResource(R.drawable.shuffle_icon)
//
//            // Revert to the original song list order
//
//        }
//    }
//    override fun onDestroy() {
//        super.onDestroy()
//        mediaPlayer.release()
//        handler.removeCallbacksAndMessages(null)
//    }
//
//    private fun formatTime(duration: Int): String {
//        val minutes = duration / 1000 / 60
//        val seconds = duration / 1000 % 60
//        return String.format("%02d:%02d", minutes, seconds)
//    }
//
//    private fun setupMediaPlayer() {
//        mediaPlayer.setOnCompletionListener {
//            if (isRepeatEnabled) {
//                mediaPlayer.seekTo(0)
//                mediaPlayer.start()
//            } else {
//                if (isShuffleEnabled) {
//                    playRandomSong()
//                } else {
//                    playNextSong()
//                }
//            }
//        }
//    }
//
//
//
//    private fun setupSeekBar() {
//        seekBar.addOnChangeListener { _, value, _ ->
//            val progress = (mediaPlayer.duration * value / 100).toInt()
//            mediaPlayer.seekTo(progress)
//            currentTime.text = formatTime(progress)
//        }
//
//        handler.post(object : Runnable {
//            override fun run() {
//                if (mediaPlayer.isPlaying) {
//                    val currentPosition = mediaPlayer.currentPosition
//                    seekBar.value = (currentPosition.toFloat() / mediaPlayer.duration.toFloat()) * 100
//                    currentTime.text = formatTime(currentPosition)
//                }
//                handler.postDelayed(this, 1000) // Update every second
//            }
//        })
//    }
//
//
//
//
//    private fun togglePlayback() {
//        if (mediaPlayer.isPlaying) {
//            mediaPlayer.pause()
//            playPauseButton.setImageResource(R.drawable.play_icon)
//        } else {
//            mediaPlayer.start()
//            playPauseButton.setImageResource(R.drawable.pause_button)
//        }
//    }
//
//    private fun playNextSong() {
//        currentSongIndex++
//        if (currentSongIndex >= songList.size) {
//            currentSongIndex = 0
//        }
//        val nextSong = songList[currentSongIndex]
//        playSong(nextSong)
//    }
//
//    private fun playPreviousSong() {
//        currentSongIndex--
//        if (currentSongIndex < 0) {
//            currentSongIndex = songList.size - 1
//        }
//        val previousSong = songList[currentSongIndex]
//        playSong(previousSong)
//    }
//
//    private fun playRandomSong() {
//        val randomIndex = (0 until songList.size).random()
//        val randomSong = songList[randomIndex]
//        playSong(randomSong)
//    }
//
//    private fun playSong(selectedMusic: Music) {
//        mediaPlayer.reset()
//        mediaPlayer = MediaPlayer.create(this, Uri.parse(selectedMusic.audioUrl))
//        mediaPlayer.start()
//        playPauseButton.setImageResource(R.drawable.pause_button)
//        totalTime.text = formatTime(mediaPlayer.duration)
//    }
//
////    private fun playSong(song: Int) {
////        mediaPlayer.reset()
////        mediaPlayer = MediaPlayer.create(this, song)
////        mediaPlayer.start()
////        playPauseButton.setImageResource(R.drawable.pause_button)
////        totalTime.text = formatTime(mediaPlayer.duration)
////    }
//    override fun onItemClick(position: Int, music: Music) {
//        // Handle the logic for playing the selected music directly in this activity
//        mediaPlayer.reset()
//        mediaPlayer = MediaPlayer.create(this, Uri.parse(music.audioUrl))
//        mediaPlayer.start()
//
//        playPauseButton.setImageResource(R.drawable.pause_button)
//        totalTime.text = formatTime(mediaPlayer.duration)
//    }
//
//
//}
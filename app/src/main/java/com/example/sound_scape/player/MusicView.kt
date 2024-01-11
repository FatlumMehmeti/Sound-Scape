package com.example.sound_scape.player

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
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
import com.squareup.picasso.Picasso

class MusicView(musicList : ArrayList<Music>) : Fragment(R.layout.music_player), MusicAdapter.OnItemClickListener {

    private lateinit var mediaPlayer: MediaPlayer
    var songList =  musicList
    private var currentSongIndex: Int = 0
     var isPlaying: Boolean = false
    private var isShuffleEnabled: Boolean = false
    private var isRepeatEnabled: Boolean = false
    private lateinit var playPauseButton: ImageView
    private lateinit var songTitle: TextView
    private lateinit var imgIcon:ImageView
    private lateinit var totalTime: TextView
    private lateinit var currentTime: TextView
    private lateinit var seekBar: Slider
    private val handler = Handler()
    private var originalSongList: List<Music> = emptyList()
    lateinit var view2 : View

    companion object {
        private lateinit var mediaPlayer: MediaPlayer

        fun getMediaPlayerInstance(): MediaPlayer {
            if (!::mediaPlayer.isInitialized) {
                mediaPlayer = MediaPlayer()
            }
            return mediaPlayer
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view2 = view
        mediaPlayer = MediaPlayer()

        songTitle = view.findViewById(R.id.song_title)
        imgIcon=view.findViewById(R.id.music_icon_big)
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

        playPauseButton = view.findViewById(R.id.pause_play)
        val nextButton: ImageView = view.findViewById(R.id.next)
        val previousButton: ImageView = view.findViewById(R.id.previous)
        val shuffleButton: ImageView = view.findViewById(R.id.shuffle)
        val repeatButton: ImageView = view.findViewById(R.id.repeat)
        val likesong :ImageView = view.findViewById(R.id.like)

        playPauseButton.setOnClickListener {
            togglePlayback()
        }
        if (getMediaPlayerInstance().isPlaying) {
            playPauseButton.setImageResource(R.drawable.pause_button)
        } else {
            playPauseButton.setImageResource(R.drawable.play_icon)
        }
        nextButton.setOnClickListener {
            playNextSong()
        }
        likesong.setOnClickListener {
            if(songList[currentSongIndex].favorite==false){
                songList[currentSongIndex].favorite=true
                FirebaseDatabase.getInstance().getReference("Music").child(songList[currentSongIndex].songtitle).child("favorite").setValue(true)
              likesong.setImageResource(R.drawable.favorite)
            }else if(songList[currentSongIndex].favorite==true){
                songList[currentSongIndex].favorite=false
                FirebaseDatabase.getInstance().getReference("Music").child(songList[currentSongIndex].songtitle).child("favorite").setValue(false)
                likesong.setImageResource(R.drawable.favorite)
            }
        }
        previousButton.setOnClickListener {
            playPreviousSong()
        }

        shuffleButton.setOnClickListener {
            toggleShuffle()
        }

        repeatButton.setOnClickListener {
            toggleRepeat()
        }

        setupMediaPlayer()
        setupSeekBar()

        if (songList.isNotEmpty()) {
            val firstSong = songList[0]
            playSong(firstSong)
        }
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
                handler.postDelayed(this, 1000)
            }
        })


    }

    private fun togglePlayback() {
                if (mediaPlayer.isPlaying()) {
                    isPlaying=false
                    mediaPlayer.pause()
                    playPauseButton.setImageResource(R.drawable.play_icon)
                } else {
                    isPlaying=true
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
        val databaseReference = FirebaseDatabase.getInstance().getReference("Music").child(selectedMusic.songtitle)
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val currentSongPlayed = snapshot.child("songPlayed").getValue().toString().toInt()
                databaseReference.child("songPlayed").setValue(currentSongPlayed + 1)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        songTitle.text = selectedMusic.songtitle
        if (!selectedMusic.imageUrl.isNullOrBlank()) {
            val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(selectedMusic.imageUrl)

            storageReference.downloadUrl.addOnSuccessListener { uri ->
                Picasso.get()
                    .load(uri)
                    .fit()
                    .centerInside()
                    .placeholder(R.drawable.music_note_icon)
                    .into(imgIcon)
            }.addOnFailureListener {
                imgIcon.setImageResource(R.drawable.music_note_icon)
            }
        } else {
            imgIcon.setImageResource(R.drawable.music_note_icon)
        }

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
        isShuffleEnabled = !isShuffleEnabled

        if (isShuffleEnabled) {
            shuffleButton.setImageResource(R.drawable.shuffle_on)

            songList.shuffle()
        } else {
            shuffleButton.setImageResource(R.drawable.shuffle_icon)

            songList = ArrayList(originalSongList)
        }
    }

    private fun toggleRepeat() {
        val repeatButton: ImageView =  view2.findViewById(R.id.repeat)
        isRepeatEnabled = !isRepeatEnabled

        if (isRepeatEnabled) {
            repeatButton.setImageResource(R.drawable.repeat_on)
        } else {
            repeatButton.setImageResource(R.drawable.repeat_icon)
        }
    }

    override fun onItemClick(position: Int, music: Music) {
        mediaPlayer.reset()
        mediaPlayer = MediaPlayer.create(requireContext(), Uri.parse(music.audioUrl))
        mediaPlayer.start()

        playPauseButton.setImageResource(R.drawable.pause_button)
        totalTime.text = formatTime(mediaPlayer.duration)    }
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        handler.removeCallbacksAndMessages(null)
    }
}

package com.example.sound_scape.player

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log


class BackgroundMusicService : Service() {
    var mediaPlayer: MediaPlayer? = null
    override fun onCreate() {
        super.onCreate()
        Log.d("mylog", "On Create Service")
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d("mylog", "Starting playing")
        mediaPlayer!!.start()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun stopService(name: Intent): Boolean {
        return super.stopService(name)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer!!.stop()
        mediaPlayer!!.release()
        mediaPlayer = null
    }
}
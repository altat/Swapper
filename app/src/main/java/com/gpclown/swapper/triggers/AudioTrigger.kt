package com.gpclown.swapper.triggers

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import com.gpclown.swapper.ProfileList

class AudioTrigger : Trigger() {
    override var type: String = "audio"
    lateinit var audioManager: AudioManager
    lateinit var audioListener: AudioManager.OnAudioFocusChangeListener
    lateinit var focusRequest: AudioFocusRequest
    val handler = Handler(Looper.getMainLooper())
    val runnableCode = object : Runnable {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun run() {
            Log.d("Handlers", "Called on main thread")
            if (audioManager.isMusicActive) {
                handler.postDelayed(this, 5000)
            }
            else {
                audioManager.requestAudioFocus(focusRequest)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun activate(context: Context, id: Int) {
        active = true
        audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioListener = AudioManager.OnAudioFocusChangeListener {focusChange ->
            when (focusChange) {
                AudioManager.AUDIOFOCUS_GAIN -> {
                    Log.i("AUDIO", "Focus Gained")
                    pulled = false
                }

                else -> {
                    pulled = true
                    val profileList = ProfileList.getInstance()
                    val profile = profileList.get(id)
                    profile.activate(context)
                    Log.i("AUDIO", "Focus Lost")
                    handler.post(runnableCode)
                }
            }
        }

        focusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN).run {
            setAudioAttributes(AudioAttributes.Builder().run {
                setUsage(AudioAttributes.USAGE_GAME)
                setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                build()
            })
            setAcceptsDelayedFocusGain(true)
            setOnAudioFocusChangeListener(audioListener)
            build()
        }

        val res = audioManager.requestAudioFocus(focusRequest)
        if (res == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) Log.i("AUDIOMANAGER", "GRANTED AUDIO FOCUS")
        else Log.i("AUDIOMANAGER", "FAILED TO GET AUDIO FOCUS")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun deactivate() {
        audioManager.abandonAudioFocusRequest(focusRequest)
        pulled = false
    }

}
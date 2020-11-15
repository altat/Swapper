package com.gpclown.swapper.settings

import android.content.Context
import android.media.AudioManager
import com.gpclown.swapper.activities.VolumeActivity

class Volume : Setting() {
    override var name: String = "Volume"
    lateinit var audioManager : AudioManager

    override fun activate(context: Context) {
        if (selected) {
            audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, scale, 0)
        }
    }

    override fun getActivity(): Class<*> {
        return VolumeActivity::class.java
    }
}
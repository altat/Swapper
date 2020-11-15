package com.gpclown.swapper.activities

import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.gpclown.swapper.Profile
import com.gpclown.swapper.ProfileList
import com.gpclown.swapper.R
import com.gpclown.swapper.settings.Volume
import kotlinx.android.synthetic.main.activity_volume.*

class VolumeActivity : AppCompatActivity() {
    private val profileList : ProfileList = ProfileList.getInstance()
    var profile : Profile? = null
    var volume: Volume? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_volume)

        val extras = intent.extras
        if (extras != null) {
            profile = profileList.get(extras.getInt("pid"))
        }

        volume = profile?.getSetting(0) as Volume
        volumeSlider.setProgress(volume!!.scale)
        volumeLabel.setText("" + volume!!.scale)

        volumeSlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                volumeLabel.setText("" + p1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                //
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                volume!!.scale = p0!!.progress
                Log.i("VOLUME ACTIVITY", "Scale is " + p0.progress)
            }
        })
    }
}
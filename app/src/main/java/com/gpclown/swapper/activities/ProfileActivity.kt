package com.gpclown.swapper.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gpclown.swapper.Profile
import com.gpclown.swapper.ProfileList
import com.gpclown.swapper.R
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {
    var profile: Profile = Profile()
    var editMode: Boolean = false
    private val profileList: ProfileList = ProfileList.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val extras = intent.extras
        if (extras != null) {
            var position = extras.getInt("position")
            profile = profileList.get(position)
            editMode = extras.getBoolean("edit")
        }

        profileName.setText(profile.name)


        cancelProfileButton.setOnClickListener{
            if (!editMode) {
                profileList.remove(profile.id)
            }
            val intent = Intent(this, ProfileListActivity::class.java)
            startActivity(intent)
        }

        confirmProfileButton.setOnClickListener{
            profile.name = profileName.text.toString()
            addProfile()
            val intent = Intent(this, ProfileListActivity::class.java)
            startActivity(intent)
        }

        settingsButton.setOnClickListener {
            addProfile()
            val intent = Intent(this, SettingsActivity::class.java)
            intent.putExtra("profile", profile.id)
            startActivity(intent)
        }

        triggersButton.setOnClickListener {
            addProfile()
            val intent = Intent(this, TriggersActivity::class.java)
            intent.putExtra("profile", profile.id)
            startActivity(intent)
        }
    }

    private fun addProfile() {
        if (!profileList.contains(profile)) {
            profile.id = profileList.profiles.size
            profileList.add(profile)
        }
    }
}

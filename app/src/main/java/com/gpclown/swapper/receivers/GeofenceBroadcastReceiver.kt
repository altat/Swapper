package com.gpclown.swapper.receivers

import android.content.BroadcastReceiver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent
import com.gpclown.swapper.Profile
import com.gpclown.swapper.ProfileList
import com.gpclown.swapper.triggers.LocationTrigger

class GeofenceBroadcastReceiver : BroadcastReceiver() {
    private val profileList: ProfileList = ProfileList.getInstance()
    private val locationIndex: Int = 0
    private var profile: Profile? = null
    private var trigger: LocationTrigger? = null
    // ...
    override fun onReceive(context: Context?, intent: Intent?) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        val extras = intent?.extras

        if (extras != null) {
            var pid = extras.getInt("pid")
            profile = profileList.get(pid)
            trigger = profile?.getTrigger(locationIndex) as LocationTrigger
        }

        if (geofencingEvent.hasError()) {
            val errorMessage = GeofenceStatusCodes.getStatusCodeString(geofencingEvent.errorCode)
            Log.e(ContentValues.TAG, errorMessage)
            return
        }

        // Get the transition type.
        val geofenceTransition = geofencingEvent.geofenceTransition


        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            trigger?.pulled = true
            profile?.activate(context!!)

        }
        else if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            trigger?.pulled = false
            profile?.deactivate()
        } else {
            // Log the error.

        }
    }
}
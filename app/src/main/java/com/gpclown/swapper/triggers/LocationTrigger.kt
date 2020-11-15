package com.gpclown.swapper.triggers

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.gpclown.swapper.activities.LocationSelectActivity
import com.gpclown.swapper.receivers.GeofenceBroadcastReceiver

class LocationTrigger : Trigger() {
    override var type: String = "location"
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    var radius : Float = 10.0F
    var pid: Int = 0
    lateinit var pContext: Context
    lateinit var geofencingClient: GeofencingClient
    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(pContext, GeofenceBroadcastReceiver::class.java)
        intent.putExtra("pid", pid)
        PendingIntent.getBroadcast(pContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }
    var geofence: Geofence? = null

    private fun getGeofencingRequest(): GeofencingRequest {
        return GeofencingRequest.Builder().apply {
            setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            addGeofence(geofence)
        }.build()
    }

    override fun activate(context: Context, id: Int) {
        active = true
        pContext = context
        pid = id
        geofencingClient = LocationServices.getGeofencingClient(pContext)
        // create geofence
        geofence = Geofence.Builder().setRequestId("place")
            .setCircularRegion(latitude, longitude, radius)
            .setExpirationDuration(0)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
            .build()

        if (ActivityCompat.checkSelfPermission(
                pContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TO DO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        geofencingClient?.addGeofences(getGeofencingRequest(), geofencePendingIntent)?.run {
            addOnSuccessListener {
                // Geofences added
                // ...
            }
            addOnFailureListener {
                // Failed to add geofences
                // ...
            }
        }
        // add pulled to shared preference
    }

    override fun deactivate() {
        geofencingClient?.removeGeofences(geofencePendingIntent)?.run {
            addOnSuccessListener {
                // Geofences removed
                // ...
            }
            addOnFailureListener {
                // Failed to remove geofences
                // ...
            }
        }
        pulled = false
    }

    override fun getActivity(): Class<*> {
        return LocationSelectActivity::class.java
    }
}
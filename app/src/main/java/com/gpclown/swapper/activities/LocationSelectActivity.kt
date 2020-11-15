package com.gpclown.swapper.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.gpclown.swapper.Profile
import com.gpclown.swapper.ProfileList
import com.gpclown.swapper.R
import com.gpclown.swapper.triggers.LocationTrigger


class LocationSelectActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private val profileList : ProfileList = ProfileList.getInstance()
    var profile : Profile? = null
    var trigger : LocationTrigger? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_select)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val autocompleteFragment =
            supportFragmentManager.findFragmentById(R.id.place_autocomplete_fragment) as AutocompleteSupportFragment

        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME))

        val extras = intent.extras
        if (extras != null) {
            val pid = extras.getInt("pid")
            profile = profileList.get(pid)
        }

        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                mMap.clear()
                mMap.addMarker(MarkerOptions().position(place.latLng!!).title(place.name.toString()))
                mMap.moveCamera(CameraUpdateFactory.newLatLng(place.latLng))
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.latLng, 12.0f))
                trigger = profile?.getTrigger(0) as LocationTrigger
                trigger!!.latitude = place.latLng!!.latitude
                trigger!!.longitude = place.latLng!!.longitude
                trigger!!.deactivate()
                trigger!!.activate(applicationContext, profile!!.id)
            }

            override fun onError(status: Status) {}
        })
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        var loc : LatLng
        // Add a marker in Sydney and move the camera
        if (trigger != null) {
            loc = LatLng(trigger!!.longitude, trigger!!.latitude)
        } else {
            loc = LatLng(0.0, 0.0)
        }
        mMap.addMarker(MarkerOptions().position(loc).title("Place"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(loc))
    }
}
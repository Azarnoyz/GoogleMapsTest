package com.example.googlemapstest.model

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

data class Place(
    val lat: Double,
    val lng: Double,
    val markerTitle: String = "",
    val markerSnippet: String = ""
) : ClusterItem {

    private val position: LatLng = LatLng(lat, lng)
    private val title: String = markerTitle
    private val snippet: String = markerSnippet

    override fun getPosition(): LatLng {
        return position
    }

    override fun getTitle(): String {
        return title
    }

    override fun getSnippet(): String {
        return snippet
    }

}
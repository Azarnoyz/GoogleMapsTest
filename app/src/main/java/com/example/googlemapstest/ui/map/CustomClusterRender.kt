package com.example.googlemapstest.ui.map

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.example.googlemapstest.R
import com.example.googlemapstest.model.Place
import com.example.googlemapstest.utils.drawableToBitmap
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer


class CustomClusterRender(
    val context: Context,
    val map: GoogleMap,
    clusterManager: ClusterManager<Place>
) : DefaultClusterRenderer<Place>(context, map, clusterManager),
    ClusterManager.OnClusterClickListener<Place> {

    private var bitmapImageSmallest: Bitmap
    private var bitmapImageSmall: Bitmap
    private var bitmapImageMedium: Bitmap
    private var bitmapImageBig: Bitmap

    private var shouldBeCluster = true

    init {
        clusterManager.setOnClusterClickListener(this)
        val drawableImageSmallest: Drawable? =
            ContextCompat.getDrawable(context, R.drawable.cluster_size_1)
        val drawableImageSmall: Drawable? =
            ContextCompat.getDrawable(context, R.drawable.cluster_size_2)
        val drawableImageMedium: Drawable? =
            ContextCompat.getDrawable(context, R.drawable.cluster_size_3)
        val drawableImageBig: Drawable? =
            ContextCompat.getDrawable(context, R.drawable.cluster_size_4)
        bitmapImageSmallest = drawableToBitmap(drawableImageSmallest)
        bitmapImageSmall = drawableToBitmap(drawableImageSmall)
        bitmapImageMedium = drawableToBitmap(drawableImageMedium)
        bitmapImageBig = drawableToBitmap(drawableImageBig)
    }

    override fun shouldRenderAsCluster(cluster: Cluster<Place?>): Boolean {
        return shouldBeCluster
    }

    override fun onBeforeClusterRendered(cluster: Cluster<Place>, markerOptions: MarkerOptions) {

        when (cluster.items.size) {
            in 0..1 -> markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmapImageSmallest))
            in 2..20 -> markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmapImageSmall))
            in 21..30 -> markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmapImageMedium))
            in 50..100 -> markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmapImageBig))
            else -> markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmapImageSmallest))
        }
    }


    override fun onClusterUpdated(cluster: Cluster<Place>, marker: Marker) {}

    fun changeMarkerToCluster(shouldBeCluster: Boolean) {
        this.shouldBeCluster = shouldBeCluster
    }

    override fun onClusterClick(cluster: Cluster<Place>): Boolean {
        val builder = LatLngBounds.builder()
        for (item in cluster.items) {
            builder.include(item.position)
        }

        val bounds = builder.build()

        try {
            map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return true
    }
}
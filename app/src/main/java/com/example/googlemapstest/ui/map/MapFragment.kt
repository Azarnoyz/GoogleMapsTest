package com.example.googlemapstest.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.googlemapstest.R
import com.example.googlemapstest.databinding.FragmentMapBinding
import com.example.googlemapstest.model.Place
import com.example.googlemapstest.utils.Constants
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterManager
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MapFragment : Fragment(), OnMapReadyCallback {

    private val mapViewModel: MapViewModel by viewModels()

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!


    private lateinit var mMap: GoogleMap
    private lateinit var mClusterManager: ClusterManager<Place>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        initSpotsObserver()

    }

    private fun initSpotsObserver() {
        mapViewModel.hotSpotsResult.observe(requireActivity()) {
            hideLoaderAndSetAlpha()
            setUpClusterer(it)
        }
    }

    private fun hideLoaderAndSetAlpha() = with(binding) {
        mapLayout.alpha = 1f
        loader.visibility = View.GONE
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }

    private fun setUpClusterer(hotSpotsResult: List<Place>) {
        moveCameraToLondonForTest()
        val clusterRender = initClusterManagerAndRender(hotSpotsResult)
        changeClusterIfZoomIn(clusterRender)
    }

    private fun moveCameraToLondonForTest() {
        mMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    Constants.TEST_LAT,
                    Constants.TEST_LNG
                ), Constants.TEST_ZOOM_IN_VALUE
            )
        )
    }

    private fun initClusterManagerAndRender(hotSpotsResult: List<Place>): CustomClusterRender {
        mClusterManager = ClusterManager(requireContext(), mMap)
        mClusterManager.addItems(hotSpotsResult)
        val clusterRender = CustomClusterRender(requireContext(), mMap, mClusterManager)
        mClusterManager.renderer = clusterRender
        mMap.setOnCameraIdleListener(mClusterManager)
        return clusterRender
    }

    private fun changeClusterIfZoomIn(clusterRender: CustomClusterRender) {
        mMap.setOnCameraMoveListener {
            if (mMap.cameraPosition.zoom > Constants.MARKERS_ZOOM_IN_VALUE) {
                clusterRender.changeMarkerToCluster(false)
            } else {
                clusterRender.changeMarkerToCluster(true)
            }
        }
    }

}
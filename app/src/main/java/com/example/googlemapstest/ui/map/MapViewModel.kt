package com.example.googlemapstest.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.googlemapstest.model.Place
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(private val mapRepository: MapRepository) : ViewModel() {

    private var _hotSpotsResult = MutableLiveData<List<Place>>()
    val hotSpotsResult: LiveData<List<Place>> = _hotSpotsResult

    private val hotSpots: MutableList<Place> = mutableListOf()

    init {
        getAllHotSpots()
    }

    private fun getAllHotSpots() {
        val def = viewModelScope.async(Dispatchers.IO) {
            mapRepository.getHotSpots().collect {
                hotSpots.add(it)
            }

        }

        viewModelScope.launch {
            def.await()
            _hotSpotsResult.postValue(hotSpots)
        }

    }
}
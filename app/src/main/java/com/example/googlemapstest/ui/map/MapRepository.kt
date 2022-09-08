package com.example.googlemapstest.ui.map

import android.content.Context
import com.example.googlemapstest.model.Place
import com.example.googlemapstest.utils.Constants
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.io.InputStream
import javax.inject.Inject

class MapRepository @Inject constructor(appContext: Context) {

    private val context = appContext
    private var count = 0
    private lateinit var stream: InputStream

    suspend fun getHotSpots() = flow {
        runCatching {
            stream = context.assets.open("hotspots.csv")
        }
        csvReader().openAsync(stream) {
            readAllAsSequence().asFlow().collect { data ->
                val condition = data[0].isNotEmpty() && data[2].isNotEmpty() && data[3].isNotEmpty()
                ++count

                if (condition && count < Constants.TEST_HOT_SPOTS_COUNT) {
                    emit(Place(data[2].toDouble(), data[3].toDouble()))
                }
            }
        }
    }.catch { e ->
        e.printStackTrace()
    }
}
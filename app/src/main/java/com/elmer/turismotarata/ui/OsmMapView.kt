package com.elmer.turismotarata.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.elmer.turismotarata.model.Place
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Composable
fun OsmMapView(
    places: List<Place>,
    modifier: Modifier = Modifier,
    centerLat: Double = -17.4742,
    centerLon: Double = -70.0348,
    zoomLevel: Double = 14.0
) {
    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            MapView(ctx).apply {
                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)
                controller.setZoom(zoomLevel)
                controller.setCenter(GeoPoint(centerLat, centerLon))

                places.forEach { place ->
                    val coords = coordenadasPorLugar(place)
                    val marker = Marker(this)
                    marker.position = GeoPoint(coords.first, coords.second)
                    marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    marker.title = place.titulo
                    marker.snippet = "Cat. ${place.categoria_id} • ${place.ubicacion}"
                    overlays.add(marker)
                }
            }
        }
    )
}

private fun coordenadasPorLugar(place: Place): Pair<Double, Double> {
    return when {
        place.titulo.contains("Putina", ignoreCase = true) || place.id == "1" -> -17.4410 to -70.0480
        place.titulo.contains("Iglesia", ignoreCase = true) || place.id == "2" -> -17.4742 to -70.0348
        place.titulo.contains("Ancla", ignoreCase = true) || place.id == "3" -> -17.4750 to -70.0320
        else -> -17.4742 to -70.0348
    }
}
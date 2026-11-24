package com.elmer.turismotarata.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elmer.turismotarata.model.Place

@Composable
fun MapScreen(places: List<Place>) {
    Column(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
            Text(
                text = "Mapa Turístico de Tarata",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E1E1E)
            )
            Text(
                text = "Toca un marcador para ver el nombre del atractivo.",
                fontSize = 13.sp,
                color = Color.Gray
            )
        }
        OsmMapView(
            places = places,
            modifier = Modifier.fillMaxSize()
        )
    }
}
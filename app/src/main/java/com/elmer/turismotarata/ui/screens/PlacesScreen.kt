package com.elmer.turismotarata.ui.screens
import com.elmer.turismotarata.ui.components.PlaceCard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elmer.turismotarata.model.Place

@Composable
fun PlacesScreen(
    places: List<Place>,
    onPlaceClick: (Place) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        item {
            Text(
                text = "Todos los Destinos",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E1E1E)
            )
            Text(
                text = "Toca cualquier tarjeta para ver su información detallada.",
                fontSize = 13.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(4.dp))
        }

        items(places) { place ->
            PlaceCard(
                place = place,
                onClick = { onPlaceClick(place) }
            )
        }
    }
}
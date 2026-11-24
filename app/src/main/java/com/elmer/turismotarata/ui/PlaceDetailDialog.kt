package com.elmer.turismotarata.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elmer.turismotarata.model.Place

@Composable
fun PlaceDetailDialog(
    place: Place,
    onDismiss: () -> Unit
) {
    val naranja = Color(0xFFC85A28)
    val categoriaTexto = when (place.categoria_id) {
        "1" -> "TERMALISMO / NATURALEZA"
        "2" -> "HISTORIA / ARQUITECTURA"
        else -> "DESTINO TURÍSTICO"
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = place.titulo, fontWeight = FontWeight.Bold)
        },
        text = {
            Column {
                Text(
                    text = categoriaTexto,
                    color = naranja,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = place.descripcion)
                Spacer(modifier = Modifier.height(10.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = null,
                        tint = naranja,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = place.ubicacion,
                        fontSize = 13.sp,
                        color = Color.DarkGray
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar")
            }
        }
    )
}
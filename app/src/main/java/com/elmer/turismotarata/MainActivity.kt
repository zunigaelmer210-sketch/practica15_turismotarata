package com.elmer.turismotarata

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elmer.turismotarata.data.RetrofitClient
import com.elmer.turismotarata.model.Place
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                TurismoTarataApp()
            }
        }
    }
}

@Composable
fun TurismoTarataApp() {
    var selectedTab by remember { mutableIntStateOf(0) }
    var selectedPlace by remember { mutableStateOf<Place?>(null) }

    var places by remember { mutableStateOf<List<Place>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()

    fun cargarLugares() {
        scope.launch {
            isLoading = true
            errorMsg = null
            try {
                places = RetrofitClient.api.getPlaces()
            } catch (e: Exception) {
                errorMsg = "Error al conectar con MySQL/PHP: ${e.localizedMessage ?: "Revisa Wi-Fi o XAMPP"}"
            } finally {
                isLoading = false
            }
        }
    }

    LaunchedEffect(Unit) {
        cargarLugares()
    }

    val naranjaHeader = Color(0xFFC85A28)
    val fondoCrema = Color(0xFFF9F4EC)

    Scaffold(
        containerColor = fondoCrema,
        topBar = {
            HeaderTarata(naranjaHeader = naranjaHeader)
        },
        bottomBar = {
            NavigationBar(containerColor = fondoCrema) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Inicio") },
                    label = { Text("INICIO", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Filled.Explore, contentDescription = "Lugares") },
                    label = { Text("LUGARES", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) }
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Icon(Icons.Filled.Place, contentDescription = "Mapa") },
                    label = { Text("MAPA", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        color = naranjaHeader,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                errorMsg != null -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = errorMsg ?: "",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(onClick = { cargarLugares() }) {
                            Text("Reintentar")
                        }
                    }
                }

                else -> {
                    when (selectedTab) {
                        0 -> InicioContenido(
                            places = places,
                            onVerTodos = { selectedTab = 1 },
                            onSelectPlace = { selectedPlace = it }
                        )
                        1 -> LugaresContenido(
                            places = places,
                            onSelectPlace = { selectedPlace = it }
                        )
                        2 -> MapaContenido(
                            places = places,
                            onSelectPlace = { selectedPlace = it }
                        )
                    }
                }
            }
        }

        selectedPlace?.let { place ->
            AlertDialog(
                onDismissRequest = { selectedPlace = null },
                title = {
                    Text(text = place.titulo, fontWeight = FontWeight.Bold)
                },
                text = {
                    Column {
                        Text(
                            text = nombreCategoria(place.categoria_id),
                            color = naranjaHeader,
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
                                tint = naranjaHeader,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = place.ubicacion, fontSize = 13.sp, color = Color.DarkGray)
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { selectedPlace = null }) {
                        Text("Cerrar")
                    }
                }
            )
        }
    }
}

@Composable
private fun HeaderTarata(naranjaHeader: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(naranjaHeader)
            .padding(horizontal = 20.dp, vertical = 18.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Tarata, Tacna (3,070 msnm)",
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 12.sp
            )
            Text(
                text = "Turismo Tarata",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Tierra del Choclo y la Historia",
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 13.sp
            )
        }

        Surface(
            shape = CircleShape,
            color = Color.White.copy(alpha = 0.2f),
            modifier = Modifier.size(44.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Filled.Bookmark,
                    contentDescription = "Guardados",
                    tint = Color(0xFFFFD54F)
                )
            }
        }
    }
}

@Composable
private fun InicioContenido(
    places: List<Place>,
    onVerTodos: () -> Unit,
    onSelectPlace: (Place) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF2B542C)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFE0A800)
                    ) {
                        Text(
                            text = "★ DESTINO RECOMENDADO",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Explora la Maravilla Andina de Tacna",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Andenería pre-inca y baños termales medicinales.",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 13.sp
                    )
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Esencia Tarateña",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Ver todos >",
                    color = Color(0xFFC85A28),
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { onVerTodos() }
                )
            }
        }

        items(places, key = { it.id }) { place ->
            PlaceTarjeta(place = place, onClick = { onSelectPlace(place) })
        }
    }
}

@Composable
private fun LugaresContenido(
    places: List<Place>,
    onSelectPlace: (Place) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Todos los Destinos",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Toca cualquier tarjeta para ver su información detallada.",
                fontSize = 13.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(6.dp))
        }

        items(places, key = { it.id }) { place ->
            PlaceTarjeta(place = place, onClick = { onSelectPlace(place) })
        }
    }
}

@Composable
private fun MapaContenido(
    places: List<Place>,
    onSelectPlace: (Place) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Ubicaciones en Tarata",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Destinos sincronizados desde la base de datos MySQL.",
                fontSize = 13.sp,
                color = Color.Gray
            )
        }

        items(places, key = { it.id }) { place ->
            PlaceTarjeta(place = place, onClick = { onSelectPlace(place) })
        }
    }
}

@Composable
private fun PlaceTarjeta(
    place: Place,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = nombreCategoria(place.categoria_id),
                color = Color(0xFFC85A28),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = place.titulo,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E1E1E)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.LocationOn,
                    contentDescription = null,
                    tint = Color(0xFFC85A28),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = place.ubicacion,
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

private fun nombreCategoria(categoriaId: String): String {
    return when (categoriaId) {
        "1" -> "TERMALISMO / NATURALEZA"
        "2" -> "HISTORIA / ARQUITECTURA"
        else -> "DESTINO TURÍSTICO"
    }
}
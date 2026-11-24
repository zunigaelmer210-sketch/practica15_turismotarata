package com.elmer.turismotarata.data

import com.elmer.turismotarata.model.Place

object LocalDataSource {
    val places: List<Place> = listOf(
        Place(
            id = "1",
            titulo = "Baños Termales de Putina",
            descripcion = "Aguas termo-mineros medicinales ideales para la relajación, ubicadas cerca al distrito de Ticaco.",
            ubicacion = "Ticaco, Tarata",
            imagen = "putina.jpg",
            categoria_id = "1"
        ),
        Place(
            id = "2",
            titulo = "Iglesia San Benedicto",
            descripcion = "Templo colonial de arquitectura andina construido en piedra tallada, ícono histórico de Tarata.",
            ubicacion = "Plaza Principal, Tarata",
            imagen = "iglesia.jpg",
            categoria_id = "2"
        ),
        Place(
            id = "3",
            titulo = "La Piedra del Ancla",
            descripcion = "Motivo de curiosidad geológica y paisajística, una formación imponente que guarda mitos locales.",
            ubicacion = "Anexo de Tala, Tarata",
            imagen = "ancla.jpg",
            categoria_id = "1"
        )
    )
}
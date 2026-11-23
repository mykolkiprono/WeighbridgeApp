package com.example.weighbridgeapp.data.model

data class CollectionEntry(
    val id: Long,
    val route: String,
    val trip: Int?,       // null = normal vehicle
    val kilos: Double
)
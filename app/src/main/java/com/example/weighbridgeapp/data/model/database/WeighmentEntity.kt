package com.example.weighbridgeapp.data.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weighments")
data class WeighmentEntity(

    @PrimaryKey
    val id: Long,

    val sessionId: Long,

    val route: String,

    val trip: Int?,

    val kilos: Double,

    val date: String,

    val timestamp: Long
)
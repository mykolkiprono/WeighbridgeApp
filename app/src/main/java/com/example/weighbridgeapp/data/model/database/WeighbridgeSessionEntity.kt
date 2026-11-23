package com.example.weighbridgeapp.data.model.database


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sessions")
data class WeighbridgeSessionEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val date: String,

    val totalVehicles: Int,

    val startedAt: Long,

    val closedAt: Long? = null,

    val status: String = "OPEN"
)
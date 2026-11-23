package com.example.weighbridgeapp.data.model.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        WeighmentEntity::class,
        WeighbridgeSessionEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun weighmentDao(): WeighmentDao

    abstract fun sessionDao(): SessionDao
}
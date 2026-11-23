package com.example.weighbridgeapp.data.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object DatabaseProvider {

    @Volatile
    private var INSTANCE: AppDatabase? = null

    private val MIGRATION_1_2 = object : Migration(1, 2) {

        override fun migrate(
            database: SupportSQLiteDatabase
        ) {

            // Create the new sessions table
            database.execSQL(
                """
                CREATE TABLE IF NOT EXISTS sessions (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    date TEXT NOT NULL,
                    totalVehicles INTEGER NOT NULL,
                    startedAt INTEGER NOT NULL,
                    closedAt INTEGER,
                    status TEXT NOT NULL
                )
                """.trimIndent()
            )

            // Add sessionId to existing weighments
            database.execSQL(
                """
                ALTER TABLE weighments
                ADD COLUMN sessionId INTEGER NOT NULL DEFAULT 0
                """.trimIndent()
            )
        }
    }

    fun getDatabase(
        context: Context
    ): AppDatabase {

        return INSTANCE ?: synchronized(this) {

            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "weighbridge_database"
            )
                .addMigrations(MIGRATION_1_2)
                .build()
                .also {
                    INSTANCE = it
                }
        }
    }
}
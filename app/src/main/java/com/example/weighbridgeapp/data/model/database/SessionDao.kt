package com.example.weighbridgeapp.data.model.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {

    @Insert
    suspend fun insert(
        session: WeighbridgeSessionEntity
    ): Long

    @Update
    suspend fun update(
        session: WeighbridgeSessionEntity
    )

    @Query("""
        SELECT * FROM sessions
        WHERE status = 'OPEN'
        ORDER BY startedAt DESC
        LIMIT 1
    """)
    fun getActiveSession(): Flow<WeighbridgeSessionEntity?>

    @Query("""
        SELECT * FROM sessions
        ORDER BY startedAt DESC
    """)
    fun getAllSessions(): Flow<List<WeighbridgeSessionEntity>>

    @Query("""
        SELECT * FROM sessions
        WHERE id = :id
        LIMIT 1
    """)
    suspend fun getSessionById(
        id: Long
    ): WeighbridgeSessionEntity?

    @Query("""
        UPDATE sessions
        SET status = 'CLOSED',
            closedAt = :closedAt
        WHERE id = :sessionId
    """)
    suspend fun closeSession(
        sessionId: Long,
        closedAt: Long
    )

    @Query("""
    UPDATE sessions
    SET totalVehicles = :totalVehicles
    WHERE id = :sessionId
""")
    suspend fun updateTotalVehicles(
        sessionId: Long,
        totalVehicles: Int
    )
}
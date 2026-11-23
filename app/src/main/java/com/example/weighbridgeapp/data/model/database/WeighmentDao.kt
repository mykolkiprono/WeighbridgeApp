package com.example.weighbridgeapp.data.model.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface WeighmentDao {

    @Insert
    suspend fun insert(weighment: WeighmentEntity)

    @Update
    suspend fun update(weighment: WeighmentEntity)

    @Delete
    suspend fun delete(weighment: WeighmentEntity)

    @Query("""
        SELECT * FROM weighments
        WHERE sessionId = :sessionId
        ORDER BY timestamp ASC
    """)
    fun getWeighmentsBySession(
        sessionId: Long
    ): Flow<List<WeighmentEntity>>

    @Query("""
        SELECT * FROM weighments
        ORDER BY timestamp DESC
    """)
    fun getAllWeighments(): Flow<List<WeighmentEntity>>

    @Query("""
        SELECT * FROM weighments
        WHERE date = :date
        ORDER BY timestamp DESC
    """)
    fun getWeighmentsByDate(
        date: String
    ): Flow<List<WeighmentEntity>>

    @Query("""
        SELECT * FROM weighments
        WHERE route = :route
        ORDER BY timestamp DESC
    """)
    fun getWeighmentsByRoute(
        route: String
    ): Flow<List<WeighmentEntity>>

    @Query("""
        DELETE FROM weighments
        WHERE id = :id
    """)
    suspend fun deleteById(id: Long)

    @Query("""
        DELETE FROM weighments
    """)
    suspend fun deleteAll()
}
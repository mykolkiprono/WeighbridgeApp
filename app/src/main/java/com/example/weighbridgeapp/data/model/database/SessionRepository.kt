package com.example.weighbridgeapp.data.model.database

import kotlinx.coroutines.flow.Flow

class SessionRepository(
    private val dao: SessionDao
) {

    suspend fun insert(
        session: WeighbridgeSessionEntity
    ): Long {
        return dao.insert(session)
    }

    suspend fun update(
        session: WeighbridgeSessionEntity
    ) {
        dao.update(session)
    }

    fun getActiveSession(): Flow<WeighbridgeSessionEntity?> {
        return dao.getActiveSession()
    }

    fun getAllSessions(): Flow<List<WeighbridgeSessionEntity>> {
        return dao.getAllSessions()
    }

    suspend fun getSessionById(
        id: Long
    ): WeighbridgeSessionEntity? {
        return dao.getSessionById(id)
    }

    suspend fun closeSession(
        sessionId: Long
    ) {
        dao.closeSession(
            sessionId = sessionId,
            closedAt = System.currentTimeMillis()
        )
    }

    suspend fun updateTotalVehicles(
        sessionId: Long,
        totalVehicles: Int
    ) {
        dao.updateTotalVehicles(
            sessionId = sessionId,
            totalVehicles = totalVehicles
        )
    }
}
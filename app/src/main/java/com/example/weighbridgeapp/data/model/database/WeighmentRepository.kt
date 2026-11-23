package com.example.weighbridgeapp.data.model.database

import kotlinx.coroutines.flow.Flow

class WeighmentRepository(
    private val dao: WeighmentDao
) {

    suspend fun insert(
        weighment: WeighmentEntity
    ) {
        dao.insert(weighment)
    }

    suspend fun update(
        weighment: WeighmentEntity
    ) {
        dao.update(weighment)
    }

    suspend fun delete(
        weighment: WeighmentEntity
    ) {
        dao.delete(weighment)
    }

    suspend fun deleteById(
        id: Long
    ) {
        dao.deleteById(id)
    }

    fun getAllWeighments(): Flow<List<WeighmentEntity>> {
        return dao.getAllWeighments()
    }

    fun getWeighmentsBySession(
        sessionId: Long
    ): Flow<List<WeighmentEntity>> {
        return dao.getWeighmentsBySession(sessionId)
    }

    fun getWeighmentsByDate(
        date: String
    ): Flow<List<WeighmentEntity>> {
        return dao.getWeighmentsByDate(date)
    }

    fun getWeighmentsByRoute(
        route: String
    ): Flow<List<WeighmentEntity>> {
        return dao.getWeighmentsByRoute(route)
    }

    suspend fun deleteAll() {
        dao.deleteAll()
    }
}
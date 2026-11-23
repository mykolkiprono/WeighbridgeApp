package com.example.weighbridgeapp.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weighbridgeapp.data.model.database.SessionRepository
import com.example.weighbridgeapp.data.model.database.WeighbridgeSessionEntity
import com.example.weighbridgeapp.data.model.database.WeighmentEntity
import com.example.weighbridgeapp.data.model.database.WeighmentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WeighbridgeViewModel(
    private val weighmentRepository: WeighmentRepository,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    // =====================================================
    // ACTIVE SESSION
    // =====================================================

    val activeSession: StateFlow<WeighbridgeSessionEntity?> =
        sessionRepository
            .getActiveSession()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = null
            )

    // =====================================================
    // CURRENT SESSION ID
    // =====================================================

    private val currentSessionId =
        MutableStateFlow<Long?>(null)

    // =====================================================
    // CURRENT SESSION WEIGHMENTS
    // =====================================================

    val weighments: StateFlow<List<WeighmentEntity>> =
        currentSessionId
            .flatMapLatest { sessionId ->

                if (sessionId == null) {
                    flowOf(emptyList())
                } else {
                    weighmentRepository
                        .getWeighmentsBySession(sessionId)
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    // =====================================================
    // SESSION HISTORY
    // =====================================================

    val sessionHistory:
            StateFlow<List<WeighbridgeSessionEntity>> =
        sessionRepository
            .getAllSessions()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    // =====================================================
    // INITIALIZATION
    // =====================================================

    init {

        viewModelScope.launch {

            activeSession.collect { session ->

                currentSessionId.value =
                    session?.id
            }
        }
    }

    // =====================================================
    // START SESSION
    // =====================================================

    fun startSession(
        totalVehicles: Int
    ) {

        if (totalVehicles <= 0) {
            return
        }

        viewModelScope.launch {

            // Do not create another session
            // if one is already active.
            if (activeSession.value != null) {
                return@launch
            }

            val today =
                SimpleDateFormat(
                    "yyyy-MM-dd",
                    Locale.getDefault()
                ).format(Date())

            val session =
                WeighbridgeSessionEntity(

                    date = today,

                    totalVehicles =
                        totalVehicles,

                    startedAt =
                        System.currentTimeMillis(),

                    closedAt = null,

                    status = "OPEN"
                )

            sessionRepository.insert(session)
        }
    }

    // =====================================================
    // UPDATE VEHICLE TARGET
    // =====================================================

    fun updateSessionVehicleTarget(
        newTarget: Int
    ) {

        if (newTarget <= 0) {
            return
        }

        viewModelScope.launch {

            val currentSession =
                activeSession.value
                    ?: return@launch

            val updatedSession =
                currentSession.copy(
                    totalVehicles = newTarget
                )

            sessionRepository.update(
                updatedSession
            )
        }
    }

    // =====================================================
    // ADD WEIGHMENT
    // =====================================================

    fun addWeighment(
        weighment: WeighmentEntity
    ) {

        val session =
            activeSession.value
                ?: return

        viewModelScope.launch {

            val correctedWeighment =
                weighment.copy(
                    sessionId = session.id
                )

            weighmentRepository.insert(
                correctedWeighment
            )
        }
    }

    // =====================================================
    // DELETE WEIGHMENT
    // =====================================================

    fun deleteWeighment(
        id: Long
    ) {

        viewModelScope.launch {

            weighmentRepository.deleteById(
                id
            )
        }
    }

    // =====================================================
    // CLOSE SESSION
    // =====================================================

    fun closeSession() {

        val session =
            activeSession.value
                ?: return

        viewModelScope.launch {

            sessionRepository.closeSession(
                session.id
            )
        }
    }
}
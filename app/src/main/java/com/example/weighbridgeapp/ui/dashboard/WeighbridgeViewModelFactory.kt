package com.example.weighbridgeapp.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weighbridgeapp.data.model.database.SessionRepository
import com.example.weighbridgeapp.data.model.database.WeighmentRepository
import com.example.weighbridgeapp.ui.WeighbridgeViewModel

class WeighbridgeViewModelFactory(
    private val weighmentRepository: WeighmentRepository,
    private val sessionRepository: SessionRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(
                WeighbridgeViewModel::class.java
            )
        ) {

            return WeighbridgeViewModel(
                weighmentRepository = weighmentRepository,
                sessionRepository = sessionRepository
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}
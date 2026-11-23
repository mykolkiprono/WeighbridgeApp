package com.example.weighbridgeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weighbridgeapp.data.model.database.DatabaseProvider
import com.example.weighbridgeapp.data.model.database.SessionRepository
import com.example.weighbridgeapp.data.model.database.WeighmentRepository
import com.example.weighbridgeapp.ui.WeighbridgeViewModel
import com.example.weighbridgeapp.ui.WeighbridgeViewModelFactory
import com.example.weighbridgeapp.ui.dashboard.DashboardScreen
import com.example.weighbridgeapp.ui.theme.WeighbridgeAppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        val database =
            DatabaseProvider.getDatabase(
                applicationContext
            )

        val weighmentRepository =
            WeighmentRepository(
                database.weighmentDao()
            )

        val sessionRepository =
            SessionRepository(
                database.sessionDao()
            )

        setContent {

            WeighbridgeAppTheme {

                val viewModel:
                        WeighbridgeViewModel =
                    viewModel(
                        factory =
                            WeighbridgeViewModelFactory(
                                weighmentRepository =
                                    weighmentRepository,

                                sessionRepository =
                                    sessionRepository
                            )
                    )

                DashboardScreen(
                    viewModel = viewModel
                )
            }
        }
    }
}
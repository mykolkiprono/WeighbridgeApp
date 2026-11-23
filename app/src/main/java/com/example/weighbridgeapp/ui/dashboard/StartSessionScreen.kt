package com.example.weighbridgeapp.ui.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun StartSessionScreen(
    onStart: (Int) -> Unit
) {

    var totalVehiclesText by remember {
        mutableStateOf("")
    }

    val totalVehicles =
        totalVehiclesText.toIntOrNull()
            ?: 0

    Column(

        modifier =
            Modifier
                .fillMaxSize()
                .padding(
                    PaddingValues(24.dp)
                ),

        verticalArrangement =
            Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Start Collection",
            style =
                MaterialTheme
                    .typography
                    .headlineMedium,

            fontWeight =
                FontWeight.Bold
        )

        Text(
            text =
                "Enter the total number of vehicles expected for today's collection."
        )

        OutlinedTextField(

            value =
                totalVehiclesText,

            onValueChange = {

                totalVehiclesText =
                    it.filter { character ->
                        character.isDigit()
                    }
            },

            label = {
                Text("Total Vehicles")
            },

            keyboardOptions =
                KeyboardOptions(
                    keyboardType =
                        KeyboardType.Number
                ),

            singleLine = true,

            modifier =
                Modifier.fillMaxWidth()
        )

        Button(

            onClick = {
                onStart(totalVehicles)
            },

            enabled =
                totalVehicles > 0,

            modifier =
                Modifier.fillMaxWidth()
        ) {

            Text(
                text = "START SESSION",
                fontWeight =
                    FontWeight.Bold
            )
        }
    }
}
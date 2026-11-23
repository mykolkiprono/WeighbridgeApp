package com.example.weighbridgeapp.ui.dashboard

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

import com.example.weighbridgeapp.data.model.CollectionEntry
import com.example.weighbridgeapp.data.model.WeighbridgeData
import com.example.weighbridgeapp.data.model.database.WeighmentEntity
import com.example.weighbridgeapp.ui.WeighbridgeViewModel

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// =============================================================
// DASHBOARD
// =============================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: WeighbridgeViewModel
) {

    val context = LocalContext.current
    val routes = WeighbridgeData.routes

    // =========================================================
    // ACTIVE SESSION
    // =========================================================

    val activeSession by viewModel.activeSession.collectAsState()

    val session = activeSession

    /*
     * No active session.
     * Show ONLY the start-session screen.
     */
    if (session == null) {

        SessionStartScreen(
            onStart = { totalVehicles ->
                viewModel.startSession(totalVehicles)
            }
        )


        return
    }

    // =========================================================
    // DATABASE WEIGHMENTS
    // =========================================================

    val weighments by viewModel.weighments.collectAsState()

    /*
     * Convert database entities into dashboard entries.
     */
    val entries: List<CollectionEntry> =
        weighments.map { weighment ->

            CollectionEntry(
                id = weighment.id,
                route = weighment.route,
                trip = weighment.trip,
                kilos = weighment.kilos
            )
        }

    // =========================================================
    // FORM STATE
    // =========================================================

    var selectedRoute by remember {
        mutableStateOf("")
    }

    var selectedTrip by remember {
        mutableStateOf<Int?>(null)
    }

    var kilosText by remember {
        mutableStateOf("")
    }

    var routeExpanded by remember {
        mutableStateOf(false)
    }

    var tripExpanded by remember {
        mutableStateOf(false)
    }

    // =========================================================
    // UI STATE
    // =========================================================

    var reportCopied by remember {
        mutableStateOf(false)
    }

    var showCloseDialog by remember {
        mutableStateOf(false)
    }

    var showEditVehicleDialog by remember {
        mutableStateOf(false)
    }

    var editVehicleText by remember {
        mutableStateOf("")
    }

    // =========================================================
    // SESSION VEHICLE TARGET
    // =========================================================

    val totalVehicles =
        session.totalVehicles.coerceAtLeast(0)

    // =========================================================
    // VEHICLE COUNTING
    // =========================================================

    val vehicleStates =
        calculateVehicleStates(entries)

    val vehiclesIn =
        vehicleStates.count {
            it.inSystem
        }

    val completedVehicles =
        vehicleStates.count {
            it.completed
        }

    val pendingVehicles =
        (totalVehicles - completedVehicles)
            .coerceAtLeast(0)

    // =========================================================
    // TOTAL KILOS
    // =========================================================

    val totalKilos =
        entries.sumOf {
            it.kilos
        }

    val allVehiclesIn =
        totalVehicles > 0 &&
                pendingVehicles == 0

    // =========================================================
    // DATE
    // =========================================================

    val currentDate =
        SimpleDateFormat(
            "dd MMMM yyyy",
            Locale.getDefault()
        ).format(Date())

    // =========================================================
    // SCREEN
    // =========================================================

    Scaffold(

        modifier =
            Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .imePadding(),

        topBar = {

            TopAppBar(

                title = {

                    Column {

                        Text(
                            text = "Weighbridge Collection",
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = currentDate,
                            style =
                                MaterialTheme
                                    .typography
                                    .labelMedium
                        )
                    }
                }
            )
        }

    ) { innerPadding ->

        LazyColumn(

            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),

            contentPadding =
                PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 12.dp,
                    bottom = 32.dp
                ),

            verticalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {

            // =================================================
            // SESSION
            // =================================================

            item {

                Card(

                    modifier =
                        Modifier.fillMaxWidth(),

                    shape =
                        RoundedCornerShape(14.dp),

                    elevation =
                        CardDefaults.cardElevation(
                            defaultElevation = 2.dp
                        )
                ) {

                    Column(
                        modifier =
                            Modifier.padding(16.dp)
                    ) {

                        Text(
                            text = "ACTIVE SESSION",

                            style =
                                MaterialTheme
                                    .typography
                                    .labelMedium,

                            fontWeight =
                                FontWeight.Bold
                        )

                        Spacer(
                            modifier =
                                Modifier.height(4.dp)
                        )

                        Text(
                            text =
                                "Vehicle Target: $totalVehicles",

                            style =
                                MaterialTheme
                                    .typography
                                    .titleMedium,

                            fontWeight =
                                FontWeight.Bold
                        )

                        Spacer(
                            modifier =
                                Modifier.height(4.dp)
                        )

                        Text(
                            text =
                                "Completed: $completedVehicles"
                        )
                    }
                }
            }

            // =================================================
            // SUMMARY TITLE
            // =================================================

            item {

                Text(
                    text = "Collection Summary",

                    style =
                        MaterialTheme
                            .typography
                            .titleLarge,

                    fontWeight =
                        FontWeight.Bold
                )
            }

            // =================================================
            // SUMMARY CARDS
            // =================================================

            item {

                Row(

                    modifier =
                        Modifier.fillMaxWidth(),

                    horizontalArrangement =
                        Arrangement.spacedBy(8.dp)
                ) {

                    SummaryCard(
                        title = "TOTAL",
                        value =
                            totalVehicles.toString(),
                        modifier =
                            Modifier.weight(1f)
                    )

                    SummaryCard(
                        title = "IN",
                        value =
                            vehiclesIn.toString(),
                        modifier =
                            Modifier.weight(1f)
                    )

                    SummaryCard(
                        title = "PENDING",
                        value =
                            pendingVehicles.toString(),
                        modifier =
                            Modifier.weight(1f)
                    )
                }
            }

            // =================================================
            // TOTAL KILOS
            // =================================================

            item {

                Card(

                    modifier =
                        Modifier.fillMaxWidth(),

                    shape =
                        RoundedCornerShape(14.dp),

                    elevation =
                        CardDefaults.cardElevation(
                            defaultElevation = 2.dp
                        )
                ) {

                    Column(
                        modifier =
                            Modifier.padding(16.dp)
                    ) {

                        Text(
                            text = "TOTAL KILOS",

                            style =
                                MaterialTheme
                                    .typography
                                    .labelMedium,

                            fontWeight =
                                FontWeight.Bold
                        )

                        Spacer(
                            modifier =
                                Modifier.height(4.dp)
                        )

                        Text(
                            text =
                                "${formatKilos(totalKilos)} KG",

                            style =
                                MaterialTheme
                                    .typography
                                    .headlineMedium,

                            fontWeight =
                                FontWeight.Bold
                        )
                    }
                }
            }

            // =================================================
            // ALL VEHICLES IN
            // =================================================

            if (allVehiclesIn) {

                item {

                    Card(

                        modifier =
                            Modifier.fillMaxWidth(),

                        shape =
                            RoundedCornerShape(14.dp)
                    ) {

                        Column(
                            modifier =
                                Modifier.padding(16.dp)
                        ) {

                            Text(
                                text =
                                    "ALL VEHICLES IN",

                                style =
                                    MaterialTheme
                                        .typography
                                        .titleLarge,

                                fontWeight =
                                    FontWeight.Bold
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(4.dp)
                            )

                            Text(
                                text =
                                    "All $totalVehicles vehicles have completed the session."
                            )
                        }
                    }
                }
            }

            // =================================================
            // REPORT TITLE
            // =================================================

            item {

                Text(
                    text = "Current Report",

                    style =
                        MaterialTheme
                            .typography
                            .titleLarge,

                    fontWeight =
                        FontWeight.Bold
                )
            }

            // =================================================
            // REPORT
            // =================================================

            item {

                Card(

                    modifier =
                        Modifier.fillMaxWidth(),

                    shape =
                        RoundedCornerShape(14.dp)
                ) {

                    Column(
                        modifier =
                            Modifier.padding(16.dp)
                    ) {

                        if (entries.isEmpty()) {

                            Text(
                                text =
                                    "No vehicles recorded yet."
                            )

                        } else {

                            entries
                                .sortedBy {
                                    it.id
                                }
                                .forEach { entry ->

                                    ReportEntry(
                                        entry = entry
                                    )
                                }

                            Spacer(
                                modifier =
                                    Modifier.height(12.dp)
                            )

                            Text(
                                text =
                                    "TOTAL - ${formatReportKilos(totalKilos)}",

                                style =
                                    MaterialTheme
                                        .typography
                                        .titleMedium,

                                fontWeight =
                                    FontWeight.Bold
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(8.dp)
                            )

                            Text(
                                text =
                                    if (allVehiclesIn)
                                        "All Vehicles In"
                                    else
                                        "$pendingVehicles Vehicles Pending",

                                fontWeight =
                                    FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // =================================================
            // COPY REPORT
            // =================================================

            item {

                Button(

                    onClick = {

                        val report =
                            buildWhatsAppReport(
                                entries =
                                    entries,

                                pendingVehicles =
                                    pendingVehicles,

                                allVehiclesIn =
                                    allVehiclesIn
                            )

                        val clipboard =
                            context.getSystemService(
                                Context.CLIPBOARD_SERVICE
                            ) as ClipboardManager

                        clipboard.setPrimaryClip(
                            ClipData.newPlainText(
                                "Weighbridge Report",
                                report
                            )
                        )

                        reportCopied = true
                    },

                    enabled =
                        entries.isNotEmpty(),

                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(52.dp),

                    shape =
                        RoundedCornerShape(10.dp)
                ) {

                    Text(
                        text =
                            if (reportCopied)
                                "REPORT COPIED"
                            else
                                "COPY WHATSAPP REPORT",

                        fontWeight =
                            FontWeight.Bold
                    )
                }
            }

            // =================================================
            // ADD VEHICLE
            // =================================================

            item {

                Text(
                    text = "Add Vehicle",

                    style =
                        MaterialTheme
                            .typography
                            .titleLarge,

                    fontWeight =
                        FontWeight.Bold
                )
            }

            // =================================================
            // ROUTE
            // =================================================

            item {

                ExposedDropdownMenuBox(

                    expanded =
                        routeExpanded,

                    onExpandedChange = {

                        routeExpanded =
                            !routeExpanded
                    }
                ) {

                    OutlinedTextField(

                        value =
                            selectedRoute,

                        onValueChange = {},

                        readOnly = true,

                        label = {
                            Text("Route")
                        },

                        placeholder = {
                            Text("Select route")
                        },

                        trailingIcon = {

                            ExposedDropdownMenuDefaults
                                .TrailingIcon(
                                    expanded =
                                        routeExpanded
                                )
                        },

                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                    )

                    ExposedDropdownMenu(

                        expanded =
                            routeExpanded,

                        onDismissRequest = {

                            routeExpanded =
                                false
                        }
                    ) {

                        routes.forEach { route ->

                            DropdownMenuItem(

                                text = {
                                    Text(route)
                                },

                                onClick = {

                                    selectedRoute =
                                        route

                                    routeExpanded =
                                        false
                                }
                            )
                        }
                    }
                }
            }

            // =================================================
            // TRIP
            // =================================================

            item {

                ExposedDropdownMenuBox(

                    expanded =
                        tripExpanded,

                    onExpandedChange = {

                        tripExpanded =
                            !tripExpanded
                    }
                ) {

                    OutlinedTextField(

                        value =
                            when (selectedTrip) {

                                1 -> "TRP1"

                                2 -> "TRP2"

                                else -> ""
                            },

                        onValueChange = {},

                        readOnly = true,

                        label = {
                            Text("Trip (Optional)")
                        },

                        placeholder = {
                            Text("None")
                        },

                        trailingIcon = {

                            ExposedDropdownMenuDefaults
                                .TrailingIcon(
                                    expanded =
                                        tripExpanded
                                )
                        },

                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                    )

                    ExposedDropdownMenu(

                        expanded =
                            tripExpanded,

                        onDismissRequest = {

                            tripExpanded =
                                false
                        }
                    ) {

                        DropdownMenuItem(

                            text = {
                                Text("None")
                            },

                            onClick = {

                                selectedTrip =
                                    null

                                tripExpanded =
                                    false
                            }
                        )

                        DropdownMenuItem(

                            text = {
                                Text("TRP1")
                            },

                            onClick = {

                                selectedTrip =
                                    1

                                tripExpanded =
                                    false
                            }
                        )

                        DropdownMenuItem(

                            text = {
                                Text("TRP2")
                            },

                            onClick = {

                                selectedTrip =
                                    2

                                tripExpanded =
                                    false
                            }
                        )
                    }
                }
            }

            // =================================================
            // KILOS
            // =================================================

            item {

                OutlinedTextField(

                    value =
                        kilosText,

                    onValueChange = {

                        kilosText = it

                        reportCopied =
                            false
                    },

                    label = {
                        Text("Kilos")
                    },

                    placeholder = {
                        Text("Enter kilos")
                    },

                    keyboardOptions =
                        KeyboardOptions(
                            keyboardType =
                                KeyboardType.Decimal
                        ),

                    singleLine = true,

                    modifier =
                        Modifier.fillMaxWidth()
                )
            }

            // =================================================
            // UPDATE
            // =================================================

            item {

                Button(

                    onClick = {

                        val kilos =
                            kilosText
                                .toDoubleOrNull()

                        if (
                            selectedRoute.isNotBlank() &&
                            kilos != null &&
                            kilos > 0
                        ) {

                            val now =
                                System.currentTimeMillis()

                            viewModel.addWeighment(

                                WeighmentEntity(

                                    id = now,

                                    sessionId =
                                        session.id,

                                    route =
                                        selectedRoute,

                                    trip =
                                        selectedTrip,

                                    kilos =
                                        kilos,

                                    date =
                                        SimpleDateFormat(
                                            "yyyy-MM-dd",
                                            Locale.getDefault()
                                        ).format(Date()),

                                    timestamp =
                                        now
                                )
                            )

                            selectedRoute = ""
                            selectedTrip = null
                            kilosText = ""
                            reportCopied = false
                        }
                    },

                    enabled =
                        selectedRoute.isNotBlank() &&
                                kilosText
                                    .toDoubleOrNull()
                                    ?.let {
                                        it > 0
                                    } == true,

                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(52.dp),

                    shape =
                        RoundedCornerShape(10.dp)
                ) {

                    Text(
                        text = "UPDATE",
                        fontWeight =
                            FontWeight.Bold
                    )
                }
            }

            // =================================================
            // UPDATE VEHICLE TARGET
            // =================================================

            item {

                OutlinedButton(

                    onClick = {

                        editVehicleText =
                            totalVehicles.toString()

                        showEditVehicleDialog =
                            true
                    },

                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(52.dp),

                    shape =
                        RoundedCornerShape(10.dp)
                ) {

                    Text(
                        text =
                            "UPDATE VEHICLE TARGET",

                        fontWeight =
                            FontWeight.Bold
                    )
                }
            }

            // =================================================
            // CLOSE SESSION
            // =================================================

            item {

                OutlinedButton(

                    onClick = {

                        showCloseDialog =
                            true
                    },

                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(52.dp),

                    shape =
                        RoundedCornerShape(10.dp)
                ) {

                    Text(
                        text =
                            "CLOSE SESSION",

                        fontWeight =
                            FontWeight.Bold
                    )
                }
            }

            // =================================================
            // CORRECTIONS
            // =================================================

            if (entries.isNotEmpty()) {

                item {

                    Text(
                        text = "Corrections",

                        style =
                            MaterialTheme
                                .typography
                                .titleMedium,

                        fontWeight =
                            FontWeight.Bold
                    )
                }

                entries
                    .sortedByDescending {
                        it.id
                    }
                    .forEach { entry ->

                        item(
                            key =
                                "correction_${entry.id}"
                        ) {

                            Card(

                                modifier =
                                    Modifier.fillMaxWidth(),

                                shape =
                                    RoundedCornerShape(10.dp)
                            ) {

                                Row(

                                    modifier =
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(12.dp),

                                    verticalAlignment =
                                        Alignment.CenterVertically
                                ) {

                                    Column(
                                        modifier =
                                            Modifier.weight(1f)
                                    ) {

                                        Text(
                                            text =
                                                entry.route,

                                            fontWeight =
                                                FontWeight.Bold
                                        )

                                        Text(
                                            text =
                                                entryDescription(
                                                    entry
                                                ),

                                            style =
                                                MaterialTheme
                                                    .typography
                                                    .bodySmall
                                        )
                                    }

                                    TextButton(

                                        onClick = {

                                            viewModel
                                                .deleteWeighment(
                                                    entry.id
                                                )

                                            reportCopied =
                                                false
                                        }
                                    ) {

                                        Text("Delete")
                                    }
                                }
                            }
                        }
                    }
            }
        }
    }

    // =========================================================
    // CLOSE SESSION DIALOG
    // =========================================================

    if (showCloseDialog) {

        AlertDialog(

            onDismissRequest = {

                showCloseDialog =
                    false
            },

            title = {

                Text(
                    "Close Session?"
                )
            },

            text = {

                Text(

                    if (allVehiclesIn) {

                        "All $totalVehicles vehicles have been completed. Close this session and save it to history?"

                    } else {

                        "$pendingVehicles vehicles are still pending. Are you sure you want to close this session?"
                    }
                )
            },

            confirmButton = {

                TextButton(

                    onClick = {

                        /*
                         * DO NOT DELETE WEIGHMENTS.
                         *
                         * Closing the session preserves the
                         * day's records for history.
                         */

                        viewModel.closeSession()

                        selectedRoute = ""
                        selectedTrip = null
                        kilosText = ""
                        reportCopied = false

                        showCloseDialog =
                            false
                    }
                ) {

                    Text(
                        "CLOSE SESSION",
                        fontWeight =
                            FontWeight.Bold
                    )
                }
            },

            dismissButton = {

                TextButton(

                    onClick = {

                        showCloseDialog =
                            false
                    }
                ) {

                    Text("CANCEL")
                }
            }
        )
    }

    // =========================================================
    // EDIT VEHICLE TARGET DIALOG
    // =========================================================

    if (showEditVehicleDialog) {

        AlertDialog(

            onDismissRequest = {

                showEditVehicleDialog =
                    false
            },

            title = {

                Text(
                    text =
                        "Update Vehicle Target",

                    fontWeight =
                        FontWeight.Bold
                )
            },

            text = {

                Column {

                    Text(
                        text =
                            "Current target: $totalVehicles"
                    )

                    Spacer(
                        modifier =
                            Modifier.height(8.dp)
                    )

                    Text(
                        text =
                            "Vehicles completed: $completedVehicles"
                    )

                    Spacer(
                        modifier =
                            Modifier.height(16.dp)
                    )

                    OutlinedTextField(

                        value =
                            editVehicleText,

                        onValueChange = {

                            editVehicleText =
                                it.filter { character ->
                                    character.isDigit()
                                }
                        },

                        label = {
                            Text("New Vehicle Target")
                        },

                        placeholder = {
                            Text("Enter target")
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

                    val newTarget =
                        editVehicleText.toIntOrNull()

                    if (
                        newTarget != null &&
                        newTarget < completedVehicles
                    ) {

                        Spacer(
                            modifier =
                                Modifier.height(8.dp)
                        )

                        Text(
                            text =
                                "Target cannot be lower than $completedVehicles vehicles already completed.",

                            color =
                                MaterialTheme
                                    .colorScheme
                                    .error,

                            style =
                                MaterialTheme
                                    .typography
                                    .bodySmall
                        )
                    }
                }
            },

            confirmButton = {

                val newTarget =
                    editVehicleText.toIntOrNull()

                val canUpdate =
                    newTarget != null &&
                            newTarget > 0 &&
                            newTarget >= completedVehicles

                TextButton(

                    onClick = {

                        if (canUpdate) {

                            viewModel
                                .updateSessionVehicleTarget(
                                    newTarget!!
                                )

                            showEditVehicleDialog =
                                false
                        }
                    },

                    enabled =
                        canUpdate
                ) {

                    Text(
                        text = "UPDATE",
                        fontWeight =
                            FontWeight.Bold
                    )
                }
            },

            dismissButton = {

                TextButton(

                    onClick = {

                        showEditVehicleDialog =
                            false
                    }
                ) {

                    Text("CANCEL")
                }
            }
        )
    }
}

// =============================================================
// START SESSION SCREEN
// =============================================================

@Composable
private fun SessionStartScreen(
    onStart: (Int) -> Unit
) {

    var totalVehiclesText by remember {
        mutableStateOf("")
    }

    val totalVehicles =
        totalVehiclesText
            .toIntOrNull()
            ?.coerceAtLeast(0)
            ?: 0

    Column(

        modifier =
            Modifier
                .fillMaxSize()
                .padding(24.dp)
                .navigationBarsPadding()
                .imePadding(),

        verticalArrangement =
            Arrangement.Center,

        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Text(

            text =
                "Start Collection Session",

            style =
                MaterialTheme
                    .typography
                    .headlineSmall,

            fontWeight =
                FontWeight.Bold
        )

        Spacer(
            modifier =
                Modifier.height(8.dp)
        )

        Text(
            text =
                "Enter the number of vehicles expected for today."
        )

        Spacer(
            modifier =
                Modifier.height(24.dp)
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

            placeholder = {
                Text("e.g. 25")
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

        Spacer(
            modifier =
                Modifier.height(16.dp)
        )

        Button(

            onClick = {

                onStart(totalVehicles)
            },

            enabled =
                totalVehicles > 0,

            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(52.dp),

            shape =
                RoundedCornerShape(10.dp)
        ) {

            Text(
                text =
                    "START SESSION",

                fontWeight =
                    FontWeight.Bold
            )
        }
    }
}

// =============================================================
// VEHICLE STATE
// =============================================================

private data class VehicleState(

    val route: String,

    val inSystem: Boolean,

    val completed: Boolean
)

// =============================================================
// VEHICLE CALCULATION
// =============================================================

private fun calculateVehicleStates(
    entries: List<CollectionEntry>
): List<VehicleState> {

    val grouped =
        entries.groupBy { it.route }

    val result =
        mutableListOf<VehicleState>()

    for ((route, routeEntries) in grouped) {

        val sorted =
            routeEntries.sortedBy { it.id }

        val first =
            sorted.firstOrNull()
                ?: continue

        when (first.trip) {

            // =================================================
            // TRP1
            // =================================================

            1 -> {

                val hasReturn =
                    sorted
                        .drop(1)
                        .any {
                            it.trip == null || it.trip == 2
                        }

                result.add(
                    VehicleState(
                        route = route,
                        inSystem = true,
                        completed = hasReturn
                    )
                )
            }

            // =================================================
            // NORMAL
            // =================================================

            null -> {

                result.add(
                    VehicleState(
                        route = route,
                        inSystem = true,
                        completed = true
                    )
                )
            }

            // =================================================
            // TRP2
            // =================================================

            2 -> {

                result.add(
                    VehicleState(
                        route = route,
                        inSystem = true,
                        completed = true
                    )
                )
            }
        }
    }

    return result
}

// =============================================================
// SUMMARY CARD
// =============================================================

@Composable
private fun SummaryCard(

    title: String,

    value: String,

    modifier: Modifier
) {

    Card(

        modifier =
            modifier,

        shape =
            RoundedCornerShape(12.dp),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 2.dp
            )
    ) {

        Column(
            modifier =
                Modifier.padding(12.dp)
        ) {

            Text(

                text =
                    title,

                style =
                    MaterialTheme
                        .typography
                        .labelSmall,

                fontWeight =
                    FontWeight.Bold
            )

            Spacer(
                modifier =
                    Modifier.height(4.dp)
            )

            Text(

                text =
                    value,

                style =
                    MaterialTheme
                        .typography
                        .headlineSmall,

                fontWeight =
                    FontWeight.Bold
            )
        }
    }
}

// =============================================================
// REPORT ENTRY
// =============================================================

@Composable
private fun ReportEntry(
    entry: CollectionEntry
) {

    Row(

        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = 3.dp),

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        val routeText =
            when (entry.trip) {

                1 ->
                    "${entry.route} TRP1-"

                2 ->
                    "${entry.route} TRP2-"

                else ->
                    "${entry.route}-"
            }

        Text(

            text =
                routeText,

            modifier =
                Modifier.weight(1f),

            fontWeight =
                FontWeight.Medium
        )

        Text(

            text =
                formatReportKilos(
                    entry.kilos
                ),

            fontWeight =
                FontWeight.Medium
        )
    }
}

// =============================================================
// ENTRY DESCRIPTION
// =============================================================

private fun entryDescription(
    entry: CollectionEntry
): String {

    val trip =
        when (entry.trip) {

            1 ->
                "TRP1"

            2 ->
                "TRP2"

            else ->
                "Normal"
        }

    return "$trip • ${formatKilos(entry.kilos)} KG"
}

// =============================================================
// WHATSAPP REPORT
// =============================================================

private fun buildWhatsAppReport(

    entries: List<CollectionEntry>,

    pendingVehicles: Int,

    allVehiclesIn: Boolean

): String {

    val report =
        StringBuilder()

    val sorted =
        entries.sortedBy {
            it.id
        }

    for (entry in sorted) {

        val routeText =
            when (entry.trip) {

                1 ->
                    "${entry.route} TRP1-"

                2 ->
                    "${entry.route} TRP2-"

                else ->
                    "${entry.route}-"
            }

        report.append(
            "$routeText ${formatReportKilos(entry.kilos)}"
        )

        report.append("\n")
    }

    val totalKilos =
        entries.sumOf {
            it.kilos
        }

    report.append("\n")

    report.append(
        "TOTAL - ${formatReportKilos(totalKilos)}"
    )

    report.append("\n\n")

    if (allVehiclesIn) {

        report.append(
            "All Vehicles In"
        )

    } else {

        report.append(
            "$pendingVehicles Vehicles Pending"
        )
    }

    return report.toString()
}

// =============================================================
// NUMBER FORMATTING
// =============================================================

private fun formatKilos(
    value: Double
): String {

    return NumberFormat
        .getNumberInstance(Locale.US)
        .apply {

            minimumFractionDigits = 1

            maximumFractionDigits = 1

        }
        .format(value)
}

private fun formatReportKilos(
    value: Double
): String {

    return NumberFormat
        .getNumberInstance(Locale.US)
        .apply {

            minimumFractionDigits = 0

            maximumFractionDigits = 1

        }
        .format(value)
}
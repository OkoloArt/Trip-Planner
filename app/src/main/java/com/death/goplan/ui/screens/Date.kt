package com.death.goplan.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.death.goplan.R
import com.death.goplan.ui.component.CustomDateRangePicker
import com.death.goplan.ui.viewmodel.TripViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateScreen(
    navController: NavController? = null,
    viewModel: TripViewModel,
    modifier: Modifier = Modifier,
    onChooseDateClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    // Initialize local state for the custom picker from ViewModel
    var startDate by remember(uiState.startDate) { 
        mutableStateOf(parseDateStringToLocalDate(uiState.startDate)) 
    }
    var endDate by remember(uiState.endDate) { 
        mutableStateOf(parseDateStringToLocalDate(uiState.endDate)) 
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color.White,

        topBar = {
            Box(modifier = Modifier.background(Color.White)) {
                TopAppBar(
                    title = {
                        Text(
                            text = "Date",
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = { navController?.popBackStack() }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.close_24),
                                contentDescription = ""
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White,
                        titleContentColor = Color.Black
                    )
                )

                HorizontalDivider(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth(),
                    thickness = 1.dp,
                    color = Color(0xFFF3F4F6)
                )
            }
        },

        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val startDisplay = startDate?.let { formatDate(it) } ?: "Select Date"
                    DateInputBox(
                        label = "Start Date",
                        value = startDisplay,
                        modifier = Modifier.weight(1f)
                    )

                    val endDisplay = endDate?.let { formatDate(it) } ?: "Select Date"
                    DateInputBox(
                        label = "End Date",
                        value = endDisplay,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (startDate != null && endDate != null) {
                             viewModel.updateDateRange(
                                startDate = startDate!!.toReadableDateWithSuffix(),
                                endDate = endDate!!.toReadableDateWithSuffix()
                            )
                            onChooseDateClick()
                        }
                    },
                    enabled = startDate != null && endDate != null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0D6EFD),
                        disabledContainerColor = Color(0xFF0D6EFD).copy(alpha = 0.5f)
                    )
                ) {
                    Text(
                        text = "Choose Date",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            CustomDateRangePicker(
                modifier = Modifier.fillMaxWidth(),
                startDate = startDate,
                endDate = endDate,
                onDateRangeSelected = { newStart, newEnd ->
                    startDate = newStart
                    endDate = newEnd
                }
            )
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
fun formatDate(date: LocalDate): String {
    val formatter = DateTimeFormatter.ofPattern("EEE, MMM d", Locale.getDefault())
    return date.format(formatter)
}

@RequiresApi(Build.VERSION_CODES.O)
fun parseDateStringToLocalDate(date: String?): LocalDate? {
    if (date.isNullOrBlank()) return null

    return try {
        // Remove ordinal suffixes: 1st, 2nd, 3rd, 4th...
        val cleanedDate = date.replace(
            Regex("(\\d+)(st|nd|rd|th)"),
            "$1"
        )
        // Assume default format "d MMM yyyy" from toReadableDateWithSuffix
        // If the stored format is different, adjust here.
        // Previously used DateTimeFormatter.ofPattern("d MMM yyyy", Locale.ENGLISH)
        val formatter = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.ENGLISH)
        LocalDate.parse(cleanedDate, formatter)
    } catch (e: Exception) {
        null
    }
}


@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.toReadableDateWithSuffix(): String {
    return try {
        val day = this.dayOfMonth
        val suffix = when {
            day in 11..13 -> "th" // special case for 11,12,13
            day % 10 == 1 -> "st"
            day % 10 == 2 -> "nd"
            day % 10 == 3 -> "rd"
            else -> "th"
        }

        val monthYear = this.format(DateTimeFormatter.ofPattern("MMM yyyy"))
        "$day$suffix $monthYear" // e.g., "10th Aug 2024"
    } catch (e: Exception) {
        "" // fallback
    }
}

@Composable
fun DateInputBox(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF6B7280)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0xFFD1D5DB), RoundedCornerShape(4.dp))
                .padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )
                Icon(
                    imageVector = Icons.Outlined.CalendarToday,
                    contentDescription = null,
                    tint = Color(0xFF6B7280),
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

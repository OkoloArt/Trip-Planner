package com.death.goplan.ui.screens


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
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.death.goplan.R
import com.death.goplan.viewmodel.TripViewModel
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateScreen(
    navController: NavController? = null,
    viewModel: TripViewModel,
    modifier: Modifier = Modifier,
    onChooseDateClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    val dateRangePickerState = rememberDateRangePickerState(
        initialSelectedStartDateMillis = parseDateStringToMillis(uiState.startDate),
        initialSelectedEndDateMillis = parseDateStringToMillis(uiState.endDate)
    )

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
                    val startDate = dateRangePickerState.selectedStartDateMillis
                        ?.let { formatDate(it) }
                        ?: uiState.startDate
                        ?: "Select Date"

                    DateInputBox(
                        label = "Start Date",
                        value = startDate,
                        modifier = Modifier.weight(1f)
                    )

                    val endDate = dateRangePickerState.selectedEndDateMillis
                        ?.let { formatDate(it) }
                        ?: uiState.endDate
                        ?: "Select Date"

                    DateInputBox(
                        label = "End Date",
                        value = endDate,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        viewModel.updateDateRange(
                            startDate = dateRangePickerState.selectedStartDateMillis!!.toReadableDateWithSuffix(),
                            endDate = dateRangePickerState.selectedEndDateMillis!!.toReadableDateWithSuffix()
                        )
                        onChooseDateClick()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D6EFD))
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
                .padding(
                    top = 8.dp,
                    bottom = innerPadding.calculateBottomPadding()
                )
        ) {
            DateRangePicker(
                state = dateRangePickerState,
                showModeToggle = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White),
                headline = { Box(modifier = Modifier.height(1.dp)) {} },
                title = { Box(modifier = Modifier.height(1.dp)) {} },
                colors = DatePickerDefaults.colors(
                    containerColor = Color.White,
                    selectedDayContainerColor = MaterialTheme.colorScheme.primary,
                    selectedDayContentColor = Color.White,
                    dayInSelectionRangeContainerColor = Color(0xFFE7F0FF),
                    dayInSelectionRangeContentColor = Color(0xFF0D6EFD),
                    todayDateBorderColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}


fun formatDate(millis: Long): String {
    val formatter = SimpleDateFormat("EEE, MMM d", Locale.getDefault())
    return formatter.format(Date(millis))
}

fun parseDateStringToMillis(date: String?): Long? {
    if (date.isNullOrBlank()) return null

    return try {
        // Remove ordinal suffixes: 1st, 2nd, 3rd, 4th...
        val cleanedDate = date.replace(
            Regex("(\\d+)(st|nd|rd|th)"),
            "$1"
        )

        val formatter = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.ENGLISH)
        val localDate = LocalDate.parse(cleanedDate, formatter)

        localDate
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

    } catch (e: Exception) {
        null
    }
}


fun Long.toReadableDateWithSuffix(): String {
    return try {
        val date = Instant.ofEpochMilli(this)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()

        val day = date.dayOfMonth
        val suffix = when {
            day in 11..13 -> "th" // special case for 11,12,13
            day % 10 == 1 -> "st"
            day % 10 == 2 -> "nd"
            day % 10 == 3 -> "rd"
            else -> "th"
        }

        val monthYear = date.format(DateTimeFormatter.ofPattern("MMM yyyy"))
        "$day$suffix $monthYear" // e.g., "10th Aug 2024"
    } catch (e: Exception) {
        "" // fallback
    }
}

@Composable
fun DateInputBox(
    label: String,
    value: String?,
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
                    text = value!!,
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

package com.death.goplan.ui.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.death.goplan.ui.screens.parseDateStringToMillis
import com.death.goplan.ui.viewmodel.TripUiState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class SelectedField {
    CITY, START_DATE, END_DATE, NONE
}

@Composable
fun CreateTripCard(
    uiState: TripUiState = TripUiState(),
    modifier: Modifier = Modifier,
    onSelectCityClick: () -> Unit = {},
    onStartDateClick: () -> Unit = {},
    onEndDateClick: () -> Unit = {},
    onCreateTripClick: () -> Unit = {}
) {
    var selectedField by remember { mutableStateOf(SelectedField.NONE) }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Where to?
            SelectionInput(
                label = "Where to ?",
                value = uiState.cityName.ifEmpty { "Select a city" },
                icon = Icons.Outlined.Place,
                isHighlight = selectedField == SelectedField.CITY,
                onClick = {
                    selectedField = SelectedField.CITY
                    onSelectCityClick()
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row {
                SelectionInput(
                    label = "Start Date",
                    value = uiState.startDate?.let { parseDateStringToMillis(it)?.let { millis -> formatDate(millis) } } ?: "Select Date",
                    icon = Icons.Outlined.CalendarToday,
                    isHighlight = selectedField == SelectedField.START_DATE,
                    onClick = {
                        selectedField = SelectedField.START_DATE
                        onStartDateClick()
                    },
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(12.dp))

                SelectionInput(
                    label = "End Date",
                    value = uiState.endDate?.let { parseDateStringToMillis(it)?.let { millis -> formatDate(millis) } } ?:"Enter Date",
                    icon = Icons.Outlined.CalendarToday,
                    isHighlight = selectedField == SelectedField.END_DATE,
                    onClick = {
                        selectedField = SelectedField.END_DATE
                        onEndDateClick()
                    },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onCreateTripClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D6EFD))
            ) {
                Text(
                    "Create a Trip",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}


fun formatDate(millis: Long): String {
    val formatter = SimpleDateFormat("EEE, MMM d", Locale.getDefault())
    return formatter.format(Date(millis))
}

@Composable
fun SelectionInput(
    label: String,
    value: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isHighlight: Boolean = false
) {
    val borderColor =
        if (isHighlight) Color(0xFFA855F7) else Color(0xFFE5E7EB) // Purple or Light Gray
    val iconColor = if (isHighlight) Color(0xFFA855F7) else Color(0xFF6B7280) // Purple or Gray

    Row(
        modifier = modifier
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(8.dp)
            )
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color(0xFF6B7280)
                )
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = Color(0xFF374151)
                )
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF3F4F6)
@Composable
fun CreateTripCardPreview() {
    Box(modifier = Modifier.padding(16.dp)) {
        CreateTripCard()
    }
}

package com.death.goplan.ui.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.death.goplan.ui.theme.TripColors
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CustomDateRangePicker(
    modifier: Modifier = Modifier,
    startDate: LocalDate? = null,
    endDate: LocalDate? = null,
    onDateRangeSelected: (LocalDate?, LocalDate?) -> Unit
) {
    val currentMonth = YearMonth.now()
    val listState = rememberLazyListState()

    // Generate a list of months (e.g., current month + next 11 months)
    val months = remember {
        (0..11).map { currentMonth.plusMonths(it.toLong()) }
    }

    // Scroll to the start date if provided
    LaunchedEffect(startDate) {
        if (startDate != null) {
            val monthIndex = months.indexOfFirst { it == YearMonth.from(startDate) }
            if (monthIndex != -1) {
                listState.scrollToItem(monthIndex)
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f), // Take remaining space
            verticalArrangement = Arrangement.spacedBy(24.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(months) { month ->
                MonthView(
                    yearMonth = month,
                    startDate = startDate,
                    endDate = endDate,
                    onDateClick = { clickedDate ->
                        var newStartDate = startDate
                        var newEndDate = endDate

                        if (newStartDate == null) {
                            newStartDate = clickedDate
                        } else if (newEndDate == null) {
                            if (clickedDate.isBefore(newStartDate)) {
                                newStartDate = clickedDate
                            } else {
                                newEndDate = clickedDate
                            }
                        } else {
                            // Start new range
                            newStartDate = clickedDate
                            newEndDate = null
                        }
                        onDateRangeSelected(newStartDate, newEndDate)
                    }
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MonthView(
    yearMonth: YearMonth,
    startDate: LocalDate?,
    endDate: LocalDate?,
    onDateClick: (LocalDate) -> Unit
) {
    Column {
        Text(
            text = "${yearMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${yearMonth.year}",
            style =MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )

        // Days Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val daysOfWeek = listOf(
                DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY
            )
            daysOfWeek.forEach { dayOfWeek ->
                Text(
                    text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()).take(2),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF1F2937)
                    )
                )
            }
        }

        val days = remember(yearMonth) { getDaysInMonth(yearMonth) }

        // We can't use LazyVerticalGrid inside LazyColumn directly due to nested scroll issues
        // Instead, we can use a custom Row-based grid layout or flow layout.
        // Given fixed 7 columns, a Column of Rows works well.
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            days.chunked(7).forEach { week ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    week.forEach { date ->
                        val isCurrentMonth = date.month == yearMonth.month
                        val isSelected = date == startDate || date == endDate
                        val isInRange = startDate != null && endDate != null &&
                                date.isAfter(startDate) && date.isBefore(endDate)

                        Box(modifier = Modifier.weight(1f)) {
                            // Only show DateCell if it belongs to the current month or if we want to show prev/next month days
                            // For a vertical list, typically we hide prev/next month days to avoid duplication,
                            // or we just render them empty/invisible to keep alignment.
                            if (isCurrentMonth) {
                                DateCell(
                                    date = date,
                                    isCurrentMonth = true,
                                    isSelected = isSelected,
                                    isInRange = isInRange,
                                    onClick = onDateClick
                                )
                            } else {
                                // Spacer for alignment
                                Spacer(modifier = Modifier.aspectRatio(1f))
                            }
                        }
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateCell(
    date: LocalDate,
    isCurrentMonth: Boolean,
    isSelected: Boolean,
    isInRange: Boolean,
    onClick: (LocalDate) -> Unit
) {
    val backgroundColor = when {
        isSelected -> TripColors.PrimaryBlue
        isInRange -> TripColors.PrimaryBlue.copy(alpha = 0.1f)
        else -> TripColors.Divider // 0xFFF3F4F6 as seen in image
    }

    val textColor = when {
        isSelected -> Color.White
        !isCurrentMonth -> Color(0xFF9CA3AF) // Gray for other months
        else -> Color(0xFF1F2937) // Dark gray/black for current month
    }

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .clickable { onClick(date) },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = date.dayOfMonth.toString(),
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = textColor
            )
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun getDaysInMonth(yearMonth: YearMonth): List<LocalDate> {
    val days = mutableListOf<LocalDate>()
    val firstDayOfMonth = yearMonth.atDay(1)
    val lastDayOfMonth = yearMonth.atEndOfMonth()

    // Previous month padding
    // DayOfWeek 1 (Mon) -> 7 (Sun)
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value
    val daysToPad = firstDayOfWeek - 1

    val prevMonth = yearMonth.minusMonths(1)
    val prevMonthLastDay = prevMonth.atEndOfMonth()
    for (i in 0 until daysToPad) {
        days.add(0, prevMonthLastDay.minusDays(i.toLong()))
    }

    // Current month days
    for (i in 1..lastDayOfMonth.dayOfMonth) {
        days.add(yearMonth.atDay(i))
    }

    // Next month padding to fill grid
    // Ensure we have a multiple of 7
    val totalDays = days.size
    val remainingCells = if (totalDays % 7 == 0) 0 else 7 - (totalDays % 7)

    // Also minimal rows check (image shows 5 or 6 rows)
    // If we have very few days (e.g. 28 + 0 padding), we might want to show more rows?
    // The image shows 6 rows. 6 * 7 = 42.
    // In vertical scroll, usually we just want to fill the last row to complete the grid visually

    val nextMonth = yearMonth.plusMonths(1)
    for (i in 1..remainingCells) {
        days.add(nextMonth.atDay(i))
    }

    return days
}

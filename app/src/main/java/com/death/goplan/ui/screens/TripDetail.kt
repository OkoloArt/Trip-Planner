package com.death.goplan.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.FlightLand
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.LocalActivity
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.death.goplan.R
import com.death.goplan.data.model.ActivityDto
import com.death.goplan.data.model.FlightDto
import com.death.goplan.data.model.HotelDto
import com.death.goplan.data.model.TripDto
import com.death.goplan.ui.theme.TripColors
import com.death.goplan.ui.theme.TripDimens
import com.death.goplan.utils.ApiResult
import com.death.goplan.ui.viewmodel.TripViewModel
import kotlinx.coroutines.delay
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripDetailScreen(navController: NavController? = null, viewModel: TripViewModel) {
    val detailState by viewModel.tripDetailState.collectAsState()
    var showLoading by remember { mutableStateOf(true) }

    // Fetch trip details on start
    LaunchedEffect(Unit) {
        viewModel.fetchSelectedTripDetails()
    }

    LaunchedEffect(detailState) {
        if (detailState is ApiResult.Success || detailState is ApiResult.Error) {
            delay(2000)
            showLoading = false
        } else {
            showLoading = true
        }
    }

    when {
        showLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LinearProgressIndicator(color = TripColors.PrimaryBlue)
            }
        }

        detailState is ApiResult.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.not_found),
                        contentDescription = "Error",
                        modifier = Modifier.size(68.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Oops! Something went wrong",
                        style = MaterialTheme.typography.titleSmall,
                        color = Color(0xFFEF4444),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Failed to load trip details...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            showLoading = true
                            viewModel.fetchSelectedTripDetails()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF0D6EFD)
                        )
                    ) {
                        Text(text = "Retry", color = Color.White)
                    }
                }
            }
        }


        detailState is ApiResult.Success -> {
            TripDetailContent(
                navController = navController,
                trip = (detailState as ApiResult.Success<TripDto>).data,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TripDetailContent(
    navController: NavController?,
    trip: TripDto,
) {
    val imageUrl =
        "https://images.unsplash.com/photo-1477959858617-67f85cf4f1df?q=80&w=2613&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"

    // Dummy data for non-empty states
    val dummyFlight = FlightDto(
        id = "1",
        airline = "American Airlines",
        flightNumber = "AA-829",
        departureTime = "08:35",
        arrivalTime = "09:55",
        duration = "1h 45m",
        from = "LOS",
        to = "SIN",
        stops = "Direct",
        price = 123450.00
    )

    val dummyHotel = HotelDto(
        id = "1",
        name = "Riviera Resort, Lekki",
        address = "18, Kenneth Agbakuru Street, Off Access Bank Admiralty Way, Lekki Phase1",
        imageUrl = imageUrl,
        rating = 8.5,
        roomType = "King size room",
        checkIn = "20-04-2024",
        checkOut = "29-04-2024",
        price = 123450.00
    )

    val dummyActivity = ActivityDto(
        id = "1",
        name = "The Museum of Modern Art",
        description = "Works from Van Gogh to Warhol & beyond plus a sculpture garden, 2 cafes & The modern restaurant",
        location = "Melbourne, Austraila",
        rating = 8.5,
        duration = "1 hour",
        time = "10:30 AM",
        date = "Mar 19",
        imageUrl = imageUrl,
        price = 123450.00
    )

    // Local UI states
    var isFlightAdded by remember { mutableStateOf(false) }
    var isHotelAdded by remember { mutableStateOf(false) }
    var isActivityAdded by remember { mutableStateOf(false) }

    val tripTitle = trip.name
    val tripDateRange =
        "${trip.startDate ?: "N/A"} → ${trip.endDate ?: "N/A"}"
    val tripSubtitle =
        "${trip.destination ?: "No destination"} | ${trip.style} Trip"

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Plan a Trip",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController?.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },

        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {

            // Top Image
            Image(
                painter = painterResource(R.drawable.trip_detail_body),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            Column(modifier = Modifier.padding(8.dp)) {

                TripDetailsHeader(
                    title = tripTitle,
                    dateRange = tripDateRange,
                    subtitle = tripSubtitle
                )

                Spacer(modifier = Modifier.height(20.dp))

                ActivitiesCard(onAddClick = { isActivityAdded = true })
                Spacer(modifier = Modifier.height(16.dp))
                HotelsCard(onAddClick = { isHotelAdded = true })
                Spacer(modifier = Modifier.height(16.dp))
                FlightsCard(onAddClick = { isFlightAdded = true })

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Trip Itineraries",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Your trip itineraries are placed here",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Flights
                if (isFlightAdded) {
                    FlightCard(dummyFlight) { isFlightAdded = false }
                } else {
                    FlightEmptyState { isFlightAdded = true }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Hotels
                if (isHotelAdded) {
                    HotelCard(dummyHotel) { isHotelAdded = false }
                } else {
                    HotelEmptyState { isHotelAdded = true }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Activities
                if (isActivityAdded) {
                    ActivityCard(dummyActivity) { isActivityAdded = false }
                } else {
                    ActivitiesEmptyState { isActivityAdded = true }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun TripDetailsHeader(
    modifier: Modifier = Modifier,
    dateRange: String ,
    title: String,
    subtitle: String,
    onCollaborateClick: () -> Unit = {},
    onShareClick: () -> Unit = {},
    onMoreClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(vertical = 16.dp)
    ) {
        // Date Range
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(Color(0xFFF8F9FA), RoundedCornerShape(4.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_calendar),
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = Color.Black
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = dateRange,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Title
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Subtitle
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Buttons
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = onCollaborateClick,
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color(0xFF0D6EFD)),
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(
                    horizontal = 8.dp,  // 🔹 reduce start/end padding
                    vertical = 6.dp     // 🔹 optional: reduce height
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_handshake),
                    contentDescription = null,
                    tint = Color(0xFF0D6EFD),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Trip Collaboration",
                    color = Color(0xFF0D6EFD),
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            OutlinedButton(
                onClick = onShareClick,
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color(0xFF0D6EFD)),
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(
                    horizontal = 8.dp,  // 🔹 reduce start/end padding
                    vertical = 6.dp     // 🔹 optional: reduce height
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_share),
                    contentDescription = null,
                    tint = Color(0xFF0D6EFD),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Share Trip",
                    color = Color(0xFF0D6EFD),
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            IconButton(onClick = onMoreClick) {
                Icon(
                    imageVector = Icons.Default.MoreHoriz,
                    contentDescription = "More",
                    tint = Color.Black
                )
            }
        }
    }
}

@Composable
fun ActivitiesCard(
    modifier: Modifier = Modifier,
    onAddClick: () -> Unit = {}
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF050A30)) // Dark Navy
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Activities",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Build, personalize, and optimize your itineraries with our trip planner.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.9f)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onAddClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0D6EFD), // Blue
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Add Activities",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
fun HotelsCard(
    modifier: Modifier = Modifier,
    onAddClick: () -> Unit = {}
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE6F2FF)) // Light Blue
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Hotels",
                style = MaterialTheme.typography.titleLarge,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Build, personalize, and optimize your itineraries with our trip planner.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onAddClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0D6EFD), // Blue
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Add Hotels",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
fun FlightsCard(
    modifier: Modifier = Modifier,
    onAddClick: () -> Unit = {}
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0D6EFD)) // Blue
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Flights",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Build, personalize, and optimize your itineraries with our trip planner.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onAddClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF0D6EFD) // Blue text
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Add Flights",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
fun CardHeader(title: String, icon: Painter, iconTint: Color, textColor: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = TripDimens.md)
    ) {
        Icon(
            painter = icon,
            contentDescription = title,
            tint = iconTint,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(TripDimens.sm))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = textColor
        )
    }
}

@Composable
fun RemoveAction(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFEF2F2))
            .clickable(onClick = onClick)
            .padding(vertical = TripDimens.md),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Remove",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = TripColors.Danger
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Remove",
                tint = TripColors.Danger,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun ActionLinks(
    vararg actions: String,
    onClick: (String) -> Unit = {}
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        actions.forEach {
            Text(
                text = it,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color(0xFF2563EB)
                ),
                modifier = Modifier.clickable { onClick(it) }
            )
        }
    }
}

@Composable
fun GlassOverlay(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    Box(
        modifier = modifier // Apply caller's modifier here
            .clip(RoundedCornerShape(8.dp))
            .background(Color.Black.copy(alpha = 0.1f))
            .border(1.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
            .background(TripColors.GlassOverlay)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(32.dp),
            verticalAlignment = Alignment.CenterVertically,
            content = content
        )
    }
}

@Composable
fun FlightCard(
    flight: FlightDto,
    modifier: Modifier = Modifier,
    onRemoveClick: () -> Unit = {}
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color(0xFFF3F4F6),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(TripDimens.mdsm)) {

            CardHeader(
                title = "Flights",
                icon = painterResource(R.drawable.ic_plane_in_flight),
                iconTint = Color(0xFF1F2937),
                textColor = Color(0xFF1F2937)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.padding(TripDimens.md)) {

                    // Airline Info
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Flight,
                            contentDescription = flight.airline,
                            tint = Color(0xFF0078D4),
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(TripDimens.sm))
                        Column {
                            Text(
                                text = flight.airline,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = Color.Black
                                )
                            )
                            Text(
                                text = flight.flightNumber,
                                style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(TripDimens.lg))

                    // Flight Times and Route
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = flight.departureTime,
                                style = MaterialTheme.typography.titleLarge
                            )
                            Text(
                                text = "Sun, 20 Aug",
                                style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                            )
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 8.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(
                                    Icons.Default.FlightTakeoff,
                                    null,
                                    tint = Color.Gray,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    flight.duration,
                                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                                )
                                Icon(
                                    Icons.Default.FlightLand,
                                    null,
                                    tint = Color.Gray,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(TripDimens.xs))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .background(Color(0xFFE5E7EB), RoundedCornerShape(3.dp))
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(0.5f)
                                        .height(6.dp)
                                        .background(
                                            TripColors.PrimaryBlue,
                                            RoundedCornerShape(3.dp)
                                        )
                                        .align(Alignment.Center)
                                )
                            }
                            Spacer(modifier = Modifier.height(TripDimens.xs))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(flight.from, style = MaterialTheme.typography.titleMedium)
                                Text(
                                    flight.stops,
                                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                                )
                                Text(flight.to, style = MaterialTheme.typography.titleMedium)
                            }
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = flight.arrivalTime,
                                style = MaterialTheme.typography.titleLarge
                            )
                            Text(
                                text = "Sun, 20 Aug",
                                style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(TripDimens.md))
                    HorizontalDivider(color = TripColors.Divider)
                    Spacer(modifier = Modifier.height(TripDimens.md))

                    ActionLinks("Flight details", "Price details", "Edit details")
                    Spacer(modifier = Modifier.height(TripDimens.md))
                    HorizontalDivider(color = TripColors.Divider)
                    Spacer(modifier = Modifier.height(TripDimens.md))

                    Text(
                        text = formatPrice(flight.price),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(TripDimens.md))
            RemoveAction(onRemoveClick)
        }
    }
}


@Composable
fun HotelCard(
    hotel: HotelDto,
    modifier: Modifier = Modifier,
    onRemoveClick: () -> Unit = {}
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color(0xFF344054),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(TripDimens.mdsm)) {

            CardHeader(
                title = "Hotels",
                icon = painterResource(R.drawable.ic_building),
                iconTint = Color.White,
                textColor = Color.White
            )

            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(0.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(12.dp)
                            .clip(RoundedCornerShape(8.dp))
                    ) {
                        AsyncImage(
                            model = hotel.imageUrl,
                            contentDescription = hotel.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                        GlassOverlay(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 20.dp)
                        ) {
                            // Previous / Next arrows
                            listOf(
                                Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                Icons.AutoMirrored.Filled.KeyboardArrowRight
                            ).forEach { icon ->
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .background(Color.White.copy(alpha = 0.9f), CircleShape)
                                        .clickable { },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        icon,
                                        contentDescription = null,
                                        tint = Color.Black,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }

                    Column(modifier = Modifier.padding(horizontal = TripDimens.md)) {
                        Text(hotel.name, style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(TripDimens.sm))
                        Text(
                            hotel.address,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color(0xFF4B5563)
                            )
                        )
                        Spacer(modifier = Modifier.height(TripDimens.md))

                        // Info row
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                Icons.Outlined.Place,
                                null,
                                tint = Color(0xFF2563EB),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                "Show in map",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFF2563EB)
                                )
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Icon(
                                Icons.Default.Star,
                                null,
                                tint = Color(0xFFF59E0B),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                "${hotel.rating} (436)",
                                style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Icon(
                                Icons.Default.Bed,
                                null,
                                tint = Color.Gray,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                hotel.roomType,
                                style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                            )
                        }

                        Spacer(modifier = Modifier.height(TripDimens.md))
                        HorizontalDivider(color = TripColors.Divider)
                        Spacer(modifier = Modifier.height(TripDimens.md))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.CalendarToday,
                                null,
                                tint = Color(0xFF6B7280),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "In: ${hotel.checkIn}",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = Color(0xFF6B7280)
                                )
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Icon(
                                Icons.Default.CalendarToday,
                                null,
                                tint = Color(0xFF6B7280),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Out: ${hotel.checkOut}",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = Color(0xFF6B7280)
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(TripDimens.md))
                        HorizontalDivider(color = TripColors.Divider)
                        Spacer(modifier = Modifier.height(TripDimens.md))

                        ActionLinks("Hotel details", "Price details", "Edit details")
                        Spacer(modifier = Modifier.height(TripDimens.md))
                        HorizontalDivider(color = TripColors.Divider)
                        Spacer(modifier = Modifier.height(TripDimens.md))

                        Text(formatPrice(hotel.price), style = MaterialTheme.typography.titleMedium)
                    }

                    Spacer(modifier = Modifier.height(TripDimens.md))
                    RemoveAction(onRemoveClick)
                }
            }
        }
    }
}


@Composable
fun ActivityCard(
    activity: ActivityDto,
    modifier: Modifier = Modifier,
    onRemoveClick: () -> Unit = {}
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = TripColors.PrimaryBlue,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(TripDimens.mdsm)) {

            CardHeader(
                title = "Activities",
                icon = painterResource(R.drawable.ic_road),
                iconTint = Color.White,
                textColor = Color.White
            )

            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(0.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(12.dp)
                            .clip(RoundedCornerShape(8.dp))
                    ) {
                        AsyncImage(
                            model = activity.imageUrl,
                            contentDescription = activity.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                        GlassOverlay(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 20.dp)
                        ) {
                            // Previous / Next arrows
                            listOf(
                                Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                Icons.AutoMirrored.Filled.KeyboardArrowRight
                            ).forEach { icon ->
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .background(Color.White.copy(alpha = 0.9f), CircleShape)
                                        .clickable { },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        icon,
                                        contentDescription = null,
                                        tint = Color.Black,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }

                    Column(modifier = Modifier.padding(horizontal = TripDimens.md)) {
                        Text(activity.name, style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(TripDimens.sm))
                        Text(
                            activity.description,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color(0xFF4B5563)
                            )
                        )
                        Spacer(modifier = Modifier.height(TripDimens.md))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                Icons.Outlined.Place,
                                null,
                                tint = Color(0xFF2563EB),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                activity.location,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFF2563EB)
                                )
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Icon(
                                Icons.Default.Star,
                                null,
                                tint = Color(0xFFF59E0B),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                "${activity.rating} (436)",
                                style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Icon(
                                Icons.Outlined.Flag,
                                null,
                                tint = Color.Gray,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                activity.duration,
                                style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                            )
                        }

                        Spacer(modifier = Modifier.height(TripDimens.md))
                        HorizontalDivider(color = TripColors.Divider)
                        Spacer(modifier = Modifier.height(TripDimens.md))

                        ActionLinks("Hotel details", "Price details", "Edit details")
                        Spacer(modifier = Modifier.height(TripDimens.md))
                        HorizontalDivider(color = TripColors.Divider)
                        Spacer(modifier = Modifier.height(TripDimens.md))

                        Text(
                            formatPrice(activity.price),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    Spacer(modifier = Modifier.height(TripDimens.md))
                    RemoveAction(onRemoveClick)
                }
            }
        }
    }
}

@Composable
fun FlightEmptyState(
    modifier: Modifier = Modifier,
    onAddFlightClick: () -> Unit = {}
) {
    // Parent Container with Rounded Corners and Light Gray Background
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color(0xFFF3F4F6), // Light gray background
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            // Header: Icon and Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_plane_in_flight),
                    contentDescription = "Flights",
                    tint = Color(0xFF1F2937), // Dark gray
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Flights",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF1F2937)
                )
            }

            // Inner White Card
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp, horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Illustration Placeholder (Circle with Icon)
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .background(Color(0xFFE0F2FE), CircleShape), // Light blue circle
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.FlightTakeoff,
                            contentDescription = null,
                            tint = Color(0xFF3B82F6), // Blue
                            modifier = Modifier.size(64.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "No request yet",
                        style = MaterialTheme.typography.bodySmall, // Medium, 12sp (Per user instruction for Medium)
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = onAddFlightClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF0D6EFD) // Blue
                        )
                    ) {
                        Text(
                            text = "Add Flight",
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HotelEmptyState(
    modifier: Modifier = Modifier,
    onAddHotelClick: () -> Unit = {}
) {
    // Parent Container with Rounded Corners and Dark Background
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color(0xFF344054), // Dark blue-gray
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            // Header: Icon and Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_building), // Placeholder for Hotel = , // Placeholder for Hotel
                    contentDescription = "Hotels",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Hotels",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
            }

            // Inner White Card
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp, horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Illustration Placeholder (Circle with Icon)
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .background(Color(0xFFE0F2FE), CircleShape), // Light blue circle
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Apartment,
                            contentDescription = null,
                            tint = Color(0xFF3B82F6), // Blue
                            modifier = Modifier.size(64.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "No request yet",
                        style = MaterialTheme.typography.bodySmall, // Medium, 12sp
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = onAddHotelClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF0D6EFD) // Blue
                        )
                    ) {
                        Text(
                            text = "Add Hotel",
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ActivitiesEmptyState(
    modifier: Modifier = Modifier,
    onAddActivityClick: () -> Unit = {}
) {
    // Parent Container with Rounded Corners and Blue Background
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color(0xFF0D6EFD), // Primary Blue
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            // Header: Icon and Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_road), // Placeholder for Activities
                    contentDescription = "Activities",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Activities",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
            }

            // Inner White Card
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp, horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Illustration Placeholder (Circle with Icon)
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .background(Color(0xFFE0F2FE), CircleShape), // Light blue circle
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalActivity,
                            contentDescription = null,
                            tint = Color(0xFF3B82F6), // Blue
                            modifier = Modifier.size(64.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "No request yet",
                        style = MaterialTheme.typography.bodySmall, // Medium, 12sp
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = onAddActivityClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF0D6EFD) // Blue
                        )
                    ) {
                        Text(
                            text = "Add Activity",
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    }
}

fun formatPrice(amount: Double): String {
    val format = NumberFormat.getCurrencyInstance(Locale("en", "NG"))
    val currency = format.format(amount).replace("NGN", "₦")
    return if (currency.contains("₦")) currency else "₦ $currency"
}

//fun formatDateRange(input: String, end: String? = null): String {
//    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
//    val outputFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH)
//
//    val startFormatted = LocalDate.parse(input, inputFormatter).format(outputFormatter)
//
//    return if (end != null) {
//        val endFormatted = LocalDate.parse(end, inputFormatter).format(outputFormatter)
//        "$startFormatted \u2192 $endFormatted"
//    } else {
//        startFormatted
//    }
//}


//@Preview(showBackground = true)
//@Composable
//fun FlightEmptyStatePreview() {
//    TripDetailScreen()
//}

//@Preview(showBackground = true)
//@Composable
//fun FlightEmptyStatePreview() {
//    Box(modifier = Modifier.padding(16.dp)) {
//        FlightEmptyState()
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun HotelEmptyStatePreview() {
//    Box(modifier = Modifier.padding(16.dp)) {
//        HotelEmptyState()
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun ActivitiesEmptyStatePreview() {
//    Box(modifier = Modifier.padding(16.dp)) {
//        ActivitiesEmptyState()
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun FlightCardPreview() {
//    val dummyFlight = FlightDto(
//        id = "1",
//        airline = "American Airlines",
//        flightNumber = "AA-829",
//        departureTime = "08:35",
//        arrivalTime = "09:55",
//        duration = "1h 45m",
//        from = "LOS",
//        to = "SIN",
//        stops = "Direct",
//        price = 123450.00
//    )
//    Box(modifier = Modifier.padding(16.dp)) {
//        FlightCard(flight = dummyFlight)
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun HotelCardPreview() {
//    val dummyHotel = HotelDto(
//        id = "1",
//        name = "Riviera Resort, Lekki",
//        address = "18, Kenneth Agbakuru Street, Off Access Bank Admiralty Way, Lekki Phase1",
//        imageUrl = "https://example.com/image.jpg",
//        rating = 8.5,
//        roomType = "King size room",
//        checkIn = "20-04-2024",
//        checkOut = "29-04-2024",
//        price = 123450.00
//    )
//    Box(modifier = Modifier.padding(16.dp)) {
//        HotelCard(hotel = dummyHotel)
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun ActivityCardPreview() {
//    val dummyActivity = ActivityDto(
//        id = "1",
//        name = "The Museum of Modern Art",
//        description = "Works from Van Gogh to Warhol & beyond plus a sculpture garden, 2 cafes & The modern restaurant",
//        location = "Melbourne, Austraila",
//        rating = 8.5,
//        duration = "1 hour",
//        time = "10:30 AM",
//        date = "Mar 19",
//        imageUrl = "https://example.com/image.jpg",
//        price = 123450.00
//    )
//    Box(modifier = Modifier.padding(16.dp)) {
//        ActivityCard(activity = dummyActivity)
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun DesignPreview() {
//    Column(modifier = Modifier.padding(16.dp)) {
//        TripDetailsHeader()
//        Spacer(modifier = Modifier.height(16.dp))
//        ActivitiesCard()
//        Spacer(modifier = Modifier.height(16.dp))
//        HotelsCard()
//        Spacer(modifier = Modifier.height(16.dp))
//        FlightsCard()
//    }
//}

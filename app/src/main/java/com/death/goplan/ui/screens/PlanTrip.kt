package com.death.goplan.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.death.goplan.R
import com.death.goplan.data.model.TripDto
import com.death.goplan.ui.component.CreateTripCard
import com.death.goplan.ui.component.CreateTripStatusDialog
import com.death.goplan.ui.component.TripListItem
import com.death.goplan.utils.ApiResult
import com.death.goplan.ui.viewmodel.CreateTripUiState
import com.death.goplan.ui.viewmodel.TripViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanTripScreen(
    navController: NavController? = null,
    viewModel: TripViewModel,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
) {
    var showCreateTrip by remember { mutableStateOf(false) }

    val uiState by viewModel.uiState.collectAsState()
    val tripsState by viewModel.allTripsState.collectAsState()
    val context = LocalContext.current

    // Dropdown state
    var expanded by remember { mutableStateOf(false) }
    var selectedStyle by remember { mutableStateOf("All") }
    val travelStyles = listOf("All", "Solo", "Couple", "Family", "Group")

    // Filter trips based on selected style
    val filteredTrips = when (tripsState) {
        is ApiResult.Success -> {
            val trips = (tripsState as ApiResult.Success<List<TripDto>>).data
            if (selectedStyle == "All") trips else trips.filter { it.style == selectedStyle }
        }
        else -> emptyList()
    }

    LaunchedEffect(Unit) {
        viewModel.getAllTrips() // Load trips on screen start
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color(0xFFF9FAFB),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Plan a Trip",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Black
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF9FAFB),
                    titleContentColor = Color.Black
                ),
                navigationIcon = {
                    // Optional: add back button if needed later
                    // IconButton(onClick = onBackClick) {
                    //     Icon(
                    //         imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    //         contentDescription = "Back",
                    //         tint = Color.Black
                    //     )
                    // }
                }
            )
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Header Section
            item {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp)
                    ) {
                        AsyncImage(
                            model = R.drawable.trip_body_one,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text = "Plan Your Dream Trip in Minutes",
                                style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Build, personalize, and optimize your itineraries with our trip planner.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.DarkGray,
                                lineHeight = 20.sp
                            )
                        }
                    }

                    CreateTripCard(
                        uiState = uiState,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(start = 16.dp, end = 16.dp, bottom = 24.dp),
                        onSelectCityClick = { navController?.navigate("whereTo") },
                        onStartDateClick = { navController?.navigate("date") },
                        onEndDateClick = { navController?.navigate("date") },
                        onCreateTripClick = {
                            if (uiState.cityName.isNotEmpty() && uiState.startDate != null && uiState.endDate != null) {
                                showCreateTrip = true
                            }else{
                                Toast.makeText(
                                    context,
                                    "Fields cannot be empty",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    )
                }
            }

            // Trips Section
            item {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                ) {
                    Text(
                        text = "Your Trips",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Black
                    )
                    Text(
                        text = "Your trip itineraries and planned trips are placed here",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Dropdown for Travel Style
                    Surface(
                        modifier = modifier.fillMaxWidth(),
                        color = Color(0xFFF3F4F6), // Light gray background
                        shape = RoundedCornerShape(12.dp)
                    ){
                        Box(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { expanded = true }
                                    .background(Color.White, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 12.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    selectedStyle,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                                )
                                Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Dropdown", tint = Color.Gray)
                            }

                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                                modifier = Modifier
                                    .background(Color.White)
                                    .fillMaxWidth(0.9f)
                            ) {
                                travelStyles.forEach { style ->
                                    val isSelected = selectedStyle == style

                                    DropdownMenuItem(
                                        onClick = {
                                            selectedStyle = style
                                            expanded = false
                                        },
                                        text = {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(38.dp)
                                                    .background(if (isSelected) Color(0xFF0D6EFD) else Color.Transparent)
                                                    .padding(horizontal = 16.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = style,
                                                    color = if (isSelected) Color.White else Color.Black,
                                                    style = MaterialTheme.typography.bodyMedium
                                                )
                                                if (isSelected) {
                                                    Icon(
                                                        painter = painterResource(id = R.drawable.ic_check), // your check icon
                                                        contentDescription = null,
                                                        tint = Color.White,
                                                        modifier = Modifier.size(16.dp)
                                                    )
                                                }
                                            }
                                        },
                                        contentPadding = PaddingValues(0.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Trips content based on filteredTrips
                    when (tripsState) {
                        is ApiResult.Loading -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(color = Color(0xFF0D6EFD))
                            }
                        }

                        is ApiResult.Error -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp),
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
                                        text = "Unable to fetch trips. Please try again.",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.Gray,
                                        textAlign = TextAlign.Center
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Button(
                                        onClick = { viewModel.retryFetchTrips() },
                                        colors = ButtonDefaults.buttonColors(containerColor =  Color(0xFF0D6EFD))
                                    ) {
                                        Text(text = "Retry", color = Color.White)
                                    }
                                }
                            }
                        }

                        is ApiResult.Success -> {
                            if (filteredTrips.isEmpty()) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.empty_box),
                                            contentDescription = "No trips",
                                            modifier = Modifier.size(60.dp)
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = "No trips found",
                                            style = MaterialTheme.typography.titleMedium,
                                            color = Color.Gray,
                                            textAlign = TextAlign.Center
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Try a different travel style or create a new trip!",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = Color.Gray,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            } else {
                                Column {
                                    filteredTrips.forEach { trip ->
                                        TripListItem(
                                            trip,
                                            onViewClick = { tripId ->
                                                viewModel.setSelectedTripId(tripId)
                                                navController?.navigate("detail")
                                            }
                                        )
                                        Spacer(modifier = Modifier.height(12.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Create Trip Modal
        if (showCreateTrip) {
            ModalBottomSheet(
                onDismissRequest = { showCreateTrip = false },
                containerColor = Color.White,
                shape = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp),
                tonalElevation = 8.dp,
                dragHandle = {},
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .imePadding()
                        .navigationBarsPadding()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CreateTripFormScreen(
                        viewModel = viewModel,
                        onNextClick = { viewModel.createTrip() },
                        onCloseClick = { showCreateTrip = false }
                    )
                }
            }
        }

        val createTripUiState by viewModel.createTripUiState.collectAsState()
        if (createTripUiState != CreateTripUiState.Idle) {
            CreateTripStatusDialog(
                state = createTripUiState,
                onSuccessComplete = {
                    showCreateTrip = false
                    viewModel.resetCreateTripFlow()
                }
            )
        }
    }
}


fun calculateDaysBetween(startDate: String, endDate: String): Int {
    return try {
        val formatter = SimpleDateFormat("d MMM yyyy", Locale.ENGLISH)

        fun clean(date: String): Date {
            val cleaned = date.replace(
                Regex("(\\d+)(st|nd|rd|th)"),
                "$1"
            )
            return formatter.parse(cleaned)!!
        }

        val start = clean(startDate)
        val end = clean(endDate)

        val diffMillis = end.time - start.time
        (diffMillis / (1000 * 60 * 60 * 24)).toInt() + 1 // inclusive

    } catch (e: Exception) {
        0
    }
}


//@Preview(showBackground = true)
//@Composable
//fun PlanTripScreenPreview() {
//    PlanTripScreen()
//}

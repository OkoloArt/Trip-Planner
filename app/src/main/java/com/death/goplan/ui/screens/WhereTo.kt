package com.death.goplan.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.death.goplan.R
import com.death.goplan.ui.component.LocationItem
import com.death.goplan.viewmodel.TripViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WhereToScreen(
    navigation: NavController? = null,
    viewModel: TripViewModel,
    modifier: Modifier = Modifier,
    onCloseClick: () -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }

    val cities = listOf(
        LocationData("Laghouat, Algeria", "Laghouat", "DZ", R.drawable.algeria),
        LocationData("Lagos, Nigeria", "Murtala Muhammed", "NG", R.drawable.nigeria),
        LocationData("Doha, Qatar", "Hamad International", "QA", R.drawable.qatar),
        LocationData("Paris, France", "Charles de Gaulle", "FR", R.drawable.france),
        LocationData("Dubai, UAE", "Dubai International", "AE", R.drawable.united_arab),
        LocationData("London, United Kingdom", "Heathrow", "GB",R.drawable.united_kingdom)
    )

    val filteredCities = cities.filter {
        it.cityName.contains(searchQuery, ignoreCase = true) ||
                it.airportName.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color.White,

        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            text = "Where",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Black
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onCloseClick) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = Color.Black
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Divider(
                    color = Color(0xFFF3F4F6),
                    thickness = 1.dp
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // Search Section
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Please select a city",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF6B7280),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { 
                        Text(
                            text = "Search city",
                            style = MaterialTheme.typography.bodyMedium
                        ) 
                    },
                    textStyle = MaterialTheme.typography.bodyMedium,
                    shape = RoundedCornerShape(4.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color(0xFF3B82F6),
                        unfocusedIndicatorColor = Color(0xFFD1D5DB)
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // City List
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp)
            ) {
                items(filteredCities) { location ->
                    LocationItem(
                        locationData = location,
                        onClick = {
                            // Save selected city in ViewModel
                            viewModel.updateSelectedCity(
                                cityName = location.cityName,
                                airportName = location.airportName,
                                countryCode = location.countryCode
                            )
                            searchQuery = location.cityName // Update search query to show selected city
                            // Optionally navigate back
                           // navigation?.popBackStack()
                        }
                    )
                }
            }
        }
    }
}

data class LocationData(
    val cityName: String,
    val airportName: String,
    val countryCode: String,
    val countryFlag: Int
)

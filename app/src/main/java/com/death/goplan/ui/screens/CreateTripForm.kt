package com.death.goplan.ui.screens

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
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
import com.death.goplan.R
import com.death.goplan.ui.theme.TripDimens
import com.death.goplan.ui.viewmodel.TripViewModel

@Composable
fun CreateTripFormScreen(
    viewModel: TripViewModel,
    modifier: Modifier = Modifier,
    onCloseClick: () -> Unit = {},
    onNextClick: () -> Unit = {}
) {
    var isDropdownExpanded by remember { mutableStateOf(false) }
    val uiState by viewModel.uiState.collectAsState()
    val isNextEnabled =
        uiState.tripName.isNotBlank() && uiState.travelStyle.isNotBlank()

    val travelStyles = listOf("Solo", "Couple", "Family", "Group")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(TripDimens.mdsm)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            // Icon Box
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color(0xFFEFF6FF), RoundedCornerShape(8.dp)), // Light Blue bg
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_tree),
                    contentDescription = "Icon",
                    tint = Color(0xFF3B82F6), // Blue
                    modifier = Modifier.size(24.dp)
                )
            }

            // Close Button
            IconButton(onClick = onCloseClick) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Titles
        Text(
            text = "Create a Trip",
            style = MaterialTheme.typography.titleLarge, // Satoshi Bold, 16sp
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Let's Go! Build Your Next Adventure",
            style = MaterialTheme.typography.bodyMedium, // Satoshi Regular, 14sp
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Trip Name
        Text(
            text = "Trip Name",
            style = MaterialTheme.typography.bodySmall, // Satoshi Medium, 12sp
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = uiState.tripName,
            onValueChange = {
                viewModel.updateTripDetails(
                    tripName = it,
                    travelStyle = uiState.travelStyle,
                    tripDescription = uiState.tripDescription
                )
            },
            textStyle = MaterialTheme.typography.bodyMedium, // Satoshi Regular, 14sp
            placeholder = { 
                Text(
                    "Enter the trip name", 
                    color = Color(0xFF9CA3AF),
                    style = MaterialTheme.typography.bodyMedium
                ) 
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFFE5E7EB),
                focusedBorderColor = Color(0xFF3B82F6),
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Travel Style Dropdown
        Text(
            text = "Travel Style",
            style = MaterialTheme.typography.bodySmall, // Satoshi Medium, 12sp
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = uiState.travelStyle,
                onValueChange = {},
                readOnly = true,
                textStyle = MaterialTheme.typography.bodyMedium, // Satoshi Regular, 14sp
                placeholder = { 
                    Text(
                        "Select your travel style", 
                        color = Color(0xFF9CA3AF),
                        style = MaterialTheme.typography.bodyMedium
                    ) 
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.ExpandMore,
                        contentDescription = "Dropdown",
                        tint = Color.Gray
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color(0xFFE5E7EB),
                    focusedBorderColor = Color(0xFF3B82F6),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            // Transparent box to capture clicks
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable { isDropdownExpanded = true }
            )

            DropdownMenu(
                expanded = isDropdownExpanded,
                onDismissRequest = { isDropdownExpanded = false },
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxWidth(0.9f)
            ) {
                travelStyles.forEach { style ->
                    val isSelected = uiState.travelStyle == style

                    DropdownMenuItem(
                        onClick = {
                            viewModel.updateTripDetails(
                                tripName = uiState.tripName,
                                travelStyle = style,
                                tripDescription = uiState.tripDescription
                            )
                            isDropdownExpanded = false
                        },
                        text = {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(38.dp)
                                    .background(
                                        if (isSelected) Color(0xFF0D6EFD) else Color.Transparent
                                    )
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
                                        painter = painterResource(id = R.drawable.ic_check),
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

        Spacer(modifier = Modifier.height(20.dp))

        // Trip Description
        Text(
            text = "Trip Description",
            style = MaterialTheme.typography.bodySmall, // Satoshi Medium, 12sp
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = uiState.tripDescription,
            onValueChange = {
                viewModel.updateTripDetails(
                    tripName = uiState.tripName,
                    travelStyle = uiState.travelStyle,
                    tripDescription = it
                )
            },
            textStyle = MaterialTheme.typography.bodyMedium, // Satoshi Regular, 14sp
            placeholder = { 
                Text(
                    "Tell us more about the trip", 
                    color = Color(0xFF9CA3AF),
                    style = MaterialTheme.typography.bodyMedium
                ) 
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFFE5E7EB),
                focusedBorderColor = Color(0xFF3B82F6),
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            ),
            minLines = 4
        )

        Spacer(modifier = Modifier.weight(1f))

        // Next Button
        Button(
            onClick = onNextClick,
            enabled = isNextEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isNextEnabled) Color(0xFF3B82F6) else Color(0xFFDBEAFE),
                contentColor = if (isNextEnabled) Color.White else Color(0xFF6B7280)
            ),
            elevation = ButtonDefaults.buttonElevation(0.dp)
        ) {
            Text(
                text = "Next",
                style = MaterialTheme.typography.titleMedium // Satoshi Bold, 16sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

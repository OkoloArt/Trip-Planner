package com.death.goplan.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.death.goplan.R
import com.death.goplan.ui.screens.LocationData

@Composable
fun LocationItem(
    modifier: Modifier = Modifier,
    locationData: LocationData,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 16.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Location Icon
        Icon(
            painter = painterResource(id = R.drawable.ic_location_filled),
            contentDescription = "Location Pin",
            tint = Color(0xFF6B7280),
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        // City and Airport Name
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = locationData.cityName,
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF111827)
            )
            Text(
                text = locationData.airportName,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF6B7280)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Flag and Country Code
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = locationData.countryFlag),
                contentDescription = "${locationData.countryCode} flag",
                modifier = Modifier.size(24.dp)
            )

            Text(
                text = locationData.countryCode,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF6B7280)
            )
        }
    }
}

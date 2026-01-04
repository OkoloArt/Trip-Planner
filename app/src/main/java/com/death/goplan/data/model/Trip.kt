package com.death.goplan.data.model

import kotlinx.serialization.Serializable

@Serializable
data class TripDto(
    val id: String? = null,
    val destination: String? = null,
    val startDate: String? = null,
    val endDate: String? = null,
    val name: String,
    val style: String,
    val description: String,
    val totalPrice: Double? = null,
    val currency: String? = null,
    val imageUrl: String =
        "https://images.unsplash.com/photo-1477959858617-67f85cf4f1df?q=80&w=2613&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
)

data class FlightDto(
    val id: String,
    val airline: String,
    val flightNumber: String,
    val departureTime: String,
    val arrivalTime: String,
    val duration: String,
    val from: String,
    val to: String,
    val stops: String,
    val price: Double
)

data class HotelDto(
    val id: String,
    val name: String,
    val address: String,
    val imageUrl: String,
    val rating: Double,
    val roomType: String,
    val checkIn: String,
    val checkOut: String,
    val price: Double
)

data class ActivityDto(
    val id: String,
    val name: String,
    val description: String,
    val location: String,
    val rating: Double,
    val duration: String,
    val time: String,
    val date: String,
    val imageUrl: String,
    val price: Double
)

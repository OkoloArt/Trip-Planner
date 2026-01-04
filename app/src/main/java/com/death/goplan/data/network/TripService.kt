package com.death.goplan.data.network

import com.death.goplan.data.model.TripDto
import com.death.goplan.utils.ApiException
import com.death.goplan.utils.Constants
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import javax.inject.Inject
import javax.inject.Named

class TripService @Inject constructor(@param:Named("MainClient") private val client: HttpClient) {

    // Get all trips
    suspend fun getAllTrips(): List<TripDto> {
        val response: HttpResponse = client.get("${Constants.BASE_URL}/api/trips") {
            contentType(ContentType.Application.Json)
        }

        return if (response.status.isSuccess()) {
            response.body<List<TripDto>>()
        } else {
            throw ApiException(
                statusCode = response.status.value,
                message = "Error fetching trips list"
            )
        }
    }

    // Get a single trip by ID
    suspend fun getTripById(tripId: String): TripDto {
        val response: HttpResponse = client.get("${Constants.BASE_URL}/api/trips/${tripId}") {
            contentType(ContentType.Application.Json)
        }

        return if (response.status.isSuccess()) {
            response.body<TripDto>()
        } else {
            throw ApiException(
                statusCode = response.status.value,
                message = "Error fetching trip by ID"
            )
        }
    }

    // Create a new trip
    suspend fun createTrip(trip: TripDto): TripDto {
        val response: HttpResponse = client.post("${Constants.BASE_URL}/api/trips") {
            contentType(ContentType.Application.Json)
            setBody(trip)
        }

        return if (response.status.isSuccess()) {
            response.body<TripDto>()
        } else {
            throw ApiException(
                statusCode = response.status.value,
                message = "Error creating trip"
            )
        }
    }
}

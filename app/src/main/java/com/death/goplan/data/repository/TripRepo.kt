package com.death.goplan.data.repository

import com.death.goplan.data.model.TripDto
import com.death.goplan.data.network.TripService
import com.death.goplan.utils.ApiResult
import javax.inject.Inject

interface TripRepo {
    suspend fun getAllTrips(): ApiResult<List<TripDto>>

    suspend fun getTripById(tripId: String): ApiResult<TripDto>

    suspend fun createTrip(data: TripDto) : ApiResult<TripDto>
}

class TripRepoImpl @Inject constructor(private val tripService: TripService): TripRepo {
    override suspend fun getAllTrips(): ApiResult<List<TripDto>> {
        return try {
            val response = tripService.getAllTrips()
            ApiResult.Success(response)
        }catch (e : Exception){
            ApiResult.Error(e)
        }
    }

    override suspend fun getTripById(tripId: String): ApiResult<TripDto> {
        return try {
            val trip = tripService.getTripById(tripId)
            ApiResult.Success(trip)
        }catch (e : Exception){
            ApiResult.Error(e)
        }
    }

    override suspend fun createTrip(data: TripDto): ApiResult<TripDto> {
        return try {
            val createdTrip = tripService.createTrip(data)
            ApiResult.Success(createdTrip)
        } catch (e: Exception) {
            ApiResult.Error(e)
        }
    }
}
package com.death.goplan.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.death.goplan.data.model.TripDto
import com.death.goplan.data.repository.TripRepo
import com.death.goplan.utils.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TripUiState(
    val cityName: String = "",
    val airportName: String = "",
    val countryCode: String = "",
    val startDate: String? = null,
    val endDate: String? = null,
    val tripName: String = "",
    val travelStyle: String = "",
    val tripDescription: String = "",
    val selectedTripId: String? = null,
)

sealed interface CreateTripUiState {
    object Idle : CreateTripUiState
    object Loading : CreateTripUiState
    data class Success(val trip: TripDto) : CreateTripUiState
    data class Error(val message: String) : CreateTripUiState
}

@HiltViewModel
class TripViewModel @Inject constructor(
    private val tripRepo: TripRepo
) : ViewModel() {

    // UI state for form
    private val _uiState = MutableStateFlow(TripUiState())
    val uiState: StateFlow<TripUiState> = _uiState.asStateFlow()

    // API state flows
    private val _allTripsState = MutableStateFlow<ApiResult<List<TripDto>>>(ApiResult.Loading)
    val allTripsState: StateFlow<ApiResult<List<TripDto>>> = _allTripsState.asStateFlow()

    private val _tripDetailState = MutableStateFlow<ApiResult<TripDto>>(ApiResult.Loading)
    val tripDetailState: StateFlow<ApiResult<TripDto>> = _tripDetailState.asStateFlow()

//    private val _createTripState = MutableStateFlow<ApiResult<TripDto>>(ApiResult.Loading)
//    val createTripState: StateFlow<ApiResult<TripDto>> = _createTripState.asStateFlow()

    private val _createTripUiState =
        MutableStateFlow<CreateTripUiState>(CreateTripUiState.Idle)
    val createTripUiState: StateFlow<CreateTripUiState> =
        _createTripUiState.asStateFlow()


    fun updateSelectedCity(cityName: String, airportName: String, countryCode: String) {
        _uiState.update { it.copy(cityName = cityName, airportName = airportName, countryCode = countryCode) }
    }

    fun updateDateRange(startDate: String?, endDate: String?) {
        _uiState.update { it.copy(startDate = startDate, endDate = endDate) }
    }

    fun updateTripDetails(tripName: String, travelStyle: String, tripDescription: String) {
        _uiState.update { it.copy(tripName = tripName, travelStyle = travelStyle, tripDescription = tripDescription) }
    }

    fun setSelectedTripId(tripId: String) {
        _uiState.update {
            it.copy(selectedTripId = tripId)
        }
    }

    fun fetchSelectedTripDetails() {
        val tripId = _uiState.value.selectedTripId ?: return
        getTripById(tripId)
    }

    fun resetCreateTripFlow() {
        _uiState.value = TripUiState()
        _createTripUiState.value = CreateTripUiState.Idle
    }

//    init {
//        getAllTrips()
//    }

    fun retryFetchTrips() {
        viewModelScope.launch {
            _allTripsState.value = ApiResult.Loading
            delay(2000)
            getAllTrips()
        }
    }

    fun getAllTrips() {
        viewModelScope.launch {
            _allTripsState.value = ApiResult.Loading
            val result = tripRepo.getAllTrips()
            _allTripsState.value = result
        }
    }

    fun getTripById(tripId: String) {
        viewModelScope.launch {
            _tripDetailState.value = ApiResult.Loading
            val result = tripRepo.getTripById(tripId)
            _tripDetailState.value = result
        }
    }

    fun createTrip() {
        val tripData = TripDto(
            destination = _uiState.value.cityName,
            startDate = _uiState.value.startDate,
            endDate = _uiState.value.endDate,
            name = _uiState.value.tripName,
            style = _uiState.value.travelStyle,
            description = _uiState.value.tripDescription
        )

        viewModelScope.launch {
            _createTripUiState.value = CreateTripUiState.Loading
            delay(2000)
            when (val result = tripRepo.createTrip(tripData)) {
                is ApiResult.Success -> {
                    _createTripUiState.value =
                        CreateTripUiState.Success(result.data)
                    getAllTrips()
                }

                is ApiResult.Error -> {
                    _createTripUiState.value =
                        CreateTripUiState.Error(
                            "Failed to create trip"
                        )
                }

                else -> Unit
            }
        }
    }


//    fun createTrip() {
//        val tripData = TripDto(
//            destination = _uiState.value.cityName,
//            startDate = _uiState.value.startDate,
//            endDate = _uiState.value.endDate,
//            name = _uiState.value.tripName,
//            style = _uiState.value.travelStyle,
//            description = _uiState.value.tripDescription
//        )
//
//        viewModelScope.launch {
//            _createTripState.value = ApiResult.Loading
//            val result = tripRepo.createTrip(tripData)
//            _createTripState.value = result
//        }
//    }
}


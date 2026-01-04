package com.death.goplan.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.death.goplan.ui.screens.PlanTripScreen
import com.death.goplan.ui.screens.DateScreen
import com.death.goplan.ui.screens.TripDetailScreen
import com.death.goplan.ui.screens.WhereToScreen
import com.death.goplan.viewmodel.TripViewModel

@Composable
fun NavGraph() {
    val navController = rememberNavController()
   val viewModel: TripViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = Screen.PlanTrip.route) {
        composable(Screen.PlanTrip.route) {
            PlanTripScreen(navController, viewModel )
        }

        composable(Screen.WhereTo.route) {
            WhereToScreen(navController, viewModel)
        }

        composable(Screen.Date.route) {
            DateScreen(navController, viewModel, onChooseDateClick = {
                navController.popBackStack()
            })
        }

        composable(Screen.Detail.route) {
            TripDetailScreen(navController, viewModel)
        }
    }
}
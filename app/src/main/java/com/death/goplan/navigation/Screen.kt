package com.death.goplan.navigation

sealed class Screen(val route: String) {
    object PlanTrip : Screen("planTrip")

    object WhereTo : Screen("whereTo")
    object Date : Screen("date")
    object Detail : Screen("detail")
}
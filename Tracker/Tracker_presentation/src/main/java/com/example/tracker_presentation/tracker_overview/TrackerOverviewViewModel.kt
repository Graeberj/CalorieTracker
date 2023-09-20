package com.example.tracker_presentation.tracker_overview

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.preferences.Preferences
import com.example.tracker_domain.use_case.TrackerUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrackerOverviewViewModel @Inject constructor(
    preferences: Preferences,
    private val trackerUseCases: TrackerUseCases
) : ViewModel() {

    var state by mutableStateOf(TrackerOverviewState())
        private set

    private var getFoodsForDateJob: Job? = null

    init {
        refreshFood()
        preferences.saveShouldShowOnboarding(false)
    }

    fun onEvent(event: TrackerOverviewEvent) {
        when (event) {
            is TrackerOverviewEvent.OnDeleteTrackedFoodClicked -> {
                viewModelScope.launch {
                    trackerUseCases.deleteTrackedFood(event.trackedFood)
                    refreshFood()
                }

            }

            TrackerOverviewEvent.OnNextDayClicked -> {
                state = state.copy(
                    date = state.date.plusDays(1)
                )
                refreshFood()
            }

            TrackerOverviewEvent.OnPreviousDayClicked -> {
                state = state.copy(
                    date = state.date.minusDays(1)
                )
                refreshFood()
            }

            is TrackerOverviewEvent.OnToggleMealClicked -> {
                state = state.copy(
                    meals = state.meals.map {
                        if(it.name == event.meal.name){
                            it.copy(isExpanded = !it.isExpanded)
                        } else it
                    }
                )
            }
        }
    }

    private fun refreshFood(){
        getFoodsForDateJob?.cancel()
        getFoodsForDateJob = trackerUseCases
            .getFoodForDate(state.date)
            .onEach { food ->
                val nutrientsResult = trackerUseCases.calculateMealNutrients(food)
                state = state.copy(
                    totalCalories = nutrientsResult.totalCalories,
                    totalCarbs = nutrientsResult.totalCarbs,
                    totalProtein = nutrientsResult.totalProtein,
                    totalFat = nutrientsResult.totalFat,
                    calorieGoal = nutrientsResult.caloriesGoal,
                    carbsGoal = nutrientsResult.carbsGoal,
                    fatGoal = nutrientsResult.fatGoal,
                    proteinGoal = nutrientsResult.proteinGoal,
                    trackedFood = food,
                    meals = state.meals.map {
                        val nutrientsForMeal = nutrientsResult
                            .mealNutrients[it.mealType]
                            ?: return@map it.copy(
                                carbs = 0,
                                protein = 0,
                                fats = 0,
                                calories = 0
                            )
                        it.copy(
                            carbs = nutrientsForMeal.carbs,
                            protein = nutrientsForMeal.protein,
                            fats = nutrientsForMeal.fat,
                            calories = nutrientsForMeal.calories
                        )
                    }
                )
            }.launchIn(viewModelScope)
    }
}
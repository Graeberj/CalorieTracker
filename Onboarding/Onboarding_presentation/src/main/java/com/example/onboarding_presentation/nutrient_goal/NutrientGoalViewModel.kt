package com.example.onboarding_presentation.nutrient_goal

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.preferences.Preferences
import com.example.core.domain.use_case.FilterDigits
import com.example.core.util.UiEvent
import com.example.onboarding_domain.use_case.ValidateNutrients
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NutrientGoalViewModel @Inject constructor(
    private val preferences: Preferences,
    private val filterDigits: FilterDigits,
    private val validateNutrients: ValidateNutrients
) : ViewModel() {

    var state by mutableStateOf(NutrientGoalState())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: NutrientGoalEvent) {
        when(event) {
            is NutrientGoalEvent.OnCarbRatioEntered -> {
                state = state.copy(carbsRatio = filterDigits(event.ratio))
            }
            is NutrientGoalEvent.OnFatRatioEntered -> {
                state = state.copy(fatRatio = filterDigits(event.ratio))
            }
            is NutrientGoalEvent.OnProteinRatioEntered -> {
                state = state.copy(proteinRatio = filterDigits(event.ratio))
            }
            is NutrientGoalEvent.OnNextClicked -> {
                val result = validateNutrients(
                    carbsRatioText = state.carbsRatio,
                    proteinRatioText = state.proteinRatio,
                    fatRatioText = state.fatRatio
                )
                when(result) {
                    is ValidateNutrients.Result.Success -> {
                        preferences.saveCarbRatio(result.carbsRatio)
                        preferences.saveProteinRatio(result.proteinRatio)
                        preferences.saveFatRatio(result.fatRatio)
                        viewModelScope.launch {
                            _uiEvent.send(UiEvent.Success)
                        }
                    }
                    is ValidateNutrients.Result.Error -> {
                        viewModelScope.launch {
                            _uiEvent.send(UiEvent.ShowSnackbar(result.message))
                        }
                    }
                }
            }
        }
    }
}
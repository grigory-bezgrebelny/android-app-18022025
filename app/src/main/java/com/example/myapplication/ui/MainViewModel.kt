package com.example.myapplication.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.R
import com.example.myapplication.core.UiText
import com.example.myapplication.entity.Gender
import com.example.myapplication.entity.RequestResult
import com.example.myapplication.repo.GenderRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(private val genderRepo: GenderRepo) : ViewModel() {

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow: Flow<UiEvent> get() = _eventFlow

    val selectedGenderState: StateFlow<Gender?> = genderRepo
        .getGenderFlow()
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val selectedAgeState: StateFlow<Int?> = genderRepo
        .getAgeFlow()
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private val ageList = genderRepo.getAgeList()
    val ageListState: StateFlow<List<AgeUiItem>> = selectedAgeState
        .map { selectedAge -> ageList.map { mapToUiItem(it, selectedAge) } }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val selectedAgePositionState: StateFlow<Int> = selectedAgeState
        .map { it?.let(ageList::indexOf) ?: 0 }
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    private val nextButtonLoadingState = MutableStateFlow(false)
    val nextButtonEnabledState: StateFlow<Boolean> = combine(
        selectedGenderState,
        selectedAgeState,
        nextButtonLoadingState,
        ::nextButtonEnabled,
    ).stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        nextButtonEnabled(
            selectedGenderState.value,
            selectedAgeState.value,
            nextButtonLoadingState.value
        ),
    )

    private fun nextButtonEnabled(gender: Gender?, age: Int?, loading: Boolean): Boolean {
        return gender != null && age != null && !loading
    }

    private fun mapToUiItem(value: Int, selectedAge: Int?): AgeUiItem {
        return AgeUiItem(
            value = value,
            selected = value == selectedAge,
            onClick = {
                viewModelScope.launch {
                    genderRepo.setAge(value)
                    _eventFlow.emit(UiEvent.DismissPopup)
                }
            },
        )
    }

    fun maleClicked() {
        viewModelScope.launch {
            genderRepo.setGender(Gender.MALE)
        }
    }

    fun femaleClicked() {
        viewModelScope.launch {
            genderRepo.setGender(Gender.FEMALE)
        }
    }

    fun nextClicked() {
        val gender = selectedGenderState.value
        val age = selectedAgeState.value
        if (gender == null || age == null || nextButtonLoadingState.value) return

        nextButtonLoadingState.value = true

        viewModelScope.launch {
            try {
                val message = when (val result = genderRepo.sendMessage(gender, age)) {
                    is RequestResult.Success -> {
                        val res = if (result.value) R.string.allowed else R.string.not_allowed
                        UiText.Res(res)
                    }

                    is RequestResult.Error -> result.value
                }
                _eventFlow.emit(UiEvent.ShowMessage(message))
            } finally {
                nextButtonLoadingState.value = false
            }
        }
    }

    sealed interface UiEvent {
        class ShowMessage(val value: UiText) : UiEvent
        data object DismissPopup : UiEvent
    }
}

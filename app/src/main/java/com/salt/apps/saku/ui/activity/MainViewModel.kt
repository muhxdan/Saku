package com.salt.apps.saku.ui.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.salt.apps.core.domain.model.UserData
import com.salt.apps.core.domain.usecase.SettingUseCase
import com.salt.apps.saku.ui.activity.MainActivityState.Loading
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val settingUseCase: SettingUseCase
) : ViewModel() {
    val mainActivityStateStateFlow: StateFlow<MainActivityState> =
        settingUseCase.userData.map<UserData, MainActivityState>(MainActivityState::Success)
            .stateIn(
                scope = viewModelScope,
                initialValue = Loading,
                started = SharingStarted.WhileSubscribed(5_000),
            )
}

sealed interface MainActivityState {
    data object Loading : MainActivityState
    data class Success(val userData: UserData) : MainActivityState
}

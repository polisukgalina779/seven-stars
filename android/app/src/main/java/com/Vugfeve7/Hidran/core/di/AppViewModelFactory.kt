package com.Vugfeve7.Hidran.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.Vugfeve7.Hidran.domain.repository.ProgressRepository
import com.Vugfeve7.Hidran.domain.repository.SettingsRepository
import com.Vugfeve7.Hidran.domain.usecase.AwardStarsUseCase
import com.Vugfeve7.Hidran.domain.usecase.GrantCollectionCardUseCase
import com.Vugfeve7.Hidran.domain.usecase.SpinReelsUseCase
import com.Vugfeve7.Hidran.domain.usecase.UnlockRoomUseCase
import com.Vugfeve7.Hidran.presentation.app.AppViewModel
import com.Vugfeve7.Hidran.presentation.collection.CollectionViewModel
import com.Vugfeve7.Hidran.presentation.game.GameViewModel
import com.Vugfeve7.Hidran.presentation.levelselect.LevelSelectViewModel
import com.Vugfeve7.Hidran.presentation.menu.MenuViewModel
import com.Vugfeve7.Hidran.presentation.progress.ProgressViewModel
import com.Vugfeve7.Hidran.presentation.result.ResultViewModel
import com.Vugfeve7.Hidran.presentation.settings.SettingsViewModel
import com.Vugfeve7.Hidran.presentation.splash.SplashViewModel

class AppViewModelFactory(
    private val progressRepository: ProgressRepository,
    private val settingsRepository: SettingsRepository,
    private val spinReels: SpinReelsUseCase,
    private val awardStars: AwardStarsUseCase,
    private val unlockRoom: UnlockRoomUseCase,
    private val grantCard: GrantCollectionCardUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T = when {
        modelClass.isAssignableFrom(AppViewModel::class.java) ->
            AppViewModel()

        modelClass.isAssignableFrom(SplashViewModel::class.java) ->
            SplashViewModel()

        modelClass.isAssignableFrom(MenuViewModel::class.java) ->
            MenuViewModel(progressRepository, settingsRepository)

        modelClass.isAssignableFrom(LevelSelectViewModel::class.java) ->
            LevelSelectViewModel(progressRepository, settingsRepository, unlockRoom)

        modelClass.isAssignableFrom(GameViewModel::class.java) ->
            GameViewModel(progressRepository, settingsRepository, spinReels, awardStars, grantCard)

        modelClass.isAssignableFrom(ResultViewModel::class.java) ->
            ResultViewModel(progressRepository)

        modelClass.isAssignableFrom(CollectionViewModel::class.java) ->
            CollectionViewModel(progressRepository)

        modelClass.isAssignableFrom(ProgressViewModel::class.java) ->
            ProgressViewModel(progressRepository)

        modelClass.isAssignableFrom(SettingsViewModel::class.java) ->
            SettingsViewModel(settingsRepository, progressRepository)

        else -> error("unknown view model")
    } as T
}

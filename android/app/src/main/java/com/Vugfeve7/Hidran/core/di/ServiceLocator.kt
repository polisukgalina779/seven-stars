package com.Vugfeve7.Hidran.core.di

import android.content.Context
import com.Vugfeve7.Hidran.data.local.PreferencesStore
import com.Vugfeve7.Hidran.data.repository.ProgressRepositoryImpl
import com.Vugfeve7.Hidran.data.repository.SettingsRepositoryImpl
import com.Vugfeve7.Hidran.domain.usecase.AwardStarsUseCase
import com.Vugfeve7.Hidran.domain.usecase.EvaluatePayLinesUseCase
import com.Vugfeve7.Hidran.domain.usecase.GrantCollectionCardUseCase
import com.Vugfeve7.Hidran.domain.usecase.SpinReelsUseCase
import com.Vugfeve7.Hidran.domain.usecase.UnlockRoomUseCase

object ServiceLocator {

    private var factory: AppViewModelFactory? = null

    fun initialise(context: Context) {
        if (factory != null) return
        val store = PreferencesStore(context.applicationContext)
        val progressRepository = ProgressRepositoryImpl(store)
        val settingsRepository = SettingsRepositoryImpl(store)
        factory = AppViewModelFactory(
            progressRepository = progressRepository,
            settingsRepository = settingsRepository,
            spinReels = SpinReelsUseCase(EvaluatePayLinesUseCase()),
            awardStars = AwardStarsUseCase(),
            unlockRoom = UnlockRoomUseCase(progressRepository),
            grantCard = GrantCollectionCardUseCase()
        )
    }

    val viewModelFactory: AppViewModelFactory
        get() = factory ?: error("service locator not initialised")
}

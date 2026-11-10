package com.Vugfeve7.Hidran.presentation.app

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Vugfeve7.Hidran.core.di.ServiceLocator
import com.Vugfeve7.Hidran.core.navigation.Screen
import com.Vugfeve7.Hidran.presentation.collection.CollectionRoute
import com.Vugfeve7.Hidran.presentation.game.GameRoute
import com.Vugfeve7.Hidran.presentation.levelselect.LevelSelectRoute
import com.Vugfeve7.Hidran.presentation.menu.MenuRoute
import com.Vugfeve7.Hidran.presentation.progress.ProgressRoute
import com.Vugfeve7.Hidran.presentation.result.ResultRoute
import com.Vugfeve7.Hidran.presentation.settings.SettingsRoute
import com.Vugfeve7.Hidran.presentation.splash.SplashRoute

@Composable
fun AppRoot() {
    val appViewModel: AppViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val screen by appViewModel.screen.collectAsStateWithLifecycle()
    val canGoBack by appViewModel.canGoBack.collectAsStateWithLifecycle()

    BackHandler(enabled = canGoBack) { appViewModel.back() }

    AnimatedContent(
        targetState = screen,
        modifier = Modifier.fillMaxSize(),
        transitionSpec = {
            (fadeIn(tween(220)) + slideInVertically(tween(220)) { full -> full / 8 })
                .togetherWith(fadeOut(tween(180)))
        },
        label = "appRoot"
    ) { current ->
        when (current) {
            is Screen.Splash -> SplashRoute(onFinished = { appViewModel.showMenu() })

            is Screen.Menu -> MenuRoute(
                onPlay = { roomIndex, mode -> appViewModel.openGame(roomIndex, mode) },
                onLevelSelect = { appViewModel.open(Screen.LevelSelect) },
                onCollection = { appViewModel.open(Screen.Collection) },
                onProgress = { appViewModel.open(Screen.Progress) },
                onSettings = { appViewModel.open(Screen.Settings) }
            )

            is Screen.LevelSelect -> LevelSelectRoute(
                onEnterRoom = { roomIndex, mode -> appViewModel.openGame(roomIndex, mode) },
                onMenu = { appViewModel.showMenu() }
            )

            is Screen.Game -> GameRoute(
                roomIndex = current.roomIndex,
                mode = current.mode,
                token = current.token,
                onRunComplete = { appViewModel.replaceTop(Screen.Result) },
                onMenu = { appViewModel.showMenu() }
            )

            is Screen.Result -> ResultRoute(
                onSpinAgain = { roomIndex, mode -> appViewModel.restartInto(roomIndex, mode) },
                onLevelSelect = { appViewModel.replaceTop(Screen.LevelSelect) },
                onMenu = { appViewModel.showMenu() }
            )

            is Screen.Collection -> CollectionRoute(
                onLevelSelect = { appViewModel.replaceTop(Screen.LevelSelect) },
                onMenu = { appViewModel.showMenu() }
            )

            is Screen.Progress -> ProgressRoute(
                onLevelSelect = { appViewModel.replaceTop(Screen.LevelSelect) },
                onMenu = { appViewModel.showMenu() }
            )

            is Screen.Settings -> SettingsRoute(
                onMenu = { appViewModel.showMenu() }
            )
        }
    }
}

package com.Vugfeve7.Hidran.core.navigation

import com.Vugfeve7.Hidran.domain.model.RiskMode

sealed interface Screen {

    data object Splash : Screen

    data object Menu : Screen

    data object LevelSelect : Screen

    data class Game(val roomIndex: Int, val mode: RiskMode, val token: Long) : Screen

    data object Result : Screen

    data object Collection : Screen

    data object Progress : Screen

    data object Settings : Screen
}

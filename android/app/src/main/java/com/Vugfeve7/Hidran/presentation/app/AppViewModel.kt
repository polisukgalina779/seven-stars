package com.Vugfeve7.Hidran.presentation.app

import androidx.lifecycle.ViewModel
import com.Vugfeve7.Hidran.core.navigation.Screen
import com.Vugfeve7.Hidran.domain.model.RiskMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppViewModel : ViewModel() {

    private val backStack = mutableListOf<Screen>(Screen.Splash)

    private val _screen = MutableStateFlow<Screen>(Screen.Splash)
    val screen: StateFlow<Screen> = _screen.asStateFlow()

    private val _canGoBack = MutableStateFlow(false)
    val canGoBack: StateFlow<Boolean> = _canGoBack.asStateFlow()

    private var gameToken = 0L

    fun showMenu() {
        backStack.clear()
        backStack.add(Screen.Menu)
        sync()
    }

    fun open(destination: Screen) {
        backStack.add(destination)
        sync()
    }

    fun openGame(roomIndex: Int, mode: RiskMode) {
        gameToken += 1
        backStack.add(Screen.Game(roomIndex, mode, gameToken))
        sync()
    }

    fun restartInto(roomIndex: Int, mode: RiskMode) {
        gameToken += 1
        dropTop()
        backStack.add(Screen.Game(roomIndex, mode, gameToken))
        sync()
    }

    fun replaceTop(destination: Screen) {
        dropTop()
        backStack.add(destination)
        sync()
    }

    fun back() {
        if (backStack.size > 1) {
            dropTop()
            sync()
        }
    }

    private fun dropTop() {
        if (backStack.isNotEmpty()) backStack.removeAt(backStack.lastIndex)
    }

    private fun sync() {
        if (backStack.isEmpty()) backStack.add(Screen.Menu)
        _screen.value = backStack.last()
        _canGoBack.value = backStack.size > 1
    }
}

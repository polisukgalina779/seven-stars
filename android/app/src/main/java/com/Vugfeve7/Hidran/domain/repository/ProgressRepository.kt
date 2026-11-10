package com.Vugfeve7.Hidran.domain.repository

import com.Vugfeve7.Hidran.domain.model.PlayerProfile
import com.Vugfeve7.Hidran.domain.model.RunResult
import kotlinx.coroutines.flow.StateFlow

interface ProgressRepository {
    val profile: StateFlow<PlayerProfile>
    val lastRun: StateFlow<RunResult?>
    fun update(transform: (PlayerProfile) -> PlayerProfile)
    fun publishRun(result: RunResult)
    fun clearRun()
    fun reset()
}

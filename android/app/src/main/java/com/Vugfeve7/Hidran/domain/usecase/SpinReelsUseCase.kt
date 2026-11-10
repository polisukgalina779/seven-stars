package com.Vugfeve7.Hidran.domain.usecase

import com.Vugfeve7.Hidran.core.config.GameConfig
import com.Vugfeve7.Hidran.domain.model.RiskMode
import com.Vugfeve7.Hidran.domain.model.SlotSymbol
import com.Vugfeve7.Hidran.domain.model.SpinOutcome
import kotlin.random.Random

class SpinReelsUseCase(
    private val evaluatePayLines: EvaluatePayLinesUseCase
) {

    operator fun invoke(
        mode: RiskMode,
        lineStake: Int,
        seed: Long,
        wildBoost: Boolean
    ): SpinOutcome {
        val random = Random(seed)
        val weights = weightsFor(mode, wildBoost)
        val strips = List(GameConfig.REELS) {
            List(GameConfig.STRIP_LENGTH) { pick(random, weights) }
        }
        val grid = strips.map { strip -> strip.take(GameConfig.ROWS) }
        val wins = evaluatePayLines(grid, lineStake, mode)
        return SpinOutcome(
            strips = strips,
            grid = grid,
            wins = wins,
            baseWin = wins.sumOf { it.amount },
            bestPayout = wins.maxOfOrNull { it.symbol.payout } ?: 0,
            seed = seed
        )
    }

    fun seedFor(roomIndex: Int, runIndex: Int, spinIndex: Int): Long {
        val room = roomIndex * GameConfig.SEED_ROOM_FACTOR
        val run = (runIndex * 31L + spinIndex) * GameConfig.SEED_RUN_FACTOR
        return room xor run
    }

    private fun weightsFor(mode: RiskMode, wildBoost: Boolean): List<Int> {
        if (!wildBoost) return mode.weights
        return mode.weights.mapIndexed { index, weight ->
            if (index == SlotSymbol.WILD.ordinal) weight * GameConfig.WILD_TILT_PERCENT / 100 else weight
        }
    }

    private fun pick(random: Random, weights: List<Int>): SlotSymbol {
        val total = weights.sum()
        var roll = random.nextInt(total)
        weights.forEachIndexed { index, weight ->
            roll -= weight
            if (roll < 0) return SlotSymbol.entries[index]
        }
        return SlotSymbol.COMMON
    }
}

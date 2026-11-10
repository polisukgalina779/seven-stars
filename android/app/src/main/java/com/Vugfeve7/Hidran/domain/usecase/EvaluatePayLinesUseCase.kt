package com.Vugfeve7.Hidran.domain.usecase

import com.Vugfeve7.Hidran.domain.model.PayLineWin
import com.Vugfeve7.Hidran.domain.model.RiskMode
import com.Vugfeve7.Hidran.domain.model.SlotSymbol

class EvaluatePayLinesUseCase {

    operator fun invoke(
        grid: List<List<SlotSymbol>>,
        lineStake: Int,
        mode: RiskMode
    ): List<PayLineWin> {
        val wins = mutableListOf<PayLineWin>()
        PAY_LINES.forEachIndexed { lineIndex, rows ->
            val cells = rows.mapIndexed { column, row -> grid[column][row] }
            val symbol = resolve(cells)
            if (symbol != null) {
                val amount = lineStake * symbol.payout * mode.payoutScalarPercent / 100
                if (amount > 0) {
                    wins.add(PayLineWin(lineIndex, symbol, amount))
                }
            }
        }
        return wins
    }

    private fun resolve(cells: List<SlotSymbol>): SlotSymbol? {
        val solid = cells.filterNot { it.isWild }.distinct()
        return when {
            solid.isEmpty() -> SlotSymbol.WILD
            solid.size == 1 -> solid.first()
            else -> null
        }
    }

    companion object {
        val PAY_LINES: List<List<Int>> = listOf(
            listOf(1, 1, 1),
            listOf(0, 1, 2),
            listOf(2, 1, 0)
        )
    }
}

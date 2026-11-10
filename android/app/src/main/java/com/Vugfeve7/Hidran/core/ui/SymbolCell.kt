package com.Vugfeve7.Hidran.core.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import com.Vugfeve7.Hidran.R
import com.Vugfeve7.Hidran.core.theme.AccentGold
import com.Vugfeve7.Hidran.core.theme.AccentGoldLight
import com.Vugfeve7.Hidran.core.theme.DividerLine
import com.Vugfeve7.Hidran.core.theme.Palette
import com.Vugfeve7.Hidran.domain.model.SlotSymbol

@Composable
fun SymbolCell(
    symbol: SlotSymbol,
    size: Dp,
    modifier: Modifier = Modifier,
    highlighted: Boolean = false,
    inset: Dp = 11.dp,
    spriteScale: Float = 1f
) {
    Surface(
        modifier = modifier.size(size),
        shape = RoundedCornerShape(12.dp),
        color = androidx.compose.ui.graphics.Color.Transparent,
        border = BorderStroke(1.dp, if (highlighted) AccentGoldLight else DividerLine)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Palette.cellFill)
                .background(
                    if (highlighted) AccentGold.copy(alpha = 0.20f) else androidx.compose.ui.graphics.Color.Transparent
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(symbolDrawable(symbol)),
                contentDescription = stringResource(R.string.cd_symbol, stringResource(symbolTierRes(symbol))),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(inset)
                    .scale(spriteScale)
            )
        }
    }
}

package com.Vugfeve7.Hidran.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Vugfeve7.Hidran.R
import com.Vugfeve7.Hidran.core.theme.AccentGoldLight
import com.Vugfeve7.Hidran.core.theme.DividerLine

@Composable
fun StarRow(
    earned: Int,
    total: Int,
    modifier: Modifier = Modifier,
    size: Dp = 16.dp,
    gap: Dp = 3.dp
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(gap),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(total) { index ->
            Icon(
                painter = painterResource(R.drawable.ic_star),
                contentDescription = null,
                tint = if (index < earned) AccentGoldLight else DividerLine,
                modifier = Modifier.size(size)
            )
        }
    }
}
